package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.util.Set;

public class RoomPersistenceTest {
	private static final String HOTEL_CODE = "XPTO123";
	private static final String HOTEL_NAME = "Londres";
	private static final String ROOM_NUMBER = "11";
	private static final Room.Type ROOM_TYPE = Room.Type.SINGLE;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = Atomic.TxMode.WRITE)
	public void atomicProcess() {
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME);
		new Room(hotel, ROOM_NUMBER, ROOM_TYPE);
		new Room(hotel, ROOM_NUMBER + "1", ROOM_TYPE);
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public void atomicAssert() {

		Set<Hotel> hotelSet = FenixFramework.getDomainRoot().getHotelSet();

		Hotel hotel = hotelSet.iterator().next();
		Assert.assertEquals(hotel.getNumberOfRooms(), 2);

		Room room = hotel.getRoomSet().iterator().next().getRoomByNumber(ROOM_NUMBER);
		Assert.assertEquals(room.getNumber(), ROOM_NUMBER);
		Assert.assertEquals(room.getType(), ROOM_TYPE);
	}

	@After
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void tearDown() {
		FenixFramework.getDomainRoot().getHotelSet().forEach(Hotel::delete);
	}
}
