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
import de.folivora.model.messenger.Message;
import de.folivora.request.AccessLayer;

/**
 * Servlet implementation class SendMessageServlet
 * <hr>Created on 21.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
@WebServlet("/sendmessageservlet")
public class SendMessageServlet extends HttpServlet {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;
	private static final Logger logger = Logger.getLogger(SendMessageServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMessageServlet() {
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
			
			if(callingUser != null) {
				SearchRequest sr = aManager.getSearchRequestWithId(input.getSrId());
				
				if(sr != null) {
					User receivingUser = uManager.getUserWithId(input.getUserMsgReceiverId());
					
					if(receivingUser != null) {
						Message m = AccessLayer.sendMsg("", input.getMsgText(), callingUser, receivingUser, sr);
						new ResponseObject(200, "Ok", response).writeResponse();
						logger.info(callingUser + " successfully sended " + receivingUser + " the message " + m);
					} else {
						new ResponseObject(400, "There is no user with the id=" + input.getUserMsgReceiverId() + "!", response).writeResponse();
						logger.warn("Failed to send message!");
					}
				} else {
					new ResponseObject(400, "There is no search request with the id=" + input.getSrId() + "!", response).writeResponse();
					logger.warn("Failed to send message!");
				}
			} else {
				new ResponseObject(400, "There is no user with the id=" + input.getUserCallingId() + "!", response).writeResponse();
				logger.warn("Failed to send message!");
			}
		} catch(Exception e) {
			new ResponseObject(500, "Failed to map input with gson!", response).writeResponse();
			logger.error("Failed to map inputdata!", e);
		}
	}
	
	class Input {
		private String userCallingId,
					   userMsgReceiverId,
					   srId,
					   msgText;

		/**
		 * @return the msgText
		 */
		public String getMsgText() {
			return msgText;
		}

		/**
		 * @param msgText the msgText to set
		 */
		public void setMsgText(String msgText) {
			this.msgText = msgText;
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

		/**
		 * @return the userMsgReceiverId
		 */
		public String getUserMsgReceiverId() {
			return userMsgReceiverId;
		}

		/**
		 * @param userMsgReceiverId the userMsgReceiverId to set
		 */
		public void setUserMsgReceiverId(String userMsgReceiverId) {
			this.userMsgReceiverId = userMsgReceiverId;
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