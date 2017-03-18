package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelConstructorTest {

	@Before
	public void setUp() {

	}

	@Test
	public void success() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		Assert.assertEquals("Londres", hotel.getName());
		Assert.assertTrue(hotel.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());
	}

	@Test (expected = HotelException.class)
	public void checkExistingCode() {
		Hotel hotel1 = new Hotel("testcod", "Lisboa");
		Hotel hotel2 = new Hotel("testcod", "Porto");
	}

	@Test (expected = HotelException.class)
	public void checkLargerCode() {
		Hotel hotel = new Hotel("12345678", "Lisboa");
	}

	@Test (expected = HotelException.class)
	public void checkSmallerCode() {
		Hotel hotel = new Hotel("123456", "Lisboa");
	}

	@Test (expected = HotelException.class)
	public void checkNullCode() {
		Hotel hotel = new Hotel(null, "Londres");
	}

	@Test (expected = HotelException.class)
	public void checkNullName() {
		Hotel hotel = new Hotel("XPTO123", null);
	}

	@Test (expected = HotelException.class)
	public void checkEmptyCode() {
		Hotel hotel = new Hotel("", "Londres");
	}

	@Test (expected = HotelException.class)
	public void checkEmptyName() {
		Hotel hotel = new Hotel("XPTO123", "");
	}

	@Test (expected = HotelException.class)
	public void checkCode7Spaces() {
		Hotel hotel = new Hotel("       ", "Londres");
	}

	@Test (expected = HotelException.class)
	public void checkNewLineCode() {
		Hotel hotel = new Hotel("\n", "Londres");
	}

	@Test (expected = HotelException.class)
	public void checkLineFeedCode() {
		Hotel hotel = new Hotel("\r", "Londres");
	}

	@Test (expected = HotelException.class)
	public void checkTabCode() {
		Hotel hotel = new Hotel("\t", "Londres");
	}

	@Test (expected = HotelException.class)
	public void checkNewLineName() {
		Hotel hotel = new Hotel("XPTO123", "\n");
	}

	@Test (expected = HotelException.class)
	public void checkLineFeedName() {
		Hotel hotel = new Hotel("XPTO123", "\r");
	}

	@Test (expected = HotelException.class)
	public void checkTabName() {
		Hotel hotel = new Hotel("XPTO123", "\t");
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
