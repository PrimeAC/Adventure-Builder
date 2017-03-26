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
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(JMockit.class)
public class BookRoomStateProcessMethodTest {

	private static final String IBAN = "BK01987654321";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ROOM_CONFIRMATION = "resultRoomConfirmation";

	private final LocalDate begin = LocalDate.now();
	private final LocalDate end = begin.plusDays(3);
	private Adventure adventure;


	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setState(Adventure.State.BOOK_ROOM);
	}

	@Test
	public void successfulBooking(@Mocked HotelInterface hotelInterface) {

		new Expectations() {{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			result = ROOM_CONFIRMATION;
		}};

		adventure.process();

		assertEquals("Unexpected state", Adventure.State.CONFIRMED, adventure.getState());
		assertEquals("Unexpected room confirmation", ROOM_CONFIRMATION, adventure.getRoomConfirmation());

		new Verifications() {{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			times = 1;
		}};
	}

	@Test
	public void hotelException(@Mocked HotelInterface hotelInterface) {

		new Expectations() {{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			result = new HotelException();
		}};

		adventure.process();

		assertEquals("Unexpected state", Adventure.State.UNDO, adventure.getState());
		assertNull(adventure.getRoomConfirmation());

		new Verifications() {{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			times = 1;
		}};
	}

	@Test
	public void remoteAccessException(@Mocked HotelInterface hotelInterface) {
		new Expectations() {{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			result = new RemoteAccessException();
		}};

		int processCount = processUntilStateChanges(adventure);

		assertEquals(10, processCount);
		assertEquals("Unexpected state", Adventure.State.UNDO, adventure.getState());
		assertNull(adventure.getRoomConfirmation());

		new Verifications() {{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			times = 10;
		}};
	}

	@Test
	public void remoteExceptionBeforeSuccess(@Mocked HotelInterface hotelInterface) {

		new Expectations() {{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			result = Collections.nCopies(9, new RemoteAccessException());
			result = ROOM_CONFIRMATION;
		}};
		
		int processCount = processUntilStateChanges(adventure);

		assertEquals(10, processCount);
		assertEquals("Unexpected state", Adventure.State.CONFIRMED, adventure.getState());
		assertEquals("Unexpected room confirmation", ROOM_CONFIRMATION, adventure.getRoomConfirmation());

		new Verifications() {{
			HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			times = 10;
		}};
	}

	public int processUntilStateChanges(Adventure adventure) {
		Adventure.State initialState = adventure.getState();
		int count = 0;

		while (adventure.getState() == initialState) {
			adventure.process();
			count++;
		}
		return count;
	}

}
