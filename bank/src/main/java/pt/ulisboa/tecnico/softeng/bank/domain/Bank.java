package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Bank extends Bank_Base {
	public static final int CODE_SIZE = 4;

	public Bank(String name, String code) {
		checkArguments(name, code);

		setName(name);
		setCode(code);

		FenixFramework.getDomainRoot().addBank(this);
	}

	public void delete() {
		getAccountSet().forEach(Account::delete);
		getOperationSet().forEach(Operation::delete);
		getClientSet().forEach(Client::delete);
    
		setRoot(null);
		deleteDomainObject();
	}

	private void checkArguments(String name, String code) {
		if (name == null || code == null || name.trim().equals("") || code.trim().equals("")) {
			throw new BankException();
		}

		if (code.length() != Bank.CODE_SIZE) {
			throw new BankException();
		}

		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			if (bank.getCode().equals(code)) {
				throw new BankException();
			}
		}
	}

	int getNumberOfAccounts() {
		return getAccountSet().size();
	}

	int getNumberOfClients() {
		return getClientSet().size();
	}

	boolean hasClient(Client client) {
		return getClientSet().contains(client);
	}

	public Account getAccount(String IBAN) {
		if (IBAN == null || IBAN.trim().equals("")) {
			throw new BankException();
		}

		for (Account account : getAccountSet()) {
			if (account.getIBAN().equals(IBAN)) {
				return account;
			}
		}

		return null;
	}

	public Operation getOperation(String reference) {
		for (Operation operation : getOperationSet()) {
			if (operation.getReference().equals(reference)) {
				return operation;
			}
		}
		return null;
	}

	public static Bank getBankByCode(String code) {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			if (bank.getCode().equals(code)) {
				return bank;
			}
		}
		return null;
	}

	public static Operation getOperationByReference(String reference) {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			Operation operation = bank.getOperation(reference);
			if (operation != null) {
				return operation;
			}
		}
		return null;
	}

	public static String processPayment(String IBAN, int amount) {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			if (bank.getAccount(IBAN) != null) {
				return bank.getAccount(IBAN).withdraw(amount);
			}
		}
		throw new BankException();
	}

	public static String cancelPayment(String paymentConfirmation) {
		Operation operation = getOperationByReference(paymentConfirmation);
		if (operation != null) {
			return operation.revert();
		}
		throw new BankException();
	}

	public static BankOperationData getOperationData(String reference) {
		Operation operation = getOperationByReference(reference);
		if (operation != null) {
			return new BankOperationData(operation);
		}
		throw new BankException();
	}

}
