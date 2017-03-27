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
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class AdventureSequenceTest {
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final int AGE = 20;
	private final LocalDate begin = LocalDate.now();
	private final LocalDate end = LocalDate.now();
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
	public void testTransitionPaymentActivitySequencePayActConf(@Mocked final BankInterface bankInterface) {

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

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				ActivityInterface.reserveActivity(begin, end, AGE);
			}
		};

		while (numberProcess++ != 2) {
			this.adventureSameDay.process();
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureSameDay.getState());
	}

	@Test
	public void testTransitionActivityRoomSequencePayActRooConf(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);
			}
		};

		while (numberProcess++ != 2) {
			this.adventureDifferentDays.process();
		}

		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventureDifferentDays.getState());
	}

	@Test
	public void testTransitionRoomConfirmedSequencePayActRooConf(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
			}
		};

		while (numberProcess++ != 3) {
			this.adventureDifferentDays.process();
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureDifferentDays.getState());
	}
}
