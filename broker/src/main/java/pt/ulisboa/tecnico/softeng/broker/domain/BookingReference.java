package pt.ulisboa.tecnico.softeng.broker.domain;

public class BookingReference extends BookingReference_Base {

	public BookingReference(String reference) {
		setReference(reference);
	}

	public void delete() {
		setBulkRoomBooking(null);
		super.deleteDomainObject();
	}
}
