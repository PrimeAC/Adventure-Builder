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
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
		new Expectations() {{
			HotelInterface.bulkBooking(number, begin, end);
			result =references;
		}};

		bulkRoomBooking.processBooking();

		assertEquals(0, this.bulkRoomBooking.getNumberOfHotelExceptions());
		assertEquals(0, this.bulkRoomBooking.getNumberOfRemoteErrors());
		assertEquals(2, this.bulkRoomBooking.getReferences().size());


	}

	@Test
	public void hotelExceptionBulkBooking(@Mocked HotelInterface hotelInterface) {
		new Expectations() {{
			HotelInterface.bulkBooking(number, begin, end);
			result = new HotelException();
		}};

		bulkRoomBooking.processBooking();

		assertEquals(1, this.bulkRoomBooking.getNumberOfHotelExceptions());
		assertEquals(0, this.bulkRoomBooking.getNumberOfRemoteErrors());

	}

	@Test
	public void hotelExceptionCancelBulkBooking(@Mocked HotelInterface hotelInterface) {
		new Expectations() {{
			HotelInterface.bulkBooking(number, begin, end);
			result = new HotelException();
		}};
		for(int i = 0; i <3; i++){
			bulkRoomBooking.processBooking();
		}

		assertEquals(3, this.bulkRoomBooking.getNumberOfHotelExceptions());
		assertEquals(0, this.bulkRoomBooking.getNumberOfRemoteErrors());
		assertEquals(true, this.bulkRoomBooking.getCancelled());

		new Verifications() {
			{
				HotelInterface.bulkBooking(number, begin, end);
				times = 3;
			}
		};

	}

	@Test
	public void RemoteAccessExceptionBulkBooking(@Mocked HotelInterface hotelInterface) {
		new Expectations() {{
			HotelInterface.bulkBooking(number, begin, end);
			result = new RemoteAccessException();
		}};

		bulkRoomBooking.processBooking();

		assertEquals(0, this.bulkRoomBooking.getNumberOfHotelExceptions());
		assertEquals(1, this.bulkRoomBooking.getNumberOfRemoteErrors());

	}

	@Test
	public void RemoteAccessExceptionCancelBulkBooking(@Mocked HotelInterface hotelInterface) {
		new Expectations() {
			{
			HotelInterface.bulkBooking(number, begin, end);
			result = new RemoteAccessException();
			}
		};
		for(int i = 0; i <20; i++){
			bulkRoomBooking.processBooking();
		}

		assertEquals(0, this.bulkRoomBooking.getNumberOfHotelExceptions());
		assertEquals(20, this.bulkRoomBooking.getNumberOfRemoteErrors());
		assertEquals(true, this.bulkRoomBooking.getCancelled());

		new Verifications() {
			{
				HotelInterface.bulkBooking(number, begin, end);
				times = 20;
			}
		};

	}


}
