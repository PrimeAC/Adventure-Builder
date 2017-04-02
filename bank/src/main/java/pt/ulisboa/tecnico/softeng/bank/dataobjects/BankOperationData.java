package pt.ulisboa.tecnico.softeng.bank.dataobjects;

import org.joda.time.LocalDateTime;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankOperationData {
	private String reference;
	private String type;
	private String iban;
	private int value;
	private LocalDateTime time;

	public BankOperationData() {
		return;
	}

	public BankOperationData(String reference, String type, String iban, int value, LocalDateTime time) {
		checkArguments(reference, type, iban, value, time);
		this.reference = reference; // nao pode haver referencias repetidas
		this.type = type;
		this.iban = iban;
		this.value = value;
		this.time = time;
	}

	private void checkArguments(String reference, String type, String iban, int value, LocalDateTime time) {
		if (reference.trim().length() < 5 || reference == null)
			throw new BankException();
		if (type.trim().length() < 5 || type == null)
			throw new BankException();
		if (iban.trim().length() < 5 || iban == null)
			throw new BankException();
		if (value <= 0)
			throw new BankException();
		if (time == null)
			throw new BankException();
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public LocalDateTime getTime() {
		return this.time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}
}
