package testing.src.csc440.nuf.complay;

import static org.junit.Assert.*;

import org.junit.Test;

import csc440.nuf.complay.Timer;

/**
 * TimerTest.java
 * @author Alex Gilbert
 */


public class TimerTest{
	
	Timer timer;
	String data[] = { "5", "6" };
	
	public TimerTest()
	{
		this.timer = new Timer();
	}
	
	@Test
	public void testSetTime()
	{
		timer.setTime(Integer.parseInt( data[0] ));
		assertEquals( Integer.parseInt( data[0] ), timer.getTime() );
	}
	
	@Test
	public void testTimePlusPlus()
	{
		timer.setTime(Integer.parseInt( data[0] ));
		timer.timePlusPlus();
		assertEquals(Integer.parseInt( data[1] ), timer.getTime() );
	}
	
	
	
	
}