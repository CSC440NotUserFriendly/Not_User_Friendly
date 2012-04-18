package testing.src.csc440.nuf.complay;

import static org.junit.Assert.*;

import org.junit.Test;

import csc440.nuf.complay.Q;
import csc440.nuf.components.*;

/**
 * QTest.java
 * @author Alex Gilbert
 */


public class QTest{
	
	Q q;
	float density[] = { 5, 10 };
	int messageLength[] = { 5, 10, 0 };
	int length[]= { 1, 2, 0 };
	SMILText SMILTextObject1;
	SMILText SMILTextObject2;
	
	public QTest()
	{
		this.q = new Q("Test");
		SMILTextObject1 = new SMILText(0, 1, 5, 5, "Test1", 12, "Blue");
		SMILTextObject2 = new SMILText(1, 2, 10, 10, "Test2", 12, "Red");
		q.push(SMILTextObject1);
	}
	/*
	@SuppressWarnings("deprecation")
	@Test
	public void testSetScreenDensity()
	{
		q.setScreenDensity(density[0]);
		assertEquals(density[0], q.getDensity());
	}
	*/
	@Test
	public void testPush()
	{
		assertFalse(q.isEmpty());
		q.push(SMILTextObject2);
		assertEquals(SMILTextObject2, q.peek());
		
	}
	
	@Test
	public void testPeek()
	{
		assertFalse(q.isEmpty());
		assertEquals(SMILTextObject1, q.peek());
		assertFalse(q.isEmpty());
		
	}

	@Test
	public void testPop()
	{
		assertFalse(q.isEmpty());
		q.push(SMILTextObject2);
		assertEquals( SMILTextObject2, q.pop());
		assertFalse(q.isEmpty());
		assertEquals( SMILTextObject1, q.peek());
		assertEquals( SMILTextObject1, q.pop());
		assertTrue(q.isEmpty());
		
	}
	
	@Test
	public void testPreqQ()
	{
		q.pop();
		assertTrue(q.isEmpty());
		q.push(SMILTextObject2);
		q.push(SMILTextObject1);
		q.prepQ();
		assertEquals(SMILTextObject2, q.pop());
		assertEquals(SMILTextObject1, q.pop());
		assertTrue(q.isEmpty());
		
	}
	
	@Test
	public void testGetMessageLength()
	{
		assertEquals(messageLength[0], q.getMessageLength());
		q.push(SMILTextObject2);
		assertEquals(messageLength[1], q.getMessageLength());
		q.pop();
		q.pop();
		assertTrue(q.isEmpty());
		assertEquals(messageLength[2], q.getMessageLength());
		
	}
	
	@Test
	public void testGetLength()
	{
		assertEquals(length[0], q.getLength());
		q.push(SMILTextObject2);
		assertEquals(length[1], q.getLength());
		q.pop();
		q.pop();
		assertTrue(q.isEmpty());
		assertEquals(length[2], q.getLength());
		
	}
	
	@Test
	public void testGetQ()
	{
		assertEquals(SMILTextObject1, q.getQ().getFirst());
		q.push(SMILTextObject2);
		assertEquals(SMILTextObject1, q.getQ().getFirst());
		assertEquals(SMILTextObject2, q.getQ().getLast());
		q.pop();
		assertTrue(q.getQ().contains(SMILTextObject1));
		q.pop();
		assertFalse(q.getQ().contains(SMILTextObject1));
		
	}
	
	@Test
	public void testClear()
	{
		assertFalse(q.isEmpty());
		q.clear();
		assertTrue(q.isEmpty());
		
	}
	
}
