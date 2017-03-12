package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class BrokerConstructorMethodTest {

	@Test
	public void success() {
		Broker broker = new Broker("BR01", "WeExplore");
		Assert.assertEquals("BR01", broker.getCode());
		Assert.assertEquals("WeExplore", broker.getName());
		Assert.assertEquals(0, broker.getNumberOfAdventures());
		Assert.assertTrue(Broker.brokers.contains(broker));
	}

	@Test(expected=BrokerException.class)
	public void nullCode() {
		new Broker(null, "WeExplore");
	}

	@Test(expected=BrokerException.class)
	public void nullName() {
		new Broker("BR01", null);
	}
	
	@Test(expected=BrokerException.class)
	public void emptyCode() {
		new Broker("  ", "WeExplore");
	}
	
	@Test(expected=BrokerException.class)
	public void emptyName() {
		new Broker("BR01", "    ");
	}

	@Test(expected=BrokerException.class)
	public void uniqueCode() {
		new Broker("BR01", "WeExplore");
		new Broker("BR01", "WeExploreX");
	}
	

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
