package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;

@RunWith(JMockit.class)
public class ReserveActivityStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private final LocalDate begin = LocalDate.now();
	private final LocalDate end = LocalDate.now().plusDays(1);
	private Adventure adventure;
	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(Adventure.State.RESERVE_ACTIVITY);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

	}

	@Test
	public void confirmProcess(@Mocked final ActivityInterface activityInterface) {

		this.adventure = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);
		this.adventure.setState(Adventure.State.RESERVE_ACTIVITY);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity((LocalDate) any, (LocalDate) any, anyInt);
				result = ACTIVITY_CONFIRMATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		Assert.assertEquals(ACTIVITY_CONFIRMATION, this.adventure.getActivityConfirmation());
	}

	@Test
	public void bookRoomProcess(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity((LocalDate) any, (LocalDate) any, anyInt);
				this.result = ACTIVITY_CONFIRMATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		Assert.assertEquals(ACTIVITY_CONFIRMATION, adventure.getActivityConfirmation());
	}

	@Test
	public void activityExceptionTest(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity((LocalDate) any, (LocalDate) any, anyInt);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		Assert.assertNull(this.adventure.getActivityConfirmation());
	}

	@Test
	public void remoteExceptionTest(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity((LocalDate) any, (LocalDate) any, anyInt);
				this.times = 5;
				this.result = new RemoteAccessException();
			}
		};

		for (int i = 0; i < 4; i++)
			this.adventure.process();

		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		Assert.assertNull(this.adventure.getActivityConfirmation());

		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void successAfterRemoteExceptions(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity((LocalDate) any, (LocalDate) any, anyInt);
				this.times = 4;
				this.result = new RemoteAccessException();

				ActivityInterface.reserveActivity((LocalDate) any, (LocalDate) any, anyInt);
				this.result = ACTIVITY_CONFIRMATION;
			}
		};

		for (int i = 0; i < 4; i++)
			this.adventure.process();

		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		Assert.assertNull(this.adventure.getActivityConfirmation());

		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		Assert.assertEquals(ACTIVITY_CONFIRMATION, this.adventure.getActivityConfirmation());

	}

	@Test
	public void activityExceptionAfterRemoteException(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity((LocalDate) any, (LocalDate) any, anyInt);
				this.result = new RemoteAccessException();

				ActivityInterface.reserveActivity((LocalDate) any, (LocalDate) any, anyInt);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		Assert.assertNull(this.adventure.getActivityConfirmation());

		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
}
