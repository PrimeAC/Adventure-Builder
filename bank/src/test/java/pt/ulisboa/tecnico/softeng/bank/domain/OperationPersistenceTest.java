package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import static org.junit.Assert.assertTrue;

public class OperationPersistenceTest {
	private static final String BANK_CODE = "BK01";
	private static final String BANK_NAME = "Bank";
	private static final String CLIENT_NAME = "Rick";
	Account account;

	@Test
	public void success() {
		String reference = atomicCreate();
		atomicAssert(reference);
	}

	@Atomic(mode = TxMode.WRITE)
	public String atomicCreate() {
		Bank bank = new Bank(BANK_NAME, BANK_CODE);
		Client client = new Client(bank, CLIENT_NAME);
		account = new Account(bank, client);
		Operation operation = new Operation(Operation.Type.DEPOSIT, account, 100);

		return operation.getReference();
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert(String reference) {
		Bank bank = Bank.getBankByCode(BANK_CODE);

		Operation operation = bank.getOperation(reference);

		assertTrue(operation.getTime().isBefore(DateTime.now()));
		assertTrue(operation.getBank().equals(bank));
		assertEquals(100, operation.getValue());
		assertEquals(Operation.Type.DEPOSIT, operation.getType());
		assertEquals(account.getIBAN(), operation.getAccount().getIBAN());


	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		FenixFramework.getDomainRoot().getBankSet().forEach(Bank::delete);
	}
}
