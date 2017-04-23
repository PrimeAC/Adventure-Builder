package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import static org.junit.Assert.assertNotNull;

public class AccountPersistenceTest {
	private static final String BANK_CODE = "BK01";
	private static final String BANK_NAME = "Bank";
	private static final String CLIENT_NAME = "Rick";
	private static final int ACCOUNT_BALANCE = 100;

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

		account.deposit(100);

		return account.getIBAN();
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert(String iban) {
		Bank bank = Bank.getBankByCode(BANK_CODE);
		assertNotNull(bank);

		Account dbAccount = bank.getAccount(iban);
		Client client = dbAccount.getClient();


		assertNotNull(dbAccount);
		assertEquals(ACCOUNT_BALANCE, dbAccount.getBalance());
		assertEquals(BANK_CODE, dbAccount.getBank().getCode());
		assertEquals(BANK_NAME, dbAccount.getBank().getName());
		assertEquals(CLIENT_NAME, dbAccount.getClient().getName());
		assertEquals(1, client.getAccountSet().size());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		FenixFramework.getDomainRoot().getBankSet().forEach(Bank::delete);
	}

}
