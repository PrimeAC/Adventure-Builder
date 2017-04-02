package pt.ulisboa.tecnico.softeng.broker.domain;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BulkRoomBookingProcessBookingMethodTest {
	private int number = 2;
	private final Set<String> references = new HashSet<>();

	private final LocalDate begin = LocalDate.now();
	private final LocalDate end = begin.plusDays(3);
	private BulkRoomBooking bulkRoomBooking;


	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.bulkRoomBooking = new BulkRoomBooking(2,this.begin, this.end);
		this.references.add("hello");
		this.references.add("world");
	}

	@Test
	public void successfulBulkBooking(@Mocked HotelInterface hotelInterface) {
		new Expectations() {
			{
				HotelInterface.bulkBooking(number, begin, end);
				result =references;
				times = 1;
			}
		};

		bulkRoomBooking.processBooking();

		int remote_errors = Deencapsulation.getField(bulkRoomBooking, "numberOfRemoteErrors");
		int hotel_errors = Deencapsulation.getField(bulkRoomBooking, "numberOfHotelExceptions");
		boolean cancelled = Deencapsulation.getField(bulkRoomBooking, "cancelled");

		assertEquals(number, this.bulkRoomBooking.getNumber());
		assertEquals(begin, this.bulkRoomBooking.getArrival());
		assertEquals(end, this.bulkRoomBooking.getDeparture());
		assertEquals(0, hotel_errors);
		assertEquals(0, remote_errors);
		assertEquals(2, this.bulkRoomBooking.getReferences().size());
		assertFalse(cancelled);

	}

	@Test
	public void hotelExceptionBulkBooking(@Mocked HotelInterface hotelInterface) {
		new Expectations() {
			{
				HotelInterface.bulkBooking(number, begin, end);
				result = new HotelException();
				times = 2;
			}
		};

		for(int i = 0; i <2; i++){
			bulkRoomBooking.processBooking();
		}

		int remote_errors = Deencapsulation.getField(bulkRoomBooking, "numberOfRemoteErrors");
		int hotel_errors = Deencapsulation.getField(bulkRoomBooking, "numberOfHotelExceptions");
		boolean cancelled = Deencapsulation.getField(bulkRoomBooking, "cancelled");

		assertEquals(2, hotel_errors);
		assertEquals(0, remote_errors);
		assertFalse(cancelled);

	}

	@Test
	public void hotelExceptionCancelBulkBooking(@Mocked HotelInterface hotelInterface) {
		new Expectations() {
			{
				HotelInterface.bulkBooking(number, begin, end);
				result = new HotelException();
				times = 3;
			}
		};

		for(int i = 0; i <3; i++){
			bulkRoomBooking.processBooking();
		}


		int remote_errors = Deencapsulation.getField(bulkRoomBooking, "numberOfRemoteErrors");
		int hotel_errors = Deencapsulation.getField(bulkRoomBooking, "numberOfHotelExceptions");
		boolean cancelled = Deencapsulation.getField(bulkRoomBooking, "cancelled");

		assertEquals(3, hotel_errors);
		assertEquals(0, remote_errors);
		assertTrue(cancelled);

	}

	@Test
	public void remoteAccessExceptionBulkBooking(@Mocked HotelInterface hotelInterface) {
		new Expectations() {{
			HotelInterface.bulkBooking(number, begin, end);
			result = new RemoteAccessException();
			times = 19;
		}};

		for(int i = 0; i <19; i++){
			bulkRoomBooking.processBooking();
		}


		int remote_errors = Deencapsulation.getField(bulkRoomBooking, "numberOfRemoteErrors");
		int hotel_errors = Deencapsulation.getField(bulkRoomBooking, "numberOfHotelExceptions");
		boolean cancelled = Deencapsulation.getField(bulkRoomBooking, "cancelled");

		assertEquals(0, hotel_errors);
		assertEquals(19, remote_errors);
		assertFalse(cancelled);

	}

	@Test
	public void remoteAccessExceptionCancelBulkBooking(@Mocked HotelInterface hotelInterface) {
		new Expectations() {
			{
			HotelInterface.bulkBooking(number, begin, end);
			result = new RemoteAccessException();
			times = 20;
			}
		};

		for(int i = 0; i <20; i++){
			bulkRoomBooking.processBooking();
		}


		int remote_errors = Deencapsulation.getField(bulkRoomBooking, "numberOfRemoteErrors");
		int hotel_errors = Deencapsulation.getField(bulkRoomBooking, "numberOfHotelExceptions");
		boolean cancelled = Deencapsulation.getField(bulkRoomBooking, "cancelled");

		assertEquals(0, hotel_errors);
		assertEquals(20, remote_errors);
		assertTrue(cancelled);

	}

	@Test
	public void cancelledTrue() {

		Deencapsulation.setField( bulkRoomBooking, "cancelled", true);
		Deencapsulation.setField( bulkRoomBooking, "numberOfHotelExceptions", 2);
		int hotel_errors = Deencapsulation.getField(bulkRoomBooking, "numberOfHotelExceptions");
		int remote_errors = Deencapsulation.getField(bulkRoomBooking, "numberOfRemoteErrors");

		assertEquals(2,hotel_errors);
		assertEquals(0, remote_errors);

		bulkRoomBooking.processBooking();


		assertEquals(2,hotel_errors);
		assertEquals(0, remote_errors);
	}

}
