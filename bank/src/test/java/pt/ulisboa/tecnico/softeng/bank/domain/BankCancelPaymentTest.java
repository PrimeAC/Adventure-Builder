package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class BankCancelPaymentTest {

	private static final int INITIAL_ACCOUNT_BALANCE = 1000;
	private static final int PAYMENT_AMOUNT = 100;
	private static final int BALANCE_AFTER_PAYMENT = INITIAL_ACCOUNT_BALANCE - PAYMENT_AMOUNT;

	public Bank bank;
	public Account account;
	public Client client;
	public String paymentID;

	@Before
	public void setUp() {
		bank = new Bank("testBank", "1234");
		client = new Client(bank, "testClient");
		account = new Account(bank, client);
		account.deposit(INITIAL_ACCOUNT_BALANCE);
		paymentID = Bank.processPayment(account.getIBAN(), PAYMENT_AMOUNT);
	}

	@Test
	public void success() {
		String opReference = Bank.cancelPayment(paymentID);

		Operation operation = bank.getOperation(opReference);

		assertEquals(Operation.Type.DEPOSIT, operation.getType());
		assertEquals(account.getIBAN(), operation.getAccount().getIBAN());
		assertEquals(PAYMENT_AMOUNT, operation.getValue());

		assertEquals("Unexpected account amount", INITIAL_ACCOUNT_BALANCE, account.getBalance());
		assertNotNull("Operation not created", bank.getOperation(opReference));
	}

	@Test(expected = BankException.class)
	public void failureNullPaymentReference() {
		try {
			Bank.cancelPayment(null);
		} catch (BankException e) {
			assertEquals(BALANCE_AFTER_PAYMENT, account.getBalance());
			throw e;
		}
	}

	@Test(expected = BankException.class)
	public void failureInvalidPaymentReference() {
		try {
			Bank.cancelPayment(paymentID + "invalidSuffix");
		} catch (BankException e) {
			assertEquals(BALANCE_AFTER_PAYMENT, account.getBalance());
			throw e;
		}
	}

	@Test
	public void failureCancelPaymentTwice() {
		Bank.cancelPayment(paymentID);
		assertEquals(INITIAL_ACCOUNT_BALANCE, account.getBalance());
		try {
			Bank.cancelPayment(paymentID);
			fail();
		} catch (BankException e) {
			assertEquals(INITIAL_ACCOUNT_BALANCE, account.getBalance());
		}
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}
}
