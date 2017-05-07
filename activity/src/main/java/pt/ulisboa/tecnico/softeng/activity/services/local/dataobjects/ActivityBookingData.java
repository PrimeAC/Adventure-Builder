package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;

public class ActivityBookingData {
	private String reference;
	private String cancel;
	private LocalDate cancellationDate;

	public ActivityBookingData() {
	}

	public ActivityBookingData(Booking booking) {
		reference = booking.getReference();
		cancel = booking.getCancel();
		cancellationDate = booking.getCancellationDate();
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCancel() {
		return cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = cancel;
	}

	public LocalDate getCancellationDate() {
		return cancellationDate;
	}

	public void setCancellationDate(LocalDate cancellationDate) {
		this.cancellationDate = cancellationDate;
	}
}
