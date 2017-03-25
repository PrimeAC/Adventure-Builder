package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;

@RunWith(JMockit.class)
public class ReserveActivityStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private final LocalDate begin = LocalDate.now();
	private final LocalDate end = LocalDate.now().plusDays(2);
	private Adventure adventure;
	@Injectable
	private Broker broker;
	
	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(State.CANCELLED);
	}
	
	@Test
	public void stateTest(@Mocked final ActivityInterface activityInterface){

		this.adventure.process();

		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY , this.adventure.getState());

		new Verifications() {
			{
				ActivityInterface.getActivityReservationData(this.anyString);
				this.times = 0;
			}
		};
	}
	
	@Test
	public void successConfirmedProcess(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(withSameInstance(begin) ,withSameInstance(begin),anyInt);
				this.result = PAYMENT_CONFIRMATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		Assert.assertEquals(PAYMENT_CONFIRMATION, adventure.getActivityConfirmation());
	}
	
	@Test
	public void successBookedRoomProcess(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(withSameInstance(begin) ,withSameInstance(begin),anyInt);
				this.result = PAYMENT_CONFIRMATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		Assert.assertEquals(PAYMENT_CONFIRMATION, adventure.getActivityConfirmation());
	}

	@Test
	public void ActivityExceptionTest(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(withSameInstance(begin) ,withSameInstance(begin),anyInt);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void RemoteExceptionTest(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity(withSameInstance(begin) ,withSameInstance(begin),anyInt);
				this.times=5;
				this.result = new ActivityException();
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	
}
