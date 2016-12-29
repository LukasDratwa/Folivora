package de.folivora.request.servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.UserManager;
import de.folivora.model.User;
import de.folivora.util.Constants;


/** 
 * Servlet to allow users to log in
 * <br><br>
 * ---------------------------------------------------------------<br>
 *
 * @author Lukas Dratwa
 */
@WebServlet("/signinservlet")
public class SignInServlet extends HttpServlet {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;
	private static final Logger logger = Logger.getLogger(SignInServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignInServlet() {
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
		Gson gson = new Gson();
		SignInInput signInInput = null;
		try {
			UserManager uManager = ApplicationManager.getApplicationManagerInstance().getuManager();
			HttpSession httpSession = request.getSession();
			signInInput = gson.fromJson(sB.toString(), SignInInput.class);
			
			String nameOrEmail = signInInput.getEmail() != null ? signInInput.getEmail() : signInInput.getName();
			boolean signedIn = uManager.authenticate(nameOrEmail, signInInput.getPassword());
			User user = uManager.getUserWithNameOrEmail(nameOrEmail);
			if(signedIn) {
				user.setSession(httpSession);
				ResponseObject ro = new ResponseObject(200, "Successfully signed in!", response);
				ro.setToken(user.getTokenStorage().getToken());
				ro.writeResponse();
				logger.info("Successfully signed in user: " + user);
			} else {
				new ResponseObject(400, "Failed to sign in user! Wron credentials.", response).writeResponse();
				logger.info("Faild to sign in: " + user);
			}
		} catch(JsonSyntaxException e) {
			new ResponseObject(400, "Failed to map data with gson!", response).writeResponse();
			logger.error("Failed to map data from request with gson.", e);
		}
	}
	
	/**
	 * This class handles a registered user after login.
	 * 
	 * @author Lukas Dratwa
	 */
	class SignInInput {
		private String name, password, email;
		
		private SignInInput(String name, String password, String email) {
			this.name = name;
			this.password = password;
			this.email = email;
		}
		
		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}

		/**
		 * @param password the password to set
		 */
		public void setPassword(String password) {
			this.password = password;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the email
		 */
		public String getEmail() {
			return email;
		}

		/**
		 * @param email the email to set
		 */
		public void setEmail(String email) {
			this.email = email;
		}
	}
}