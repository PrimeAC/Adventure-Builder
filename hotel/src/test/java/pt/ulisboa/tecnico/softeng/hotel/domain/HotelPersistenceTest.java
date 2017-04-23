package pt.ulisboa.tecnico.softeng.hotel.domain;


import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import static org.junit.Assert.*;

public class HotelPersistenceTest {

	private static final String HOTEL_CODE1 = "XPTO123";
	private static final String HOTEL_NAME1 = "Londres";
	private static final String HOTEL_CODE2 = "XPTO456";
	private static final String HOTEL_NAME2 = "Paris";

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = Atomic.TxMode.WRITE)
	public void atomicProcess() {
		new Hotel(HOTEL_CODE1, HOTEL_NAME1);
		new Hotel(HOTEL_CODE2, HOTEL_NAME2);
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public void atomicAssert() {
		Hotel hotel1 = Hotel.getHotelByCode(HOTEL_CODE1);
		Hotel hotel2 = Hotel.getHotelByCode(HOTEL_CODE2);
		Hotel hotelNull = Hotel.getHotelByCode("XPTO000");

		assertNotNull(hotel1);
		assertNotNull(hotel2);
		assertNull(hotelNull);

		assertEquals(2, FenixFramework.getDomainRoot().getHotelSet().size());
		assertEquals(HOTEL_NAME1, hotel1.getName());
		assertEquals(HOTEL_NAME2, hotel2.getName());

	}

	@After
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}
}
