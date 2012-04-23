package csc440.nuf.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class BlobServe extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2828362876044152861L;
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException {
		System.out.println("Made it");
        BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
        res.setContentType("application/zip");
        blobstoreService.serve(blobKey, res);
    }
}
