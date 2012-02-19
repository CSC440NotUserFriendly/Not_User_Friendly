package connector.cc;


/**
 * CSC-440 SMIL Project
 * 02-16-2011
 * BaseServlet.java
 * @author Alex Gilbert
 * 
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class BaseServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	  throws ServletException, IOException {
	resp.setContentType("application/json; charset=utf-8");
	resp.setHeader("Cache-Control", "no-cache");
  }
}
