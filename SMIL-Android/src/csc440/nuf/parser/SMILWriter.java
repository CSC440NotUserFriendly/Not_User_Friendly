package csc440.nuf.parser;

import java.io.*;

import csc440.nuf.complay.Q;
import csc440.nuf.complay.Waiting;
import csc440.nuf.components.AbstractSMILObject;

/**
 * CSC-440 SMIL Project 01-22-2011 SMILWriter.java
 * 
 * @author Brad Barker
 * 
 *         Main class for writing SMIL Files.
 * 
 * 
 */

public class SMILWriter {

	public static void saveSMIL(File SMILFile) {
		try {
			Q queue = Waiting.Q();
			queue.prepQ();
			System.out.println("Save: " + queue + " Size: " + queue.getLength());
			PrintWriter pw = new PrintWriter(SMILFile);
			pw.println("<smil>");
			while (!queue.isEmpty()) {
				String temp = queue.pop().printTag();
				System.out.println(temp);
				pw.println(temp);
			}
			pw.println("</smil>");
			pw.close();
			
		} catch (Exception e) {
			System.err.println("Unable to write file: " + e);
		}

	}

	// This method saves the WaitingQueue in serialized form for quick restart.
	public static void saveState() {

		// Todo if time permits
	}

}
