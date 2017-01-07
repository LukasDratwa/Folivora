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
import de.folivora.model.SearchRequest;
import de.folivora.model.User;
import de.folivora.request.AccessLayer;
import de.folivora.util.Constants;

/**
 * Servlet implementation class FinishSrServlet
 */
@WebServlet("/finishsrservlet")
public class FinishSrServlet extends HttpServlet {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;
	private static final Logger logger = Logger.getLogger(FinishSrServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FinishSrServlet() {
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
			SearchRequest sr = aManager.getSearchRequestWithId(input.getSrId());
			
			if(input.getUnlockToken() == null) {
				new ResponseObject(400, "Please provide the filed \"unlockToken\"!", response).writeResponse();
				logger.warn("Failed to finish SR!");
				return;
			}
			
			User callingUser = uManager.getUserWithId(input.getUserCallingId());
			if(sr != null) {
				AccessLayer.finsihSr(sr, input.getUnlockToken(), callingUser);
			} else {
				new ResponseObject(400, "There is no SR with the id=" + input.getSrId() + "!", response).writeResponse();
				logger.warn("Failed to finish SR!");
			}
		} catch(Exception e) {
			new ResponseObject(500, "Failed to map input with gson!", response).writeResponse();
			logger.error("Failed to map inputdata!", e);
		}
	}
	
	class Input {
		private long userCallingId;
		private long srId;
		private String unlockToken;
		
		/**
		 * @return the userCallingId
		 */
		public long getUserCallingId() {
			return userCallingId;
		}
		
		/**
		 * @param userCallingId the userCallingId to set
		 */
		public void setUserCallingId(long userCallingId) {
			this.userCallingId = userCallingId;
		}
		
		/**
		 * @return the srId
		 */
		public long getSrId() {
			return srId;
		}
		
		/**
		 * @param srId the srId to set
		 */
		public void setSrId(long srId) {
			this.srId = srId;
		}

		/**
		 * @return the unlockToken
		 */
		public String getUnlockToken() {
			return unlockToken;
		}

		/**
		 * @param unlockToken the unlockToken to set
		 */
		public void setUnlockToken(String unlockToken) {
			this.unlockToken = unlockToken;
		}
	}
}