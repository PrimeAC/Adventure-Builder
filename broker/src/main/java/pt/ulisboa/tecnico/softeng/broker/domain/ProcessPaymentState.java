package pt.ulisboa.tecnico.softeng.broker.domain;


import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;

public class ProcessPaymentState extends AdventureState {
    @Override
    public Adventure.State getState() {
        return Adventure.State.PROCESS_PAYMENT;
    }

    @Override
    public void process(Adventure adventure) {
        try{
            String payment = BankInterface.processPayment(adventure.getIBAN(), adventure.getAmount());
            adventure.setPaymentConfirmation(payment);
            adventure.setState(Adventure.State.RESERVE_ACTIVITY);
        } catch (BankException be) {
            adventure.setState(Adventure.State.CANCELLED);
        } catch (RemoteAccessException rae) {
            incNumOfRemoteErrors();
            if (numOfRemoteErrors == 3) {
                adventure.setState(Adventure.State.CANCELLED);
            }
        }

    }


}

