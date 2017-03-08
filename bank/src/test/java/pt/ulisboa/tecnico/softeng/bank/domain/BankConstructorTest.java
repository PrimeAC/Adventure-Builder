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
	public void testCheckNullName() {
		Bank bank = new Bank(null, "BK02");

	}

	@Test(expected = BankException.class)
	public void testCheckNullCode() {
		Bank bank = new Bank("CGD", null);

	}

	@Test(expected = BankException.class)
	public void testCheckEmptyName() {
		Bank bank = new Bank("" , "BK03");

	}

	@Test(expected = BankException.class)
	public void testCheckEmptyCode() {
		Bank bank = new Bank("BPI" , "");

	}

	@Test(expected = BankException.class)
	public void testCheckOneWhitespacesName() {
		Bank bank = new Bank("ABC ", "BK01");

	}

	@Test(expected = BankException.class)
	public void testCheckOneWhitespacesCode() {
		Bank bank = new Bank("CGD", "BK1 ");

	}

	@Test(expected = BankException.class)
	public void testCheckTwoWhitespacesName() {
		Bank bank = new Bank("AB  ", "BK01");

	}

	@Test(expected = BankException.class)
	public void testCheckTwoWhitespacesCode() {
		Bank bank = new Bank("CGD", "BK  ");

	}

	@Test(expected = BankException.class)
	public void testCheckThreeWhitespacesName() {
		Bank bank = new Bank("A   ", "BK01");

	}

	@Test(expected = BankException.class)
	public void testCheckThreeWhitespacesCode() {
		Bank bank = new Bank("CGD", "B   ");

	}

	@Test(expected = BankException.class)
	public void testCheckAllWhitespacesName() {
		Bank bank = new Bank("    ", "BK01");

	}

	@Test(expected = BankException.class)
	public void testCheckAllWhitespacesCode() {
		Bank bank = new Bank("CGD", "    ");

	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}
}
