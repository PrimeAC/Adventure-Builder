package pt.ulisboa.tecnico.softeng.broker.domain;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

@RunWith(JMockit.class)
public class AdventureSequenceTest {
    private static final String IBAN = "BK01987654321";
    private static final int AMOUNT = 300;
    private static final int AGE = 20;
    private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
    private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
    private static final String ROOM_CONFIRMATION = "RoomConfirmation";
    private final LocalDate begin = new LocalDate(2016, 12, 19);
    private final LocalDate end = new LocalDate(2016, 12, 19);
    private Adventure adventureSameDay, adventureDifferentDays;
    private int numberProcess;

    @Injectable
    private Broker broker;

    @Before
    public void setUp() {
        this.adventureSameDay = new Adventure(this.broker, this.begin, this.end, AGE, IBAN, AMOUNT);
        this.adventureDifferentDays = new Adventure(this.broker, this.begin, this.end.plusDays(1), AGE, IBAN, AMOUNT);
        numberProcess = 0;
    }

    @Test
    public void testTransitionPaymentActivitySequencePayActConf(@Mocked final BankInterface bankInterface,
                    @Mocked final ActivityInterface activityInterface) {
        this.adventureSameDay.setPaymentConfirmation(PAYMENT_CONFIRMATION);

        new StrictExpectations() {
            {
                BankInterface.processPayment(IBAN, AMOUNT);
            }
        };

        this.adventureSameDay.process();

        Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureSameDay.getState());
    }

    @Test
    public void testTransitionActivityConfirmedSequencePayActConf(@Mocked final BankInterface bankInterface,
                                                                @Mocked final ActivityInterface activityInterface) {
        this.adventureSameDay.setPaymentConfirmation(PAYMENT_CONFIRMATION);
        this.adventureSameDay.setActivityConfirmation(ACTIVITY_CONFIRMATION);

        new StrictExpectations() {
            {
                BankInterface.processPayment(IBAN, AMOUNT);
                ActivityInterface.reserveActivity(begin, end, AGE);
            }
        };

        while (numberProcess++ != 2) { this.adventureSameDay.process(); }

        Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureSameDay.getState());
    }

    @Test
    public void testTransitionActivityRoomSequencePayActRooConf(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
        this.adventureDifferentDays.setPaymentConfirmation(PAYMENT_CONFIRMATION);
        this.adventureDifferentDays.setActivityConfirmation(ACTIVITY_CONFIRMATION);

        new StrictExpectations() {
            {
                BankInterface.processPayment(IBAN, AMOUNT);
                ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);
            }
        };

        while (numberProcess++ != 2) { this.adventureDifferentDays.process(); }

        Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventureDifferentDays.getState());
    }

    @Test
    public void testTransitionRoomConfirmedSequencePayActRooConf(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
        this.adventureDifferentDays.setPaymentConfirmation(PAYMENT_CONFIRMATION);
        this.adventureDifferentDays.setActivityConfirmation(ACTIVITY_CONFIRMATION);
        this.adventureDifferentDays.setRoomConfirmation(ROOM_CONFIRMATION);

        new StrictExpectations() {
            {
                BankInterface.processPayment(IBAN, AMOUNT);
                ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);
                HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
            }
        };

        while (numberProcess++ != 3) { this.adventureDifferentDays.process(); }

        Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureDifferentDays.getState());
    }
}
