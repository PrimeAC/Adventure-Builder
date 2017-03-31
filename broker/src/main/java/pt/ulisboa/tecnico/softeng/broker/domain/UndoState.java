package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class UndoState extends AdventureState {

	@Override
	public State getState() {
		return State.UNDO;
	}

	@Override
	public void process(Adventure adventure) {

		if (adventure.cancelPayment()) {
			try {
				String paymentCancellation = BankInterface.cancelPayment(adventure.getPaymentConfirmation());
				adventure.setPaymentCancellation(paymentCancellation);
			} catch (BankException | RemoteAccessException ex) {
			}
		}

		if (adventure.cancelActivity()) {
			try {
				String activityCancellation = ActivityInterface.cancelReservation(adventure.getActivityConfirmation());
				adventure.setActivityCancellation(activityCancellation);
			} catch (ActivityException | RemoteAccessException ex) {
			}
		}

		if (adventure.cancelRoom()) {
			try {
				String roomCancellation = HotelInterface.cancelBooking(adventure.getRoomConfirmation());
				adventure.setRoomCancellation(roomCancellation);
			} catch (HotelException | RemoteAccessException ex) {
			}
		}

		if (!adventure.cancelPayment() && !adventure.cancelActivity() && !adventure.cancelRoom()) {
			adventure.setState(State.CANCELLED);
		}

	}

}
