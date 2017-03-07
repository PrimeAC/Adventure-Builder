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
			throw new ActivityException();
	}

	private void checkArguments(ActivityProvider provider, ActivityOffer offer) {
		if (provider == null || offer == null)
			throw new ActivityException();
	}

	public String getReference() {
		return this.reference;
	}
}
