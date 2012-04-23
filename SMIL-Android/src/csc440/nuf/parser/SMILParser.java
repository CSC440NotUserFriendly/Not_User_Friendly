package csc440.nuf.parser;

/**
 * CSC-440 SMIL Project
 * 01-22-2011
 * SMILParser.java
 * @author Brad Barker
 * 
 * Accepts a URL and retrieves the XML to be extracted.
 * 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import csc440.nuf.complay.Waiting;

public class SMILParser {

    private InputStream is;

    //If connection passes a URL
    SMILParser(URL SMILURL) {
        try {
            is = SMILURL.openStream();
            parse();
            
        } catch (IOException ex) {
            Logger.getLogger(SMILParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    //If connection passes a file
    public SMILParser(File SMILFile) {
        try {
            is = new FileInputStream(SMILFile);
            parse();
            System.out.println("Open: " + Waiting.Q() + " Size: " + Waiting.Q());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SMILParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void parse() {

        try {

            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xReader = sp.getXMLReader();
            SMILHandler sHandler = new SMILHandler();

            xReader.setContentHandler(sHandler);
            InputSource in = new InputSource(is);
            in.setEncoding("UTF-8");
            xReader.parse(in);


        } catch (ParserConfigurationException e) {
            //Bad ParserFactory
        	System.err.println("ParserFactory: " + e);
        } catch (SAXException e) {
            //Bad SAXParser
        	System.err.println("SAXParser: " + e);
        	e.printStackTrace();
        } catch (IOException e) {
            //Bad XMLReader
        	System.err.println("XMLReader: " + e);
        } catch (Exception e){
        	e.printStackTrace();
        	System.err.println("Parser: " + e);
        }


    }
}

