package com.csc440.nuf.cc;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class GetMode extends HttpServlet {
	
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException
    {
    	
    	String mode = req.getParameter("mode");
    	PrintWriter out = res.getWriter();
    	
	    if(mode.equals("upload"))
	    {
	    	String url = blobstoreService.createUploadUrl("/upload");
	    	res.setContentType("text/plain");
	    	out.println(url);
	    }
	    
	    if(mode.equals("download"))
	    {
	    	
	    	String filename = req.getParameter("file");
	    	Map<String, BlobKey> keys = blobstoreService.getUploadedBlobs(req);
	    	BlobKey blobKey = keys.get(filename);
	        blobstoreService.serve(blobKey, res);	
	    
	    }
	    
    }
}
