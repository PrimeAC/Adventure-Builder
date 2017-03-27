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
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;

@RunWith(JMockit.class)
public class ReserveActivityStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private final LocalDate begin = LocalDate.now();
	private final LocalDate end = LocalDate.now().plusDays(1);
	private Adventure adventure;
	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(Adventure.State.RESERVE_ACTIVITY);
	}

	@Test
	public void stateTest(@Mocked final ActivityInterface activityInterface) {

		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());

		new Verifications() {
			{
				ActivityInterface.getActivityReservationData(this.anyString);
				this.times = 0;
			}
		};
	}

	@Test
	public void ConfirmProcess(@Mocked final ActivityInterface activityInterface) {

		// the following method have as 1st agr - a LocalDate, 2nd arg - same
		// LocalDate as 1st arg
		this.adventure = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);
		this.adventure.setState(Adventure.State.RESERVE_ACTIVITY);

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
	public void BookRoomProcess(@Mocked final ActivityInterface activityInterface) {

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
	public void ActivityExceptionTest(@Mocked final ActivityInterface activityInterface) {

		new StrictExpectations() {
			{
				ActivityInterface.reserveActivity((LocalDate) any, (LocalDate) any, anyInt);
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
				ActivityInterface.reserveActivity((LocalDate) any, (LocalDate) any, anyInt);
				this.times = 5;
				this.result = new ActivityException();
			}
		};

		this.adventure.process();
		this.adventure.setState(Adventure.State.RESERVE_ACTIVITY);
		this.adventure.process();
		this.adventure.setState(Adventure.State.RESERVE_ACTIVITY);
		this.adventure.process();
		this.adventure.setState(Adventure.State.RESERVE_ACTIVITY);
		this.adventure.process();
		this.adventure.setState(Adventure.State.RESERVE_ACTIVITY);
		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
}
