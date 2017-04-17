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
	private static final String HOTEL_CODE3 = "XPTO789";
	private static final String HOTEL_NAME3 = "New York";

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Test
	public void test2Hotels(){
		atomicProcess2();
		atomicAssert2();
	}

	@Test
	public void testDeletionOfHotel(){
		atomicProcess3();
		atomicAssert3();
	}

	@Atomic(mode = Atomic.TxMode.WRITE)
	public void atomicProcess() {
		new Hotel(HOTEL_CODE1, HOTEL_NAME1);
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public void atomicAssert() {
		Hotel hotel = Hotel.getHotelByCode(HOTEL_CODE1);

		assertNotNull(hotel);
		assertEquals(1, FenixFramework.getDomainRoot().getHotelSet().size());
		assertEquals(HOTEL_NAME1, hotel.getName());
	}

	@Atomic(mode = Atomic.TxMode.WRITE)
	public void atomicProcess2() {
		new Hotel(HOTEL_CODE1,HOTEL_NAME1 );
		new Hotel(HOTEL_CODE2,HOTEL_NAME2);
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public void atomicAssert2() {
		Hotel hotel1 = Hotel.getHotelByCode(HOTEL_CODE1);
		Hotel hotel2 = Hotel.getHotelByCode(HOTEL_CODE2);

		assertEquals(2, FenixFramework.getDomainRoot().getHotelSet().size());
		assertEquals(HOTEL_NAME1, hotel1.getName());
		assertEquals(HOTEL_NAME2, hotel2.getName());

	}

	@Atomic(mode = Atomic.TxMode.WRITE)
	public void atomicProcess3() {
		new Hotel(HOTEL_CODE1,HOTEL_NAME1);
		Hotel hotel = new Hotel(HOTEL_CODE2,HOTEL_NAME2);
		new Hotel(HOTEL_CODE3, HOTEL_NAME3);

		hotel.delete();
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public void atomicAssert3() {
		Hotel hotel1 = Hotel.getHotelByCode(HOTEL_CODE1);
		Hotel nullHotel = Hotel.getHotelByCode(HOTEL_CODE2);
		Hotel hotel2 = Hotel.getHotelByCode(HOTEL_CODE3);

		assertNull(nullHotel);
		assertEquals(2, FenixFramework.getDomainRoot().getHotelSet().size());
		assertEquals(HOTEL_NAME1, hotel1.getName());
		assertEquals(HOTEL_NAME3, hotel2.getName());

	}

	@After
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}
}
