package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

import java.util.HashSet;
import java.util.Set;

public class ActivityOffer extends ActivityOffer_Base{

	public ActivityOffer(Activity activity, LocalDate begin, LocalDate end) {
		checkArguments(activity, begin, end);

		setBegin(begin);
		setEnd(end);
		setCapacity(activity.getCapacity());
		setActivity(activity);
	}

	private void checkArguments(Activity activity, LocalDate begin, LocalDate end) {
		if (activity == null || begin == null || end == null) {
			throw new ActivityException();
		}

		if (end.isBefore(begin)) {
			throw new ActivityException();
		}
	}

	public void delete() {
		getBookingSet().forEach(Booking::delete);
		setActivity(null);
		deleteDomainObject();
	}

	int getNumberOfBookings() {
		int count = 0;
		for (Booking booking : getBookingSet()) {
			if (!booking.isCancelled()) {
				count++;
			}
		}
		return count;
	}
	
	@Override
	public void addBooking(Booking booking) {
		if (getCapacity() == getNumberOfBookings()) {
			throw new ActivityException();
		}
		super.addBooking(booking);
	}

	boolean available(LocalDate begin, LocalDate end) {
		return hasVacancy() && matchDate(begin, end);
	}

	boolean matchDate(LocalDate begin, LocalDate end) {
		if (begin == null || end == null) {
			throw new ActivityException();
		}

		return begin.equals(getBegin()) && end.equals(getEnd());
	}

	boolean hasVacancy() {
		return getCapacity() > getNumberOfBookings();
	}

	public Booking getBooking(String reference) {
		for (Booking booking : getBookingSet()) {
			if (booking.getReference().equals(reference)
					|| (booking.isCancelled() && booking.getCancel().equals(reference))) {
				return booking;
			}
		}
		return null;
	}

}
