package tests.csc440.nuf.components;

import com.csc440.nuf.components.*;

import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/**
 * CSC-440 SMIL Project
 * 02-09-2011
 * SMILAudioTest.java
 * @author Alex Gilbert
 * 
 */

class SMILImageTest extends TestCase{
	
	private AttributesImpl att;
	private SMILImage si;
	/* 
	 * 
	 */
	@BeforeClass
	public void setUp(){
	    att = new AttributesImpl();
		att.addAttribute("testUri","testLocalName","testQName","testType","testValue");
		si = new SMILImage(att);
	}
}
