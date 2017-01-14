package de.folivora.request.servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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
import de.folivora.request.AccessLayer;
import de.folivora.util.GsonUtil;

/**
 * Servlet to allow users to make an account for folivora
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
@WebServlet("/signupservlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;
	private static final Logger logger = Logger.getLogger(SignUpServlet.class);

	/**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
		dispatcher.forward(request, response);
		response.setStatus(200);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sB = new StringBuilder();
		try(BufferedReader sis = request.getReader()) {
			// Get data
			String l = "";
			while((l = sis.readLine()) != null) {
				sB.append(l);
			}
			
			sis.close();
		} catch(Exception e) {
			new ResponseObject(500, "Failed to get data from request!", response).writeResponse();
			logger.error("Failed to get input data from request.", e);
		}
		
		// Map inputdata
		Gson gson = GsonUtil.getGsonDateAsLongHandling();
		User user = null;
		try {
			user = gson.fromJson(sB.toString(), User.class);
				
			if(user.getName() != null && user.getEmail() != null && user.getPassword() != null) {
				ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
				UserManager uManager = aManager.getuManager();
				if(uManager.isUniqueUser(user)) {
					AccessLayer.signUp(user, request.getSession());
					ResponseObject ro = new ResponseObject(200, "Successfully signed up!", response);
					ro.setToken(uManager.getUserWithNameOrEmail(user.getName()).getTokenStorage().getToken());
					ro.writeResponse();
					logger.info("Successfully signed up user: " + user);
				} else {
					new ResponseObject(400, "User exists already! Change \"name\" and/or \"email\"", response).writeResponse();
					logger.warn("User exists already, cant save user: " + user);
				}
			} else {
				new ResponseObject(400, "Missing credentials \"name\", \"password\" and/or \"email\"", response).writeResponse();
				logger.info("Missing data to create new user.");
			}
		} catch(Exception e) {
			new ResponseObject(500, "Failed to map input with gson!", response).writeResponse();
			logger.error("Failed to map inputdata!", e);
		}
	}
}