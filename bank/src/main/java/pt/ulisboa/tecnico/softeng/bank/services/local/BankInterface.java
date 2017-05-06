package pt.ulisboa.tecnico.softeng.bank.services.local;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.AccountData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

import java.util.ArrayList;
import java.util.List;

public class BankInterface {

	@Atomic(mode = TxMode.READ)
	public static ClientData getClientData(String ID, String code, ClientData.CopyDepth depth) {

		Client client = getClientByID(ID, code);

		if (client != null)
			return new ClientData(client, depth);
		else
			return null;
	}

	@Atomic(mode = TxMode.READ)
	public static BankData getBankData(String code, BankData.CopyDepth depth) {

		Bank bank = getBankByCode(code);

		if (bank != null)
			return new BankData(bank, depth);
		else
			return null;
	}

	@Atomic(mode = TxMode.READ)
	public static List<BankData> getBanks() {
		List<BankData> banks = new ArrayList<>();
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			banks.add(new BankData(bank, BankData.CopyDepth.SHALLOW));
		}
		return banks;
	}

	@Atomic(mode = TxMode.READ)
	public static List<ClientData> getClients(String code) {
		List<ClientData> clients = new ArrayList<>();

		Bank bank = getBankByCode(code);

		for (Client client : bank.getClientSet()) {
			clients.add(new ClientData(client, ClientData.CopyDepth.SHALLOW));
		}
		return clients;
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createBank(BankData bankData) {
		new Bank(bankData.getName(), bankData.getCode());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createClient(ClientData clientData, String code) {
		Bank bank = getBankByCode(code);
		new Client(bank, clientData.getName());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createAccount(AccountData accountData) {
		new Account(accountData.getBank(), accountData.getClient());
	}

	public static int getBalance(String iban) {
		return getAccountByIban(iban).getBalance();
	}

	@Atomic(mode = TxMode.WRITE)
	public static void deposit(AccountData accountData) {
		Account account = getAccountByIban(accountData.getIBAN());
		account.deposit(accountData.getAmount());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void widthraw(AccountData accountData) {
		Account account = getAccountByIban(accountData.getIBAN());
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

	private static Client getClientByID(String ID, String code) {

		Bank bank = getBankByCode(code);

		for (Client client : bank.getClientSet()) {
			if (client.getID().equals(ID))
				return client;
		}
		return null;
	}

	private static Bank getBankByCode(String code) {

		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			if (bank.getCode().equals(code))
				return bank;
		}
		return null;
	}

}
