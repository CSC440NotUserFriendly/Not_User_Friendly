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
 * SMILVideoTest.java
 * @author Alex Gilbert
 * 
 */

class SMILVideoTest extends TestCase{
	
	private AttributesImpl att;
	private SMILVideo sv;
	/* 
	 * 
	 */
	@BeforeClass
	public void setUp(){
	    att = new AttributesImpl();
		att.addAttribute("testUri","testLocalName","testQName","testType","testValue");
		sv = new SMILVideo(att);
	}
}
