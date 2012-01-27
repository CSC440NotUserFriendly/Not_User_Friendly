package com.csc440.nuf.parser;

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
    SMILParser(File SMILFile) {
        try {
            is = new FileInputStream(SMILFile);
            parse();

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
            xReader.parse(new InputSource(is));


        } catch (ParserConfigurationException e) {
            //Bad ParserFactory
        } catch (SAXException e) {
            //Bad SAXParser
        } catch (IOException e) {
            //Bad XMLReader
        }


    }
}

