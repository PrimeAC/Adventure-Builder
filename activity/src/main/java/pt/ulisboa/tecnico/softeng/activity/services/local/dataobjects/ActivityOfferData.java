package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;

import java.util.ArrayList;
import java.util.List;

public class ActivityOfferData {
	public enum CopyDepth {
		SHALLOW, BOOKINGS
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate begin, end;
	private int capacity;
	private List<ActivityBookingData> bookings = new ArrayList<>();

	public ActivityOfferData() {
	}

	public ActivityOfferData(ActivityOffer offer, CopyDepth depth) {
		begin = offer.getBegin();
		end = offer.getEnd();
		capacity = offer.getCapacity();

		switch (depth) {
			case BOOKINGS:
				offer.getBookingSet().forEach(booking -> bookings.add(new ActivityBookingData(booking)));
				break;
			case SHALLOW:
			default:
				break;
		}
	}

	public LocalDate getBegin() {
		return begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public List<ActivityBookingData> getBookings() {
		return bookings;
	}

	public void setBookings(List<ActivityBookingData> bookings) {
		this.bookings = bookings;
	}
}
