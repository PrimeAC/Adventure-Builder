package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class ClientContructorMethodTest {
	Bank bank;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
	}

	@Test
	public void success() {
		Client client = new Client(this.bank, "António");

		Assert.assertEquals("António", client.getName());
		Assert.assertTrue(client.getID().length() >= 1);
		Assert.assertTrue(this.bank.hasClient(client));
	}

	@Test(expected = BankException.class)
	public void testCheckNullBank() {
		Client client = new Client(null, "Afonso");
	}

	@Test(expected = BankException.class)
	public void testCheckNullName() {
		Client client = new Client(this.bank, null);
	}

	@Test(expected = BankException.class)
	public void testCheckEmptyName() {
		Client client = new Client(this.bank, "");
	}

	@Test(expected = BankException.class)
	public void testCheckAllWhitespacesName() {
		Client client = new Client(this.bank, "    ");
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
