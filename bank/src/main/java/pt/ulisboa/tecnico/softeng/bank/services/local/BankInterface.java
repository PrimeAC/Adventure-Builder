package pt.ulisboa.tecnico.softeng.bank.services.local;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.AccountData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankOperationData;

public class BankInterface {

	@Atomic(mode = TxMode.READ)
	public static int getBalance(String iban) {
		return getAccountByIban(iban).getBalance();
	}

	@Atomic(mode = TxMode.WRITE)
	public static void deposit(AccountData accountData) {
		Account account = getAccountByIban(accountData.getIban());
		account.deposit(accountData.getAmount());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void widthraw(AccountData accountData) {
		Account account = getAccountByIban(accountData.getIban());
		account.withdraw(accountData.getAmount());
	}

	private static Account getAccountByIban(String iban) {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			Account account = bank.getAccount(iban);
			if (account != null) {
				return account;
			}
		}
		return null;
	}

	@Atomic(mode = TxMode.WRITE)
	public static String processPayment(String IBAN, int amount) {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			if (bank.getAccount(IBAN) != null) {
				return bank.getAccount(IBAN).withdraw(amount);
			}
		}
		throw new BankException();
	}

	@Atomic(mode = TxMode.WRITE)
	public static String cancelPayment(String paymentConfirmation) {
		Operation operation = getOperationByReference(paymentConfirmation);
		if (operation != null) {
			return operation.revert();
		}
		throw new BankException();
	}

	@Atomic(mode = TxMode.READ)
	public static BankOperationData getOperationData(String reference) {
		Operation operation = getOperationByReference(reference);
		if (operation != null) {
			return new BankOperationData(operation);
		}
		throw new BankException();
	}

	private static Operation getOperationByReference(String reference) {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			Operation operation = bank.getOperation(reference);
			if (operation != null) {
				return operation;
			}
		}
		return null;
	}

}
