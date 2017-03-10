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
		checkDates(activity, begin, end);

		this.begin = begin;
		this.end = end;
		this.capacity = activity.getCapacity();

		activity.addOffer(this);
	}

	void checkArguments(Activity activity, LocalDate begin, LocalDate end){
		if (activity == null)
			throw new ActivityException("Activity argument is null");
		else if (begin == null)
			throw new ActivityException("Begin argument is null");
		else if (end == null)
			throw new ActivityException("End argument is null");
	}

	void checkDates(Activity activity, LocalDate begin, LocalDate end) {
		if(begin.isBefore(LocalDate.now()))
			throw new ActivityException("Begin date has to be equal or after today");
		else if(end.isBefore(begin))
			throw new ActivityException("End date can't be before begin date");
		else if(activity.hasOffers(begin, end)) {
			throw new ActivityException("Activity has offers that conflict with begin and end dates");
		}
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
		return !this.begin.isBefore(begin) && !this.end.isAfter(end);
	}

	boolean matchDateConflict(LocalDate begin, LocalDate end) {
		return !begin.isAfter(this.end) && !end.isBefore(this.begin);
	}

	boolean hasVacancy() {
		return this.capacity > getNumberOfBookings();
	}

}
