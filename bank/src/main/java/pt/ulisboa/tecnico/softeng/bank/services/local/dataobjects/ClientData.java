package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientData {

	public static enum CopyDepth {
		SHALLOW, ACCOUNTS
	};

	private String id;
	private String name;
	private Bank bank;
	private List<AccountData> accounts = new ArrayList<>();

	public ClientData() {
	}

	public ClientData(Client client, CopyDepth depth) {
		this.id = client.getID();
		this.name = client.getName();
		this.bank = client.getBank();
		switch (depth) {
			case ACCOUNTS:
				for (Account account : client.getAccountSet()) {
					this.accounts.add(new AccountData(account));
				}
				break;
			case SHALLOW:
				break;
			default:
				break;
		}
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bank getBank() {
		return this.bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public List<AccountData> getAccounts(){
		return this.accounts;
	}

}
