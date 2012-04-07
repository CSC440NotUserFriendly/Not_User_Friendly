package testing.src.csc440.nuf.components;

import static org.junit.Assert.*;

import org.junit.Test;

import csc440.nuf.components.*;

/**
 * AbstractSMILObjectTest.java
 * @author Alex Gilbert
 */

public class AbstractSMILObjectTest {

	SMILText SMILTextObject1;
	SMILText SMILTextObject2;
	SMILText SMILTextObject3;
	int compare[] = { -1, 0, 1 };
	int time[] = { 5, 10 };
	int intVal[] = { -1, 0 };
	String str[] = { "", "Test" };
	
	public AbstractSMILObjectTest()
	{
		SMILTextObject1 = new SMILText(0, 1, 5, 5, "Test1", 12, "Blue");
		SMILTextObject2 = new SMILText(1, 2, 10, 10, "Test2", 12, "Red");
		SMILTextObject3 = new SMILText(1, 2, 15, 15, "Test3", 12, "Yellow");
	}
	
	@Test
	public void testCompareTo() 
	{
		assertEquals(compare[0], SMILTextObject1.compareTo(SMILTextObject2));
		assertEquals(compare[1], SMILTextObject2.compareTo(SMILTextObject3));
		assertEquals(compare[2], SMILTextObject3.compareTo(SMILTextObject1));
		
	}
	
	@Test
	public void testSetStartTime()
	{
		SMILTextObject3.setStartTime(time[0]);
		assertEquals(time[0], SMILTextObject3.getStartTime());
		
	}
	
	@Test
	public void testSetEndTime()
	{
		SMILTextObject3.setEndTime(time[1]);
		assertEquals(time[1], SMILTextObject3.getEndTime());
		
	}
	
	@Test
	public void testGetIntValue()
	{
		assertEquals(intVal[0], AbstractSMILObject.getIntValue(str[0]));
		assertEquals(intVal[1], AbstractSMILObject.getIntValue(str[1]));
		
	}
	
	@Test
	public void testToString()
	{
		assertEquals(str[1], SMILTextObject1.toString());
	
	}
	
	@Test
	public void testSetDuration()
	{
		SMILTextObject3.setDuration(time[1]);
		assertEquals(time[1], SMILTextObject3.getDuration());
		
	}

}
