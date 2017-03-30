package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class UndoStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final int AGE = 20;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private final LocalDate begin = LocalDate.now();
	private final LocalDate end = begin.plusDays(1);
	private Adventure adventure;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
	}

	@Test
	public void undoPaymentHotelException(@Mocked final BankInterface bankInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = this.anyString;
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);			
				this.result = new BankException();
			}
		};
		
		this.adventure.process();
		adventure.setState(State.UNDO);
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void undoPaymentRemoteAccessException(@Mocked final BankInterface bankInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = this.anyString;
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);			
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		adventure.setState(State.UNDO);
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void undoPayment(@Mocked final BankInterface bankInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = this.anyString;
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = this.anyString;
			}
		};
		
		this.adventure.process();
		adventure.setState(State.UNDO);
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void undoActivityHotelException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				ActivityInterface.reserveActivity(begin, end, AGE);
				this.result = this.anyString;
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};
		
		this.adventure.process();
		this.adventure.process();
		adventure.setState(State.UNDO);
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void undoActivityRemoteAccessException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				ActivityInterface.reserveActivity(begin, end, AGE);
				this.result = this.anyString;
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		this.adventure.process();
		adventure.setState(State.UNDO);
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());

	}

	@Test
	public void undoActivity(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				ActivityInterface.reserveActivity(begin, end, AGE);
				this.result = this.anyString;
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = this.anyString;
			}
		};
		
		this.adventure.process();
		this.adventure.process();
		adventure.setState(State.UNDO);
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void undoRoomHotelException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				ActivityInterface.reserveActivity(begin, end, AGE);
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);
				this.result = this.anyString;
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = new HotelException();
			}
		};
		
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		adventure.setState(State.UNDO);
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void undoRoomRemoteAccessException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				ActivityInterface.reserveActivity(begin, end, AGE);
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);
				this.result = this.anyString;
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		adventure.setState(State.UNDO);
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void undoRoom(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface hotelInterface) {
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				ActivityInterface.reserveActivity(begin, end, AGE);
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);
				this.result = this.anyString;
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = this.anyString;
			}
		};
		
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		adventure.setState(State.UNDO);
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
		
	}

}