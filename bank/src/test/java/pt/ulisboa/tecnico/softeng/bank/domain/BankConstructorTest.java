package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankConstructorTest {

	@Before
	public void setUp() {

	}

	@Test
	public void success() {
		Bank bank = new Bank("Money", "BK01");

		Assert.assertEquals("Money", bank.getName());
		Assert.assertEquals("BK01", bank.getCode());
		Assert.assertEquals(1, Bank.banks.size());
		Assert.assertEquals(0, bank.getNumberOfAccounts());
		Assert.assertEquals(0, bank.getNumberOfClients());
	}

	@Test(expected = BankException.class)
	public void testCheckNullArguments() {
		Bank bank = new Bank(null, null);

	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}
}
