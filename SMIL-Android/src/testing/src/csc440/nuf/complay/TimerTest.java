package testing.src.csc440.nuf.complay;

import junit.framework.*;
import Users.alex.Documents.CSC_440.Project.SMIL.Not_User_Friendly.SMIL-Android/src/csc440.nuf.complay.*;

/**
 * TimerTest.java
 * @author Alex Gilbert
 */


class TimerTest 
	extends TestCase
{
	
	Timer timer;
	int data[] = { 5 };
	
	public void setup()
	{
		this.timer = new Timer();
	}
	
	public void testSetTime()
	{
		timer.setTime( data[0] );
		this.assertEquals( data[0], timer.getTime() );
	}
	
	
	
}