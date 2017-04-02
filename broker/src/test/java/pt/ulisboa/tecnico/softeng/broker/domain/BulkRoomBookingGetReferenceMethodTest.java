package pt.ulisboa.tecnico.softeng.broker.domain;

import mockit.*;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(JMockit.class)
public class BulkRoomBookingGetReferenceMethodTest {

	private final LocalDate arrival = LocalDate.now();
	private final LocalDate departure= arrival.plusDays(3);

	private final Room.Type DOUBLE = Room.Type.DOUBLE;

	private Set<String> oneRef;
	private Set<String> twoRefs;
	private Set<String> emptyRefs;

	@Before
	public void setUp() {
		this.twoRefs = new HashSet<String>(Arrays.asList("1", "2"));
		this.emptyRefs = new HashSet<String>();
	}

	public static void setParams(BulkRoomBooking brb,  Set<String> refs, int numberHotelException, int numberRemoteError, boolean cancelled) {
		Deencapsulation.setField(brb, "references", refs);
		Deencapsulation.setField(brb, "numberOfHotelExceptions", numberRemoteError);
		Deencapsulation.setField(brb, "numberOfRemoteErrors", numberHotelException);
		Deencapsulation.setField(brb, "cancelled", cancelled);
	}

	@Test
	public void successSingleReference(@Mocked HotelInterface hotelInterface, @Mocked RoomBookingData data) {

		new Expectations() {{
			HotelInterface.getRoomBookingData(anyString);
			result = data;
			times = 1;

			data.getRoomType();
			result = DOUBLE;
			times = 1;
		}};
		String inRef = "1";
		Set<String> refs = new HashSet<String>(Arrays.asList(inRef));

		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(1, this.arrival, this.departure);
		BulkRoomBookingGetReferenceMethodTest.setParams(bulkRoomBooking, refs, 0, 0, false);
		String outRef = bulkRoomBooking.getReference(DOUBLE);

		assertEquals("1", outRef);
		assertTrue(bulkRoomBooking.getReferences().isEmpty());

	}

	@Test
	public void successMultipleReferences(@Mocked HotelInterface hotelInterface, @Mocked RoomBookingData data) {

		String inRef1 = "1";
		String inRef2 = "2";
		Set<String> inRefs = new HashSet<String>(Arrays.asList(inRef1, inRef2));

		new Expectations() {{
			hotelInterface.getRoomBookingData(inRef1);
			result = data;
			times = 1;

			hotelInterface.getRoomBookingData(inRef2);
			result = null;
			minTimes = 0;

			data.getRoomType();
			result = DOUBLE;
			times = 1;
		}};

		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(2, this.arrival, this.departure);
		BulkRoomBookingGetReferenceMethodTest.setParams(bulkRoomBooking, inRefs, 0, 0, false);
		String outRef = bulkRoomBooking.getReference(DOUBLE);

		Set<String> outRefs = bulkRoomBooking.getReferences();
		assertEquals(inRef1, outRef);
		assertFalse(outRefs.contains(outRef));
		assertTrue(outRefs.contains(inRef2));
		assertEquals(1, outRefs.size());

	}

	@Test
	public void noReferences(@Mocked HotelInterface hotelInterface, @Mocked RoomBookingData data) {

		new Verifications() {{
			HotelInterface.getRoomBookingData(anyString);
			times = 0;

			data.getRoomType();
			times = 0;
		}};

		Set<String> refs = new HashSet<String>();

		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(1, this.arrival, this.departure);
		BulkRoomBookingGetReferenceMethodTest.setParams(bulkRoomBooking, refs, 0, 0, false);
		String outRef = bulkRoomBooking.getReference(DOUBLE);

		assertEquals(null, outRef);
		assertTrue(bulkRoomBooking.getReferences().isEmpty());
	}

	@Test
	public void cancelledState(@Mocked HotelInterface hotelInterface, @Mocked RoomBookingData data) {

		new Expectations() {{
			HotelInterface.getRoomBookingData(anyString);
			times = 0;

			data.getRoomType();
			times = 0;
		}};

		String inRef = "1";
		Set<String> refs = new HashSet<String>(Arrays.asList(inRef));

		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(1, this.arrival, this.departure);
		BulkRoomBookingGetReferenceMethodTest.setParams(bulkRoomBooking, refs, 0, 0, true);
		String outRef = bulkRoomBooking.getReference(DOUBLE);

		assertEquals(null, outRef);
		assertTrue(bulkRoomBooking.getReferences().contains(inRef));

	}

	@Test
	public void succesAfterRemoteErrors(@Mocked HotelInterface hotelInterface, @Mocked RoomBookingData data) {

		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(1, this.arrival, this.departure);
		int maxErrors = Deencapsulation.getField(bulkRoomBooking, "MAX_REMOTE_ERRORS");

		Set<String> refs = new HashSet<String>();
		int i = 0;
		while (i < maxErrors+10) {
			refs.add(Integer.toString(++i));
		}

		new Expectations() {{
			HotelInterface.getRoomBookingData(anyString);
			result = data;
			times = 1;

			data.getRoomType();
			result = DOUBLE;
			times = 1;
		}};

		BulkRoomBookingGetReferenceMethodTest.setParams(bulkRoomBooking, refs, 0, maxErrors-1, false);

		String outRef = bulkRoomBooking.getReference(DOUBLE);

		assertNotNull(outRef);
		int errors = Deencapsulation.getField(bulkRoomBooking, "numberOfRemoteErrors");
		assertEquals(0, errors);

		boolean isCancelled = Deencapsulation.getField(bulkRoomBooking, "cancelled");
		assertFalse(isCancelled);

	}

