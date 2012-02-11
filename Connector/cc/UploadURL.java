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

public class UploadURL extends HttpServlet {
	
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException
    {
    	
    	PrintWriter out = res.getWriter();
    	String url = blobstoreService.createUploadUrl("/upload");
    	res.setContentType("text/plain");
    	out.println(url);
    	
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException
    {
    	
    	PrintWriter out = res.getWriter();
    	Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        BlobKey blobKey = blobs.get("myFile");
        out.println(blobKey.getKeyString());
    	
    }
}
