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
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
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

	@Test
	public void testSequencePayCancBankException(@Mocked final BankInterface bankInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new BankException();
			}
		};

		this.adventureDifferentDays.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testSequencePayCancBankRemoteAccessException(@Mocked final BankInterface bankInterface) {

		new StrictExpectations() {
			{
				while (numberProcess++ != 3) {
					BankInterface.processPayment(IBAN, AMOUNT);
					this.result = new RemoteAccessException();
				}
			}
		};

		numberProcess = 0;
		while (numberProcess++ != 3) {
			this.adventureDifferentDays.process();
		}

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testSequencePayResUndCancActivityException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);
				this.result = new ActivityException();
			}
		};

		while (numberProcess++ != 3) {
			this.adventureDifferentDays.process();
			if (numberProcess == 2) {
				Assert.assertEquals(Adventure.State.UNDO, this.adventureDifferentDays.getState());
			}
		}

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testSequencePayResUndCancActivityRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				while (numberProcess++ != 5) {
					ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);
					this.result = new RemoteAccessException();
				}
			}
		};

		numberProcess = 0;
		while (numberProcess++ != 7) {
			this.adventureDifferentDays.process();
			if (numberProcess == 6) {
				Assert.assertEquals(Adventure.State.UNDO, this.adventureDifferentDays.getState());
			}
		}

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testSequencePayResBookUndCancHotelException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
				this.result = new HotelException();
			}
		};

		while (numberProcess++ != 4) {
			this.adventureDifferentDays.process();
			if (numberProcess == 3) {
				Assert.assertEquals(Adventure.State.UNDO, this.adventureDifferentDays.getState());
			}
		}

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testSequencePayResBookUndCancHotelRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				while (numberProcess++ != 10) {
					HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
					this.result = new RemoteAccessException();
				}
			}
		};

		numberProcess = 0;
		while (numberProcess++ != 13) {
			this.adventureDifferentDays.process();
			if (numberProcess == 12) {
				Assert.assertEquals(Adventure.State.UNDO, this.adventureDifferentDays.getState());
			}
		}

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testSequencePayResBookConfUndCancBankException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));

				while (numberProcess++ != 5) {
					BankInterface.getOperationData(this.anyString);
					this.result = new BankException();
				}
			}
		};

		numberProcess = 0;
		while (numberProcess++ != 9) {
			this.adventureDifferentDays.process();
			if (numberProcess == 8) {
				Assert.assertEquals(Adventure.State.UNDO, this.adventureDifferentDays.getState());
			}
		}

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testSequencePayResBookConfUndCancBankRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));

				while (numberProcess++ != 20) {
					BankInterface.getOperationData(this.anyString);
					this.result = new RemoteAccessException();
				}
			}
		};

		numberProcess = 0;
		while (numberProcess++ != 24) {
			this.adventureDifferentDays.process();
			if (numberProcess == 23) {
				Assert.assertEquals(Adventure.State.UNDO, this.adventureDifferentDays.getState());
			}
		}

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testSequencePayResBookConfUndCancActivityException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));

				BankInterface.getOperationData(this.anyString);

				ActivityInterface.getActivityReservationData(this.anyString);
				this.result = new ActivityException();

			}
		};

		while (numberProcess++ != 5) {
			this.adventureDifferentDays.process();
			if (numberProcess == 4) {
				Assert.assertEquals(Adventure.State.UNDO, this.adventureDifferentDays.getState());
			}
		}

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testSequencePayResBookConfUndCancActivityRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));

				while (numberProcess++ != 20) {
					BankInterface.getOperationData(this.anyString);

					ActivityInterface.getActivityReservationData(this.anyString);
					this.result = new RemoteAccessException();
				}
			}
		};

		numberProcess = 0;
		while (numberProcess++ != 24) {
			this.adventureDifferentDays.process();
			if (numberProcess == 23) {
				Assert.assertEquals(Adventure.State.UNDO, this.adventureDifferentDays.getState());
			}
		}

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testSequencePayResBookConfUndCancHotelException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
				this.result = this.anyString;

				BankInterface.getOperationData(this.anyString);

				ActivityInterface.getActivityReservationData(this.anyString);

				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new HotelException();

				HotelInterface.cancelBooking(this.anyString);
				this.result = this.anyString;

			}
		};

		while (numberProcess++ != 5) {
			this.adventureDifferentDays.process();
			if (numberProcess == 4) {
				Assert.assertEquals(Adventure.State.UNDO, this.adventureDifferentDays.getState());
			}
		}

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testSequencePayResBookConfUndCancHotelRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
				this.result = this.anyString;

				while (numberProcess++ != 20) {
					BankInterface.getOperationData(this.anyString);

					ActivityInterface.getActivityReservationData(this.anyString);

					HotelInterface.getRoomBookingData(this.anyString);
					this.result = new RemoteAccessException();
				}

				HotelInterface.cancelBooking(this.anyString);
				this.result = this.anyString;
			}
		};

		numberProcess = 0;
		while (numberProcess++ != 24) {
			this.adventureDifferentDays.process();
			if (numberProcess == 23) {
				Assert.assertEquals(Adventure.State.UNDO, this.adventureDifferentDays.getState());
			}
		}

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventureDifferentDays.getState());

	}
}
