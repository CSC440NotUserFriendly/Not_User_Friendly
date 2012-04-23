package csc440.nuf.cache;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import csc440.nuf.AsyncFetchSMILMessage;
import csc440.nuf.SMILActivity;
import csc440.nuf.parser.SMILParser;
import csc440.nuf.parser.SMILWriter;
import csc440.nuf.shared.SMILMessageProxy;

import android.content.Context;
import android.os.Environment;

/**
 * CSC-440 SMIL Project
 * 02-18-2011
 * SMILCache.java
 * @author Alex Gilbert
 * 
 * For time constraints I've simplified this to just keep the most recent, Brad.
 * 
 */

public class SMILCache {
	
	private static Context activity;
	public static final String workingDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SMIL";
	
	public SMILCache(Context activity){
		SMILCache.activity = activity;
	}
	
	public static void newFile(SMILMessageProxy smilMessage){
		try {
		String filename = smilMessage.getFilename();
		String path = workingDir + "/" + filename;
		File baseDir = new File(path);
		if(!baseDir.exists()){
			baseDir.mkdirs();
		}
		else{
			//throw new Exception("A file with this name already exists!");
		}
		//new smil message file
		File baseMessage = new File(path + "/" + filename + ".smil");
		baseMessage.createNewFile();
		
		//media folder
		File media = new File(path + "/" + "media");
		media.mkdirs();
		
		Blob b = new Blob(activity);
		
		AsyncFetchSMILMessage async = new AsyncFetchSMILMessage((SMILActivity)activity);
		smilMessage = async.editMessage(smilMessage);
		smilMessage.setKey(b.sendBlob(filename, smilMessage.getKey()));
		
		async.updateMessage(smilMessage);
		}
		catch (Exception e) {
			System.err.println("Unable to create new file: " + e);
		}
	}

	public static void getFile(SMILMessageProxy smilMessage){
		try {
			String filename = smilMessage.getFilename();
			String path = workingDir + "/" + filename;
			File baseDir = new File(path);
			if(!baseDir.exists()){
				Blob b = new Blob(activity);
				b.getBlob(smilMessage.getKey(), smilMessage.getFilename());
			}
			baseDir = new File(path);
			if(!baseDir.exists())
				throw new Exception("No message found in the cloud!");
			else{
				String [] filenames = baseDir.list();
				File smilFile = null;
				for(int i=0; i<filenames.length; i++){
					if(filenames[i].contains(".smil")){
						smilFile = new File(workingDir + "/" + filename + "/" + filenames[i]);
						break;
					}
				}
				//After this has executed the queue should be populated.
				new SMILParser(smilFile); 
				
			}
			deleteOldFiles();
			
		}
		catch (Exception e) {
			System.err.println("Unable to open file: " + e);
		}
	}
	
	public static void updateFile(SMILMessageProxy smilMessage){
		
		String filename = smilMessage.getFilename();
		SMILWriter.saveSMIL(new File(workingDir + "/" + filename + "/" + filename + ".smil"));
		
		Blob b = new Blob(activity);
		AsyncFetchSMILMessage async = new AsyncFetchSMILMessage((SMILActivity)activity);
		
		smilMessage = async.editMessage(smilMessage);
		smilMessage.setKey(b.sendBlob(filename, smilMessage.getKey()));
		
		async.updateMessage(smilMessage);
	}
	
	private static void deleteOldFiles(){
		File base = new File(workingDir);
		File [] files = base.listFiles();
		Long currentTime = System.currentTimeMillis();
		final Long MAX_TIME = new Long("2592000000");
		
		//Just keeping last 30 days
		for(int i=0; i<files.length; i++){
			Long time = currentTime - files[i].lastModified();
			if(time > MAX_TIME){
				files[i].delete();
			}
		}
	}
}


