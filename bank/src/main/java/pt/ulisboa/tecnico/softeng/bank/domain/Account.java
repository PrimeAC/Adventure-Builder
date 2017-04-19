package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Account extends Account_Base {
	private static int counter = 0;

	private final Client client;

	public Account(Bank bank, Client client) {
		checkArguments(bank, client);

		this.client = client;
		setIBAN(generateIBAN(bank));
		setBalance(0);

		bank.addAccount(this);
		setBank(bank);
	}

	private String generateIBAN(Bank bank) {
		return bank.getCode() + Integer.toString(++Account.counter);
	}

	private void checkArguments(Bank bank, Client client) {
		if (bank == null || client == null) {
			throw new BankException();
		}

		if (!bank.hasClient(client)) {
			throw new BankException();
		}

	}


	public Client getClient() {
		return this.client;
	}


	public String deposit(int amount) {
		if (amount <= 0) {
			throw new BankException();
		}

		setBalance(getBalance() + amount);

		Operation operation = new Operation(Operation.Type.DEPOSIT, this, amount);
		return operation.getReference();
	}

	public String withdraw(int amount) {
		if (amount <= 0 || amount > getBalance()) {
			throw new BankException();
		}

		setBalance(getBalance() - amount);

		return new Operation(Operation.Type.WITHDRAW, this, amount).getReference();
	}

	public void delete() {
		setBank(null);
		super.deleteDomainObject();
	}
}
