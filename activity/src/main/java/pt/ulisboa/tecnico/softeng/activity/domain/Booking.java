package pt.ulisboa.tecnico.softeng.activity.domain;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class Booking {
	private static int counter = 0;

	private final String reference;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		checkArguments(provider, offer);
		checkAvailability(offer);

		this.reference = provider.getCode() + Integer.toString(++Booking.counter);

		offer.addBooking(this);
	}

	private void checkAvailability(ActivityOffer offer) {
		if (!offer.hasVacancy())
			throw new ActivityException("ActivityOffer's capability reached the limit");
	}

	private void checkArguments(ActivityProvider provider, ActivityOffer offer) {
		if (provider == null || offer == null)
			throw new ActivityException("Argument was null");
	}

	public String getReference() {
		return this.reference;
	}
}
