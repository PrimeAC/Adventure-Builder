package pt.ulisboa.tecnico.softeng.broker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class ConfirmedState extends AdventureState {

	private static Logger logger = LoggerFactory.getLogger(ConfirmedState.class);

	@Override
	public State getState() {
		return State.CONFIRMED;
	}

	@Override
	public void process(Adventure adventure) {
		logger.debug("process");

		BankOperationData operation;
		try {
			operation = BankInterface.getOperationData(adventure.getPaymentConfirmation());
		} catch (BankException be) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == 5) {
				adventure.setState(State.UNDO);
			}
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == 20) {
				adventure.setState(State.UNDO);
			}
			return;
		}
		logger.debug("Payment confirmation: " + operation.getReference());
		logger.debug("Type: " + operation.getType());
		logger.debug("Value: " + operation.getValue());

		ActivityReservationData reservation;
		try {
			reservation = ActivityInterface.getActivityReservationData(adventure.getActivityConfirmation());
		} catch (ActivityException ae) {
			adventure.setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == 20) {
				adventure.setState(State.UNDO);
			}
			return;
		}
		logger.debug("Activity confirmation: " + reservation.getReference());
		logger.debug("Activity name: " + reservation.getName());
		logger.debug("Activity code: " + reservation.getCode());
		logger.debug("Begin: " + reservation.getBegin());
		logger.debug("End: " + reservation.getEnd());


		if (adventure.getRoomConfirmation() != null) {
			RoomBookingData booking;
			try {
				booking = HotelInterface.getRoomBookingData(adventure.getRoomConfirmation());
			} catch (HotelException he) {
				adventure.setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				incNumOfRemoteErrors();
				if (getNumOfRemoteErrors() == 20) {
					adventure.setState(State.UNDO);
				}
				return;
			}
			logger.debug("Room confirmation: " + booking.getReference());
			logger.debug("Hotel name: " + booking.getHotelName());
			logger.debug("Hotel code: " + booking.getHotelCode());
			logger.debug("Room number: " + booking.getRoomNumber());
			logger.debug("Room type: " + booking.getRoomType());
			logger.debug("Arrival: " + booking.getArrival());
			logger.debug("Departure: " + booking.getDeparture());

		}
		resetNumOfRemoteErrors();
	}
}
