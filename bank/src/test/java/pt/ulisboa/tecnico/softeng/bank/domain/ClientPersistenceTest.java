package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class ClientPersistenceTest {
	private static final String BANK_CODE = "BK01";
	private static final String BANK_NAME = "Money";
	private static final String CLIENT_NAME = "Rick";

	Client client;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Bank bank = new Bank(BANK_NAME, BANK_CODE);
		client = new Client(bank, CLIENT_NAME);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		Set<Bank> banks = FenixFramework.getDomainRoot().getBankSet();

		assertEquals(1, banks.size());

		Bank bank = banks.iterator().next();
		Client client = bank.getClientSet().iterator().next();

		assertEquals(BANK_NAME, client.getBank().getName());
		assertEquals(BANK_CODE, client.getBank().getCode());
		assertEquals(CLIENT_NAME, client.getName());
		assertEquals(this.client.getID(), client.getID());
		assertEquals(1, bank.getClientSet().size());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		FenixFramework.getDomainRoot().getBankSet().forEach(Bank::delete);
	}

}
