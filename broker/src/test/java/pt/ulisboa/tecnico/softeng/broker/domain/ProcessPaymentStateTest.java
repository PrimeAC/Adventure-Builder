package pt.ulisboa.tecnico.softeng.broker.domain;


import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(JMockit.class)
public class ProcessPaymentStateTest {

    private static final String IBAN = "BK01987654321";
    private static final int AMOUNT = 300;
    private static final int AGE = 20;
    private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";

    private final LocalDate begin = LocalDate.now();
    private final LocalDate end = begin.plusDays(5);
    private Adventure adventure;


    @Injectable
    private Broker broker;

    @Before
    public void setUp() {
        this.adventure = new Adventure(this.broker, this.begin, this.end, AGE, IBAN, AMOUNT);
        this.adventure.setState(Adventure.State.PROCESS_PAYMENT);
    }

    @Test
    public void paymentSuccess(@Mocked BankInterface bankInterface) {

        new Expectations() {{
            bankInterface.processPayment(IBAN, AMOUNT);
            result = PAYMENT_CONFIRMATION;
        }};

        adventure.process();

        assertEquals(PAYMENT_CONFIRMATION, adventure.getPaymentConfirmation());
        assertEquals(Adventure.State.RESERVE_ACTIVITY, adventure.getState());

        new Verifications() {{
            bankInterface.processPayment(IBAN, AMOUNT);
            times = 1;
        }};
    }

    @Test
    public void bankException(@Mocked BankInterface bankInterface) {

        new Expectations() {{
            bankInterface.processPayment(IBAN, AMOUNT);
            result = new BankException();
        }};

        adventure.process();

        assertNull(adventure.getPaymentConfirmation());
        assertEquals(Adventure.State.CANCELLED, adventure.getState());

        new Verifications() {{
            bankInterface.processPayment(IBAN, AMOUNT);
            times = 1;
        }};
    }

    @Test
    public void remoteAccessException(@Mocked BankInterface bankInterface) {
        new Expectations() {{
            bankInterface.processPayment(IBAN, AMOUNT);
            result = new RemoteAccessException();
        }};

        int i = 0;
        while (adventure.getState().equals(Adventure.State.PROCESS_PAYMENT)) {
            adventure.process();
            i++;
        }

        assertNull(adventure.getPaymentConfirmation());
        assertEquals(Adventure.State.CANCELLED, adventure.getState());
        assertEquals(3, i);

        new Verifications() {{
            bankInterface.processPayment(IBAN, AMOUNT);
            times = 3;
        }};
    }

}