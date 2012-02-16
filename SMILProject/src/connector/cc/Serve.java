package connector.cc;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.storage.onestore.v3.OnestoreEntity.User;

public class Serve extends HttpServlet {
	
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException {
	
		UserService userService = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();

        if (user != null) {
            res.setContentType("text/plain");
            res.getWriter().println("Hello, " + user.getNickname());
        } else {
            res.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }

        BlobKey blobKey = new BlobKey(req.getParameter("id"));
        blobstoreService.serve(blobKey, res);
        
    }

}
