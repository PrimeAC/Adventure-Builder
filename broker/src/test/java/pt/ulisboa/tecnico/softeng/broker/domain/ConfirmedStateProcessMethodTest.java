package pt.ulisboa.tecnico.softeng.broker.domain;

import mockit.*;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class ConfirmedStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(Adventure.State.CONFIRMED);

	}

	@Test
	public void success(@Mocked final BankInterface bankInterface,
	                    @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());

		new Verifications() {
			{
				BankInterface.getOperationData(this.anyString);
				this.times = 1;

				ActivityInterface.getActivityReservationData(this.anyString);
				this.times = 1;

				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 1;
			}
		};
	}

	@Test
	public void confirmedBankException(@Mocked final BankInterface bankInterface,
	                                   @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());

	}


	@Test
	public void confirmedBankLess5RemoteErrors(@Mocked final BankInterface bankInterface,
	                                           @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		int i = 0;
		while (i < 4) {
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void confirmedBank4BankExceptions(@Mocked final BankInterface bankInterface,
	                                         @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				this.times = 4;

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.times = 1;
			}
		};

		int i = 0;
		while (i < 5) {
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void confirmedBank5BankExceptions(@Mocked final BankInterface bankInterface,
	                                       @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		int i = 0;
		while (i < 5) {
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void confirmedBankRemoteAccessException(@Mocked final BankInterface bankInterface,
	                                                @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());

	}

	@Test
	public void confirmedBankLess20RemoteErrors(@Mocked final BankInterface bankInterface,
	                                        @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		int i = 0;
		while (i < 19) {
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void confirmedBank19RemoteErrors(@Mocked final BankInterface bankInterface,
	                                              @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = 19;

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.times = 1;
			}
		};

		int i = 0;
		while (i < 20) {
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void confirmedBank20RemoteErrors(@Mocked final BankInterface bankInterface,
	                                        @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		int i = 0;
		while (i < 20) {
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void confirmedActivityException(@Mocked final BankInterface bankInterface,
	                                       @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);

		new StrictExpectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void confirmedActivityRemoteAccessException(@Mocked final BankInterface bankInterface,
	                                                 @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);

		new StrictExpectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());

	}

	@Test
	public void confirmedActivityLess20RemoteErrors(@Mocked final BankInterface bankInterface,
	                                            @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);

		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		int i = 0;
		while (i < 19) {
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void confirmedActivity19RemoteErrors(@Mocked final BankInterface bankInterface,
	                                              @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);

		new StrictExpectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times=19;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.times = 1;
			}
		};

		int i = 0;
		while (i < 20) {
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void confirmedActivity20RemoteErrors(@Mocked final BankInterface bankInterface,
	                                            @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);

		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		int i = 0;
		while (i < 20) {
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void confirmedHotelException(@Mocked final BankInterface bankInterface,
	                                    @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);

		new StrictExpectations() {
			{
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new HotelException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void confirmedHotelRemoteAccessException(@Mocked final BankInterface bankInterface,
	                                                @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);

		new StrictExpectations() {
			{
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());

	}

	@Test
	public void confirmedHotelLess20RemoteErrors(@Mocked final BankInterface bankInterface,
	                                         @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);

		new Expectations() {
			{
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		int i = 0;
		while (i < 19) {
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void confirmedHotel19RemoteErrors(@Mocked final BankInterface bankInterface,
	                                            @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);

		new StrictExpectations() {
			{
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = 19;

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RoomBookingData();
				this.times = 1;
			}
		};

		int i = 0;
		while (i < 20) {
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void confirmedHotel20RemoteErrors(@Mocked final BankInterface bankInterface,
	                                         @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);

		new Expectations() {
			{
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		int i = 0;
		while (i < 20) {
			this.adventure.process();
			i++;
		}

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void confirmedNoRoomConfirmation(@Mocked final BankInterface bankInterface,
	                                     @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());

		new Verifications(){
			{
				BankInterface.getOperationData(this.anyString);
				this.times = 1;

				ActivityInterface.getActivityReservationData(this.anyString);
				this.times = 1;

				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
			}
		};
	}

}
