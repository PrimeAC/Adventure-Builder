package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import static org.junit.Assert.assertEquals;

public class BankPersistenceTest {
	private static final String BANK_CODE = "BK01";

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		new Bank("Money", BANK_CODE);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		Bank bank = Bank.getBankByCode(BANK_CODE);

		assertEquals("Money", bank.getName());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			bank.delete();
		}
	}

}
