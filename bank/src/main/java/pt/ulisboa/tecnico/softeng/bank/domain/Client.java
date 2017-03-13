package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class Client {
	private static int counter = 0;

	private final String name;
	private final String ID;

	public Client(Bank bank, String name) {
		checkInvalidArguments(bank, name);

		this.ID = Integer.toString(++Client.counter);
		this.name = name;

		bank.addClient(this);
	}

	private void checkInvalidArguments(Bank bank, String name) {
		if(bank ==  null) {
			throw new BankException("Invalid bank argument!");
		}

		else if(isBlank(name)) {
			throw new BankException("Invalid client name!");
		}

	}

	public String getName() {
		return this.name;
	}

	public String getID() {
		return this.ID;
	}

}
