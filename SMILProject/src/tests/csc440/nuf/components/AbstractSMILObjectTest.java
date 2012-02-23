
package tests.csc440.nuf.components;

import com.csc440.nuf.components.*;

import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.xml.sax.helpers.AttributesImpl;

/**
 * CSC-440 SMIL Project
 * 02-09-2011
 * AbstractSMILObjectTest.java
 * @author Alex Gilbert
 * 
 */

class AbstractSMILObjectTest extends TestCase{
	
	private AttributesImpl att;
	private SMILAudio sa;
	/* 
	 * 
	 */
	@Override
	@BeforeClass
	public void setUp(){
	    att = new AttributesImpl();
		att.addAttribute("testUri","testLocalName","testQName","testType","testValue");
		sa = new SMILAudio(att);
	}
}
