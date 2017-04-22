package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.DateTime;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Operation extends Operation_Base {
	public static enum Type {
		DEPOSIT, WITHDRAW
	}

	private static int counter = 0;

	public Operation(Type type, Account account, int value) {
		checkArguments(type, account, value);

		setReference(generateReference(account));
		setType(type);
		setAccount(account);
		setValue(value);
		setTime(DateTime.now());

		setBank(account.getBank());
	}

	private String generateReference(Account account) {
		return account.getBank().getCode() + Integer.toString(++Operation.counter);
	}

	private void checkArguments(Type type, Account account, int value) {
		if (type == null || account == null || value <= 0) {
			throw new BankException();
		}
	}

	public String revert() {
		switch (getType()) {
			case DEPOSIT:
				return getAccount().withdraw(getValue());
			case WITHDRAW:
				return getAccount().deposit(getValue());
			default:
				throw new BankException();

		}


	}

	public void delete() {
		setBank(null);
		setAccount(null);
		super.deleteDomainObject();
	}
}
