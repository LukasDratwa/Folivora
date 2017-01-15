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
import de.folivora.model.User;
import de.folivora.request.AccessLayer;

/**
 * Servlet implementation class SetMessageUnseenServlet
 * 
 * <hr>Created on 15.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
@WebServlet("/setmessageseenservlet")
public class SetMessageSeenServlet extends HttpServlet {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;
	private static final Logger logger = Logger.getLogger(SetMessageSeenServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetMessageSeenServlet() {
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
			
			if(input.getMsgIds() != null && !input.getMsgIds().equals("")) {
				for(String msgId : input.getMsgIds().split(",")) {
					AccessLayer.setMsgSeen(callingUser, aManager.getMessageWithId(msgId));
				}
				new ResponseObject(200, "Ok", response).writeResponse();
				logger.info("Called servlet to mark \"" + input.getMsgIds() + "\" as seen.");
			} else {
				new ResponseObject(400, "Please enter some msg id which should be set to seen.", response).writeResponse();
				logger.warn("Failed to set messages seen!");
			}
		} catch(Exception e) {
			new ResponseObject(500, "Failed to map input with gson!", response).writeResponse();
			logger.error("Failed to map inputdata!", e);
		}
	}
	
	class Input {
		private String msgIds = "";
		private String userCallingId = "";
		
		/**
		 * @return the msgIds
		 */
		public String getMsgIds() {
			return msgIds;
		}

		/**
		 * @param msgIds the msgIds to set
		 */
		public void setMsgIds(String msgIds) {
			this.msgIds = msgIds;
		}

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