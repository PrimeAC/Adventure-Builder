package pt.ulisboa.tecnico.softeng.broker.domain;

import jdk.internal.cmm.SystemResourcePressureImpl;
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
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import sun.jvm.hotspot.interpreter.BytecodeMultiANewArray;

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
	public void testPaymentActivityConfirmed(@Mocked final BankInterface bankInterface,
											 @Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				ActivityInterface.reserveActivity(begin, end, AGE);
			}
		};

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureSameDay.getState());

		while (numberProcess++ != 2) {
			this.adventureSameDay.process();

			if (numberProcess == 1) {
				Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureSameDay.getState());
			}
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureSameDay.getState());
	}

	@Test
	public void testPaymentActivityConfirmedRemoteExceptionPayment(@Mocked final BankInterface bankInterface,
																   @Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				while (numberProcess++ != 2) {
					BankInterface.processPayment(IBAN, AMOUNT);
					this.result = new RemoteAccessException();
				}
				BankInterface.processPayment(IBAN, AMOUNT);

				ActivityInterface.reserveActivity(begin, end, AGE);
			}
		};

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureSameDay.getState());

		numberProcess = 0;
		while (numberProcess++ != 4) {
			this.adventureSameDay.process();

			if (numberProcess < 3)
				Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureSameDay.getState());
			else if (numberProcess == 3)
				Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureSameDay.getState());
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureSameDay.getState());
	}

	@Test
	public void testPaymentActivityConfirmedRemoteExceptionActivity(@Mocked final BankInterface bankInterface,
																	@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				while (numberProcess++ != 4) {
					ActivityInterface.reserveActivity(begin, end, AGE);
					this.result = new RemoteAccessException();
				}
				ActivityInterface.reserveActivity(begin, end, AGE);
			}
		};

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureSameDay.getState());

		numberProcess = 0;
		while (numberProcess++ != 6) {
			this.adventureSameDay.process();

			if (numberProcess < 6)
				Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureSameDay.getState());
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureSameDay.getState());
	}

	@Test
	public void testPaymentActivityConfirmedRemoteExceptionPaymentActivity(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				while (numberProcess++ != 2) {
					BankInterface.processPayment(IBAN, AMOUNT);
					this.result = new RemoteAccessException();
				}
				BankInterface.processPayment(IBAN, AMOUNT);

				numberProcess = 0;
				while (numberProcess++ != 4) {
					ActivityInterface.reserveActivity(begin, end, AGE);
					this.result = new RemoteAccessException();
				}
				ActivityInterface.reserveActivity(begin, end, AGE);
			}
		};

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureSameDay.getState());

		numberProcess = 0;
		while (numberProcess++ != 8) {
			this.adventureSameDay.process();

			if (numberProcess < 3)
				Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureSameDay.getState());
			else if (numberProcess < 8)
				Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureSameDay.getState());
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureSameDay.getState());
	}

	@Test
	public void testPaymentActivityRoomConfirmed(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
			}
		};

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureSameDay.getState());

		while (numberProcess++ != 3) {
			this.adventureDifferentDays.process();

			if (numberProcess == 1)
				Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureDifferentDays.getState());
			else if (numberProcess == 2)
				Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventureDifferentDays.getState());
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testPaymentActivityRoomConfirmedRemoteExceptionPayment(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				while (numberProcess++ != 2) {
					BankInterface.processPayment(IBAN, AMOUNT);
					this.result = new RemoteAccessException();
				}
				BankInterface.processPayment(IBAN, AMOUNT);

				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
			}
		};

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureSameDay.getState());

		numberProcess = 0;
		while (numberProcess++ != 5) {
			this.adventureDifferentDays.process();

			if (numberProcess < 3)
				Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureDifferentDays.getState());
			else if (numberProcess == 3)
				Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureDifferentDays.getState());
			else if (numberProcess == 4)
				Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventureDifferentDays.getState());
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testPaymentActivityRoomConfirmedRemoteExceptionActivity(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				while (numberProcess++ != 4) {
					ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);
					this.result = new RemoteAccessException();
				}
				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
			}
		};

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureDifferentDays.getState());

		numberProcess = 0;
		while (numberProcess++ != 7) {
			this.adventureDifferentDays.process();

			if (numberProcess < 6)
				Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureDifferentDays.getState());
			else if (numberProcess == 6)
				Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventureDifferentDays.getState());

		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testPaymentActivityRoomConfirmedRemoteExceptionPaymentActivity(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				while (numberProcess++ != 2) {
					BankInterface.processPayment(IBAN, AMOUNT);
					this.result = new RemoteAccessException();
				}
				BankInterface.processPayment(IBAN, AMOUNT);

				numberProcess = 0;
				while (numberProcess++ != 4) {
					ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);
					this.result = new RemoteAccessException();
				}
				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
			}
		};

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureDifferentDays.getState());

		numberProcess = 0;
		while (numberProcess++ != 9) {
			this.adventureDifferentDays.process();

			if (numberProcess < 3)
				Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureDifferentDays.getState());
			else if (numberProcess < 8)
				Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureDifferentDays.getState());
			else if (numberProcess == 8)
				Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventureDifferentDays.getState());
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testPaymentActivityRoomConfirmedRemoteExceptionRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				numberProcess = 0;
				while (numberProcess++ != 9) {
					HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
					this.result = new RemoteAccessException();
				}
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
			}
		};

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureDifferentDays.getState());

		numberProcess = 0;
		while (numberProcess++ != 12) {
			this.adventureDifferentDays.process();

			if (numberProcess == 1)
				Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureDifferentDays.getState());
			else if (numberProcess < 12)
				Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventureDifferentDays.getState());
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testPaymentActivityRoomConfirmedRemoteExceptionPaymentRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				while (numberProcess++ != 2) {
					BankInterface.processPayment(IBAN, AMOUNT);
					this.result = new RemoteAccessException();
				}
				BankInterface.processPayment(IBAN, AMOUNT);

				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				numberProcess = 0;
				while (numberProcess++ != 9) {
					HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
					this.result = new RemoteAccessException();
				}
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
			}
		};

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureDifferentDays.getState());

		numberProcess = 0;
		while (numberProcess++ != 14) {
			this.adventureDifferentDays.process();

			if (numberProcess < 3)
				Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureDifferentDays.getState());
			else if (numberProcess == 3)
				Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureDifferentDays.getState());
			else if (numberProcess < 14)
				Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventureDifferentDays.getState());
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testPaymentActivityRoomConfirmedRemoteExceptionActivityRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				while (numberProcess++ != 4) {
					ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);
					this.result = new RemoteAccessException();
				}
				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				numberProcess = 0;
				while (numberProcess++ != 9) {
					HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
					this.result = new RemoteAccessException();
				}
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
			}
		};

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureDifferentDays.getState());

		numberProcess = 0;
		while (numberProcess++ != 16) {
			this.adventureDifferentDays.process();

			if (numberProcess < 6)
				Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureDifferentDays.getState());
			else if (numberProcess < 16)
				Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventureDifferentDays.getState());
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureDifferentDays.getState());
	}

	@Test
	public void testPaymentActivityRoomConfirmedRemoteExceptionPaymentActivityRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				while (numberProcess++ != 2) {
					BankInterface.processPayment(IBAN, AMOUNT);
					this.result = new RemoteAccessException();
				}
				BankInterface.processPayment(IBAN, AMOUNT);

				numberProcess = 0;
				while (numberProcess++ != 4) {
					ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);
					this.result = new RemoteAccessException();
				}
				ActivityInterface.reserveActivity(begin, end.plusDays(1), AGE);

				numberProcess = 0;
				while (numberProcess++ != 9) {
					HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
					this.result = new RemoteAccessException();
				}
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end.plusDays(1));
			}
		};

		Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureDifferentDays.getState());

		numberProcess = 0;
		while (numberProcess++ != 18) {
			this.adventureDifferentDays.process();

			if (numberProcess < 3)
				Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventureDifferentDays.getState());
			else if (numberProcess < 8)
				Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventureDifferentDays.getState());
			else if (numberProcess < 18)
				Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventureDifferentDays.getState());
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventureDifferentDays.getState());
	}
}