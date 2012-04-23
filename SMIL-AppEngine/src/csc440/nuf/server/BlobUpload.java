package csc440.nuf.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class BlobUpload extends HttpServlet {
    //private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
		System.err.println("Made it!");
    	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    	//Delete the old blob
    	System.out.println(req.getHeader("blob-key").length());
    	if(req.getHeader("blob-key").length() != 0){
    		BlobKey oldKey = new BlobKey(req.getHeader("blob-key"));
    		blobstoreService.delete(oldKey);
    	}
    	
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKey = blobs.get(req.getHeader("name"));

        System.out.println(blobKey + " Length: " + blobKey.size());
        
        if (blobKey.size() == 0) {
            res.addHeader("blob-key", "fail");
        } else {
            res.addHeader("blob-key", blobKey.get(0).getKeyString());
        }
    }
}
