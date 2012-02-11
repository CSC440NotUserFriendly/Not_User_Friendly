package com.csc440.nuf.cc;

import java.io.IOException;
import java.util.Map;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class Download extends HttpServlet {
	
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException
    {
    	
    	String filename = req.getParameter("file");
    	String key = req.getParameter("key");
    	
    	if(key != null)
    	{
    		
    		blobstoreService.serve(new BlobKey(key), res);
    		
    	}
    	else
    	{
    		
    		if(filename != null)
    		{
	    			
		    	BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
		    	Iterator<BlobInfo> it = blobInfoFactory.queryBlobInfos();
		    	BlobKey blobkey = null;
		    	
		    	while(it.hasNext())
		    	{
		    		
		    		BlobInfo blobInfo = it.next();
		    		
		    		if(blobInfo.getFilename().equals(filename))
		    			blobkey = blobInfo.getBlobKey();
		    		
		    	}
		    	
		    	if(blobkey!=null)
		    		blobstoreService.serve(blobkey, res);
		    	
    		}
    	}
	    	
    }
}
