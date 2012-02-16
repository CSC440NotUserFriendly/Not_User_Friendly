package connector.cc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.storage.onestore.v3.OnestoreEntity.User;

public class UploadURL extends HttpServlet {
	
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException
    {
	
		UserService userService = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();

        if (user != null) {
            res.setContentType("text/plain");
            res.getWriter().println("Hello, " + user.getNickname());
        } else {
            res.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }
    	
    	PrintWriter out = res.getWriter();
    	String url = blobstoreService.createUploadUrl("/upload");
    	res.setContentType("text/plain");
    	out.println(url);
    	
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException
    {
    	UserService userService = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();

        if (user != null) {
            res.setContentType("text/plain");
            res.getWriter().println("Hello, " + user.getNickname());
        } else {
            res.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }

    	PrintWriter out = res.getWriter();
    	Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        BlobKey blobKey = blobs.get("myFile");
        out.println(blobKey.getKeyString());
    	
    }
}
