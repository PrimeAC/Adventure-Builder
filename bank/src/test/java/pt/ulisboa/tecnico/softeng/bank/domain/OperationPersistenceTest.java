package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OperationPersistenceTest {
	private static final String BANK_CODE = "BK01";
	private static final String BANK_NAME = "Bank";
	private static final String CLIENT_NAME = "Rick";
	private Operation operation;

	@Test
	public void success() {
		String iban = atomicCreate();
		atomicAssert(iban);
	}

	@Atomic(mode = TxMode.WRITE)
	public String atomicCreate() {
		Bank bank = new Bank(BANK_NAME, BANK_CODE);
		Client client = new Client(bank, CLIENT_NAME);
		Account account = new Account(bank, client);
		operation = new Operation(Operation.Type.DEPOSIT, account, 100);

		return account.getIBAN();
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert(String iban) {
		Bank bank = Bank.getBankByCode(BANK_CODE);
		assertNotNull(bank);

		Account dbAccount = bank.getAccount(iban);
		assertNotNull(dbAccount);
		assertEquals(operation.getAccount(), dbAccount);
		assertTrue(operation.getTime().isBefore(DateTime.now()));
		assertTrue(operation.getBank().equals(bank));
		assertEquals(100 ,operation.getValue());


	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		FenixFramework.getDomainRoot().getBankSet().forEach(Bank::delete);
	}
}
