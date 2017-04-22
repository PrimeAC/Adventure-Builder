package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BulkRoomBooking extends BulkRoomBooking_Base {
	public static final int MAX_HOTEL_EXCEPTIONS = 3;
	public static final int MAX_REMOTE_ERRORS = 10;

	public BulkRoomBooking(int number, LocalDate arrival, LocalDate departure) {

		setNumber(number);
		setArrival(arrival);
		setDeparture(departure);
		setCancelled(false);
		setNumberOfHotelExceptions(0);
		setNumberOfRemoteErrors(0);
	}

	public void delete() {
		setBroker(null);

		super.deleteDomainObject();
	}

	public Set<String> getReferences() {
		Set<String> references = new HashSet<>();
		for (BookingReference bookingReference : this.getBookingReferenceSet()) {
			references.add(bookingReference.getReference());
		}
		return references;
	}

	public void processBooking() {
		if (getCancelled()) {
			return;
		}

		try {
			for (String reference : HotelInterface.bulkBooking(getNumber(), getArrival(), getDeparture())) {
				BookingReference bookingReference = new BookingReference(reference);
				this.addBookingReference(bookingReference);
			}

			setNumberOfHotelExceptions(0);
			setNumberOfRemoteErrors(0);
			return;
		} catch (HotelException he) {
			setNumberOfHotelExceptions(getNumberOfRemoteErrors() + 1);
			if (getNumberOfHotelExceptions() == MAX_HOTEL_EXCEPTIONS) {
				setCancelled(true);
			}
			setNumberOfRemoteErrors(0);
			return;
		} catch (RemoteAccessException rae) {
			setNumberOfRemoteErrors(getNumberOfRemoteErrors() + 1);
			if (getNumberOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				setCancelled(true);
			}
			setNumberOfHotelExceptions(0);
			return;
		}
	}

	public String getReference(String type) {
		if (getCancelled()) {
			return null;
		}
		for (BookingReference bookingReference : getBookingReferenceSet()) {
			String reference = bookingReference.getReference();
			RoomBookingData data = null;
			try {
				data = HotelInterface.getRoomBookingData(reference);
				setNumberOfRemoteErrors(0);
			} catch (HotelException he) {
				setNumberOfRemoteErrors(0);
			} catch (RemoteAccessException rae) {
				setNumberOfRemoteErrors(getNumberOfRemoteErrors() + 1);
				if (getNumberOfRemoteErrors() == MAX_REMOTE_ERRORS) {
					setCancelled(true);
				}
			}

			if (data != null && data.getRoomType().equals(type)) {
				bookingReference.delete();
				return reference;
			}
		}
		return null;
	}
}
