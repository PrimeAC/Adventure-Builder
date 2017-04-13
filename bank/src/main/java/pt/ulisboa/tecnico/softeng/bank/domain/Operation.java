package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.DateTime;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Operation extends Operation_Base {
	public static enum Type {
		DEPOSIT, WITHDRAW
	};

	private static int counter = 0;

	//private final String reference;
	//private final Type type;
	private final Account account;
	//private final int value;
	//private final DateTime time;

	public Operation(Type type, Account account, int value) {
		checkArguments(type, account, value);

		//this.reference = account.getBank().getCode() + Integer.toString(++Operation.counter);
		setReference(generateReference(account));
		//this.type = type;
		setType(type);
		this.account = account;
		//this.value = value;
		setValue(value);
		//this.time = DateTime.now();
		setTime(DateTime.now());

		//account.getBank().addLog(this);
		setBank(account.getBank());
	}

	private String generateReference(Account account) { return account.getBank().getCode() + Integer.toString(++Operation.counter);}

	private void checkArguments(Type type, Account account, int value) {
		if (type == null || account == null || value <= 0) {
			throw new BankException();
		}
	}

	/*public String getReference() {
		return this.reference;
	}

	public Type getType() {
		return this.type;
	}
	*/
	public Account getAccount() {
		return this.account;
	}

	/*public int getValue() {
		return this.value;
	}

	public DateTime getTime() {
		return this.time;
	}
*/
	public String revert() {
		switch (getType()) {
		case DEPOSIT:
			return this.account.withdraw(getValue());
		case WITHDRAW:
			return this.account.deposit(getValue());
		default:
			throw new BankException();

		}


	}

	public void delete() {
		setBank(null);
		super.deleteDomainObject();
	}

}
