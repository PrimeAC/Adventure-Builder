package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;

public class AccountData {

	String IBAN;
	int balance;

	public AccountData() {
	}

	public AccountData(Account account) {
		this.IBAN = account.getIBAN();
		this.balance = account.getBalance();
	}

	public String getIBAN() {
		return this.IBAN;
	}

	public void setIBAN(String IBAN) {
		this.IBAN = IBAN;
	}

	public int getBalance() {
		return this.balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

}
