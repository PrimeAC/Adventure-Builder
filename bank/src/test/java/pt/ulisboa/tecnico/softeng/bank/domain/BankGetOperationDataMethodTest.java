package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.Days;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankGetOperationDataMethodTest {

	private Bank bank;
	private Client client;
	private Account account;
	private Operation operation;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		this.client = new Client(this.bank, "António");
		this.account = new Account(this.bank, this.client);
		this.operation = new Operation(Operation.Type.DEPOSIT, this.account, 1);
	}

	@Test
	public void success() {
		Account result = this.bank.getAccount(this.account.getIBAN());

		Assert.assertEquals(this.account, result);
		Assert.assertEquals(this.client, result.getClient());

		Assert.assertEquals(Operation.Type.DEPOSIT, this.operation.getType());
		Assert.assertEquals(this.account, this.operation.getAccount());
		Assert.assertEquals(1, this.operation.getValue());

		BankOperationData bankOpData = Bank.getOperationData(this.operation.getReference());
		Assert.assertEquals(this.operation.getReference(), bankOpData.getReference());
	}

	@Test(expected = BankException.class)
	public void nullReference() {
		Bank.getOperationData(null);
	}

	@Test(expected = BankException.class)
	public void blankReference() {
		Bank.getOperationData("     ");
	}

	@Test(expected = BankException.class)
	public void minReferenceSize() {
		Bank.getOperationData("AAAA");
	}

	@Test(expected = BankException.class)
	public void wrongReference() {
		Bank.getOperationData("AAAA0");
	}

	@Test(expected = BankException.class)
	public void emptySetOfBanks() {
		Bank.banks.clear();
		Bank.getOperationData(this.operation.getReference());
	}

	@Test
	public void multipleBanks() {
		Bank bank2 = new Bank("Moneyz", "BK02");
		Client client2 = new Client(bank2, "António");
		Account account2 = new Account(bank2, client2);
		Operation operation2 = new Operation(Operation.Type.DEPOSIT, account2, 1000);

		BankOperationData bankOpData2 = Bank.getOperationData(operation2.getReference());
		Assert.assertEquals(operation2.getReference(), bankOpData2.getReference());

		BankOperationData bankOpData = Bank.getOperationData(this.operation.getReference());
		Assert.assertEquals(this.operation.getReference(), bankOpData.getReference());
	}

	@Test
	public void TypeOperationToBankOperationData() {
		BankOperationData bankOpData = Bank.getOperationData(this.operation.getReference());
		Assert.assertEquals(bankOpData.getType(), this.operation.getType().toString());
	}

	@Test
	public void ReferenceOperationToBankOperationData() {
		BankOperationData bankOpData = Bank.getOperationData(this.operation.getReference());
		Assert.assertEquals(bankOpData.getIban(), this.operation.getReference());
	}

	@Test
	public void ValueOperationToBankOperationData() {
		BankOperationData bankOpData = Bank.getOperationData(this.operation.getReference());
		Assert.assertEquals(bankOpData.getValue(), this.operation.getValue());
	}

	@Test
	public void TimeOperationToBankOperationData() {
		BankOperationData bankOpData = Bank.getOperationData(this.operation.getReference());
		int daysBetween = Days.daysBetween(bankOpData.getTime(), this.operation.getTime()).getDays();
		Assert.assertEquals(0, daysBetween);
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
