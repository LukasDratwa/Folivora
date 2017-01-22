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
import com.google.gson.GsonBuilder;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.UserManager;
import de.folivora.model.Constants;
import de.folivora.model.SearchRequest;
import de.folivora.model.User;
import de.folivora.request.AccessLayer;

/**
 * Servlet implementation class NewFeedbackServlet
 * 
 * <hr>Created on 22.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
@WebServlet("/newfeedbackservlet")
public class NewFeedbackServlet extends HttpServlet {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;
	private static final Logger logger = Logger.getLogger(NewFeedbackServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewFeedbackServlet() {
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
		Gson gson = new GsonBuilder().create();
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		UserManager uManager = aManager.getuManager();
		try {
			Input input = gson.fromJson(sB.toString(), Input.class);
			
			User callingUser = uManager.getUserWithId(input.getUserCallingId());
			SearchRequest sr = aManager.getSearchRequestWithId(input.getSrId());
			if(callingUser != null) {
				
				if(sr != null) {
					if(input.getStars() > 0 ) {
						// Set empty feedback
						if(input.getFeedbacktext() == null) {
							input.setFeedbacktext("");
						}
						
						boolean success = AccessLayer.createFeedback(input.getStars(), input.getFeedbacktext(), callingUser, sr);
						
						if(success) {
							new ResponseObject(200, "Ok.", response).writeResponse();
							logger.info(callingUser + " created successfully a feedback for the sr: " + sr);
						} else {
							new ResponseObject(403, "Ok.", response).writeResponse();
							logger.info(callingUser + " is not allowed to create another feedback for the sr: " + sr);
						}
					}
				} else {
					new ResponseObject(400, "There is no SR with the id=" + input.getSrId() + "!", response).writeResponse();
					logger.warn("Failed to finish SR! " + sr);
				}
			} else {
				new ResponseObject(400, "There is no user with the given id!", response).writeResponse();
				logger.warn("Failed to finish SR!");
			}
		} catch(Exception e) {
			new ResponseObject(500, "Failed to map input with gson!", response).writeResponse();
			logger.error("Failed to map inputdata!", e);
		}
	}
	
	class Input {
		private String userCallingId,
					   srId,
					   feedbackText;
		private int stars;
		
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

		/**
		 * @return the feedbacktext
		 */
		public String getFeedbacktext() {
			return feedbackText;
		}

		/**
		 * @param feedbacktext the feedbacktext to set
		 */
		public void setFeedbacktext(String feedbacktext) {
			this.feedbackText = feedbacktext;
		}

		/**
		 * @return the stars
		 */
		public int getStars() {
			return stars;
		}

		/**
		 * @param stars the stars to set
		 */
		public void setStars(int stars) {
			this.stars = stars;
		}

		/**
		 * @return the srId
		 */
		public String getSrId() {
			return srId;
		}

		/**
		 * @param srId the srId to set
		 */
		public void setSrId(String srId) {
			this.srId = srId;
		}
	}
}