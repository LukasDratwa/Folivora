package de.folivora.request.servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.UserManager;
import de.folivora.model.Constants;
import de.folivora.model.User;
import de.folivora.request.AccessLayer;

/**
 * Servlet implementation class GetUnseenMessagesServlet
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
@WebServlet("/getmessagesservlet")
public class GetMessagesServlet extends HttpServlet {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;
	private static final Logger logger = Logger.getLogger(GetMessagesServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMessagesServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
		Gson gson = new GsonBuilder().create();
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		UserManager uManager = aManager.getuManager();
		try {
			Input input = gson.fromJson(sB.toString(), Input.class);
			User user = uManager.getUserWithId(input.getUserCallingId());
			
			if(user != null) {
				JsonArray msgArray = AccessLayer.getRelevantMsgsOfUser(user);
				ResponseObject ro = new ResponseObject(200, "Ok", response);
				ro.setUserMessages(msgArray);
				ro.writeResponse();
			} else {
				new ResponseObject(400, "There is no user with the id=" + input.getUserCallingId() + "!", response).writeResponse();
				logger.warn("Failed to cancel SR!");
			}
		} catch(Exception e) {
			new ResponseObject(500, "Failed to map input with gson!", response).writeResponse();
			logger.error("Failed to map inputdata!", e);
		}
	}
	
	class Input {
		private String userCallingId;
		
		/**
		 * @return the userCallingId
		 */
		public String getUserCallingId() {
			return userCallingId;
		}
		
		/**
		 * @param userCallingId the userCallingId to set
		 */
		public void setUserCallingId(String userCallingId) {
			this.userCallingId = userCallingId;
		}
	}
}