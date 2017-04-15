package pt.ulisboa.tecnico.softeng.hotel.domain;


import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import static org.junit.Assert.assertEquals;

public class HotelPersistenceTest {

	private static final String HOTEL_CODE = "XPTO123";
	private static final String HOTEL_NAME = "Londres";

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = Atomic.TxMode.WRITE)
	public void atomicProcess() {
		new Hotel(HOTEL_CODE, HOTEL_NAME);
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public void atomicAssert() {
		Hotel hotel = Hotel.getHotelByCode(HOTEL_CODE);

		assertEquals(HOTEL_NAME, hotel.getName());
	}

	@After
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}
}
