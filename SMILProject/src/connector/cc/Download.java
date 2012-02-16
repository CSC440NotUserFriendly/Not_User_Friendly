package connector.cc;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.storage.onestore.v3.OnestoreEntity.User;

public class Download extends HttpServlet {
	
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException
    {
    	
    	String filename = req.getParameter("file");
    	String id = req.getParameter("id");
    	
    	if(id != null)
    	{
    		
    		blobstoreService.serve(new BlobKey(id), res);
    		
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
