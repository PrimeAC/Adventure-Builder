package pt.ulisboa.tecnico.softeng.broker.domain;

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

		assertEquals(number, this.bulkRoomBooking.getNumber());
		assertEquals(begin, this.bulkRoomBooking.getArrival());
		assertEquals(end, this.bulkRoomBooking.getDeparture());
		assertEquals(0, this.bulkRoomBooking.getNumberOfHotelExceptions());
		assertEquals(0, this.bulkRoomBooking.getNumberOfRemoteErrors());
		assertEquals(2, this.bulkRoomBooking.getReferences().size());

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

		assertEquals(2, this.bulkRoomBooking.getNumberOfHotelExceptions());
		assertEquals(0, this.bulkRoomBooking.getNumberOfRemoteErrors());
		assertFalse(this.bulkRoomBooking.getCancelled());

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

		assertEquals(3, this.bulkRoomBooking.getNumberOfHotelExceptions());
		assertEquals(0, this.bulkRoomBooking.getNumberOfRemoteErrors());
		assertEquals(true, this.bulkRoomBooking.getCancelled());

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

		assertEquals(0, this.bulkRoomBooking.getNumberOfHotelExceptions());
		assertEquals(19, this.bulkRoomBooking.getNumberOfRemoteErrors());
		assertFalse(this.bulkRoomBooking.getCancelled());

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

		assertEquals(0, this.bulkRoomBooking.getNumberOfHotelExceptions());
		assertEquals(20, this.bulkRoomBooking.getNumberOfRemoteErrors());
		assertEquals(true, this.bulkRoomBooking.getCancelled());

	}

	@Test
	public void cancelledTrue() {

		bulkRoomBooking.setCancelled();

		bulkRoomBooking.processBooking();
	}

}
