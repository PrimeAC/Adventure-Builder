package pt.ulisboa.tecnico.softeng.bank.domain;

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
		this.operation= new Operation(Operation.Type.DEPOSIT, this.account, 1);
	}
	
	@Test
	public void success() {
		Account result = this.bank.getAccount(this.account.getIBAN());
		Assert.assertEquals(this.account, result);
		Assert.assertEquals(this.client, result.getClient());
		
		BankOperationData bankOpData = Bank.getOperationData(this.operation.getReference());
		Assert.assertEquals(this.operation.getReference(), bankOpData.getReference());
		
		Assert.assertEquals(this.operation.getType().toString(), bankOpData.getType());
		Assert.assertEquals(this.operation.getAccount(), this.account);
		Assert.assertEquals(this.operation.getValue(), 1);
	}

	@Test(expected = BankException.class)
	public void nullReference() {
		Bank.getOperationData(null);
	}

	@Test(expected = BankException.class)
	public void blankReference() {
		Bank.getOperationData(" ");
	}
	
	@Test(expected = BankException.class)
	public void minReferenceSize() {
		//min size reference= 5
		Bank.getOperationData("AAAA");
	}
	
	@Test(expected = BankException.class)
	public void wrongReference() {
		Bank.getOperationData("AAAA0");
	}

	@Test(expected = BankException.class)
	public void emptySetOfBanks() {
		// check if the test is the same as previous, not supposed to be, this should check if static banks list is empty 1st
		Bank.banks.clear();
		Bank.getOperationData(this.operation.getReference());
	}

	@Test
	public void multipleBanks() {
		// testing with multiple banks 
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
	public void TypeEnumToStringConversion() {
		// this test checks type - enum to string conversion
		BankOperationData bankOpData = Bank.getOperationData(this.operation.getReference());
		Assert.assertEquals(bankOpData.getType(), this.operation.getType().toString());
	}
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}

	
}
