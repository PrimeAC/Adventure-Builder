package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class Booking extends Booking_Base {
	private static int counter = 0;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		checkArguments(provider, offer);
		setReference(provider.getCode() + Integer.toString(++Booking.counter));
		setCancel(null);
		setCancellationDate(null);
		setActivityOffer(offer);
	}

	private void checkArguments(ActivityProvider provider, ActivityOffer offer) {
		if (provider == null || offer == null) {
			throw new ActivityException();
		}
		if (offer.getNumberOfBookings() == offer.getCapacity())
			throw new ActivityException();
	}

	public void delete() {
		setActivityOffer(null);
		deleteDomainObject();
	}

	public String cancel() {
		setCancel("CANCEL" + getReference());
		setCancellationDate(new LocalDate());
		return getCancel();
	}

	public String getCancellation() {
		return getCancel();
	}

	public boolean isCancelled() {
		return getCancel() != null;
	}
}
