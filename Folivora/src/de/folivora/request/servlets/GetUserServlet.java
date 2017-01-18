package de.folivora.request.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.UserManager;
import de.folivora.model.Constants;
import de.folivora.model.User;

/**
 * Servlet implementation class GetUserServlet
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
@WebServlet("/getuserservlet")
public class GetUserServlet extends HttpServlet {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;
	private static final Logger logger = Logger.getLogger(GetUserServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserManager uManager = ApplicationManager.getApplicationManagerInstance().getuManager();
		User user = uManager.getUserWithId(request.getParameter("id"));
		if(user == null) {
			response.sendError(404, "User not found");
			return;
		}
		
		response.setStatus(200);
		response.setContentType("application/json");
		
		try {
			Gson gson = new Gson();
			String s = gson.toJson(user.getAsJsonObject());
			ServletOutputStream sos = response.getOutputStream();
			sos.write(s.getBytes());
			sos.flush();
			sos.close();
		} catch (IOException e) {
			logger.warn("Failed to to write data to response obj.");
		}
	}
}