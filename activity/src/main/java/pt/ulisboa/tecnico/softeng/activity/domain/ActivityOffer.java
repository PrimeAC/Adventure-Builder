package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityOffer {
	private final LocalDate begin;
	private final LocalDate end;
	private final int capacity;
	private final Set<Booking> bookings = new HashSet<>();

	public ActivityOffer(Activity activity, LocalDate begin, LocalDate end) {
		checkArguments(activity, begin, end);
		if(begin.isBefore(LocalDate.now()) ||
				end.isBefore(begin) ||
				activity.hasOffers(begin, end)) {
			throw new ActivityException("Can't create ActivityOffer with those dates as values");
		}

		this.begin = begin;
		this.end = end;
		this.capacity = activity.getCapacity();

		activity.addOffer(this);
	}

	void checkArguments(Activity activity, LocalDate begin, LocalDate end){
		if(activity == null || begin == null || end == null)
			throw new ActivityException("Invalid ActivityOffer arguments");
	}

	LocalDate getBegin() {
		return this.begin;
	}

	LocalDate getEnd() {
		return this.end;
	}

	int getNumberOfBookings() {
		return this.bookings.size();
	}

	void addBooking(Booking booking) {
		this.bookings.add(booking);

	}

	boolean available(LocalDate begin, LocalDate end) {
		return hasVacancy() && matchDate(begin, end);
	}

	boolean matchDate(LocalDate begin, LocalDate end) {
		return !begin.isAfter(this.end) && !end.isBefore(this.begin);
	}

	boolean hasVacancy() {
		return this.capacity > getNumberOfBookings();
	}

}