	@Test
	public void tooManyRemoteErrors(@Mocked HotelInterface hotelInterface, @Mocked RoomBookingData data) {

		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(1, this.arrival, this.departure);
		int maxErrors = Deencapsulation.getField(bulkRoomBooking, "MAX_REMOTE_ERRORS");

		Set<String> refs = new HashSet<String>();
		int i = 0;
		while (i < maxErrors+10) {
			refs.add(Integer.toString(++i));
		}

		new Expectations() {{
			HotelInterface.getRoomBookingData(anyString);
			result = new RemoteAccessException();

			data.getRoomType();
			result = DOUBLE;
			times = 0;
		}};

		BulkRoomBookingGetReferenceMethodTest.setParams(bulkRoomBooking, refs, 0, maxErrors-1, false);

		String outRef = bulkRoomBooking.getReference(DOUBLE);

		assertTrue(bulkRoomBooking.getReferences().contains("1"));
		assertNull(outRef);

		int errors = Deencapsulation.getField(bulkRoomBooking, "numberOfRemoteErrors");
		assertEquals(maxErrors, errors);

		boolean isCancelled = Deencapsulation.getField(bulkRoomBooking, "cancelled");
		assertTrue(isCancelled);

	}

	@Test
	public void successAfterHotelException(@Mocked HotelInterface hotelInterface, @Mocked RoomBookingData data) {

		new Expectations() {{
			HotelInterface.getRoomBookingData(anyString);
			result = new HotelException();
			result = data;

			data.getRoomType();
			result = DOUBLE;
			times = 1;
		}};
		String inRef1 = "1";
		String inRef2 = "2";
		Set<String> refs = new HashSet<String>(Arrays.asList(inRef1, inRef2));

		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(2, this.arrival, this.departure);
		BulkRoomBookingGetReferenceMethodTest.setParams(bulkRoomBooking, refs, 0, 0, false);

		String outRef = bulkRoomBooking.getReference(DOUBLE);

		assertFalse(bulkRoomBooking.getReferences().contains(outRef));
		assertTrue(bulkRoomBooking.getReferences().contains(outRef == inRef1 ? inRef2 : inRef1));
		assertEquals(1, bulkRoomBooking.getReferences().size());

		boolean isCancelled = Deencapsulation.getField(bulkRoomBooking, "cancelled");
		assertFalse(isCancelled);

	}

	@Test
	public void successAfterHotelExceptionAfterRemoteError(@Mocked HotelInterface hotelInterface, @Mocked RoomBookingData data) {

		new Expectations() {{
			HotelInterface.getRoomBookingData(anyString);
			result = new RemoteAccessException();
			result = new HotelException();
			result = data;
			times = 3;

			data.getRoomType();
			result = DOUBLE;
			times = 1;
		}};

		String inRef1 = "1";
		String inRef2 = "2";
		String inRef3 = "3";
		String inRef4 = "4";
		Set<String> refs = new HashSet<String>(Arrays.asList(inRef1, inRef2, inRef3, inRef4));

		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(1, this.arrival, this.departure);
		BulkRoomBookingGetReferenceMethodTest.setParams(bulkRoomBooking, refs, 0, 0, false);

		String outRef = bulkRoomBooking.getReference(DOUBLE);

		assertNotNull(outRef);
		assertTrue(bulkRoomBooking.getReferences().size() == 3);
		assertFalse(bulkRoomBooking.getReferences().contains(outRef));

		int remoteErrors = Deencapsulation.getField(bulkRoomBooking, "numberOfRemoteErrors");
		assertEquals(0, remoteErrors);

		boolean isCancelled = Deencapsulation.getField(bulkRoomBooking, "cancelled");
		assertFalse(isCancelled);

	}

	@Test
	public void nullType(@Mocked HotelInterface hotelInterface, @Mocked RoomBookingData data) {

		new Expectations() {{
			HotelInterface.getRoomBookingData(anyString);
			result = data;
			times = 1;

			data.getRoomType();
			result = DOUBLE;
			times = 1;
		}};
		String inRef = "1";
		Set<String> refs = new HashSet<String>(Arrays.asList(inRef));

		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(1, this.arrival, this.departure);
		BulkRoomBookingGetReferenceMethodTest.setParams(bulkRoomBooking, refs, 0, 0, false);
		String outRef = bulkRoomBooking.getReference(null);

		assertNull(outRef);
		assertFalse(bulkRoomBooking.getReferences().isEmpty());

	}

	@Test
	public void nullOutputFromGetRoomBookingData(@Mocked HotelInterface hotelInterface, @Mocked RoomBookingData data) {

		new Expectations() {{
			HotelInterface.getRoomBookingData(anyString);
			result = null;
			times = 1;
		}};
		String inRef = "1";
		Set<String> refs = new HashSet<String>(Arrays.asList(inRef));

		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(1, this.arrival, this.departure);
		BulkRoomBookingGetReferenceMethodTest.setParams(bulkRoomBooking, refs, 0, 0, false);
		String outRef = bulkRoomBooking.getReference(DOUBLE);

		assertNull(outRef);
		assertFalse(bulkRoomBooking.getReferences().isEmpty());

	}
}
