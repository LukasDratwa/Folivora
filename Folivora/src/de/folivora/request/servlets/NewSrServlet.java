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
 * Servlet implementation class NewSrServlet
 */
@WebServlet("/newsrservlet")
public class NewSrServlet extends HttpServlet {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;
	private static final Logger logger = Logger.getLogger(NewSrServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewSrServlet() {
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
		try {
			SearchRequest sr = gson.fromJson(sB.toString(), SearchRequest.class);
			
			if(sr.getLat() != null && sr.getLng() !=  null && sr.getTitle() !=  null
					&& sr.getPossibleDelivery_from() !=  null && sr.getPossibleDelivery_to() !=  null) {
				UserManager uManager = aManager.getuManager();
				User userCreator = uManager.getUserWithSession(request.getSession());
				
				if(userCreator != null) {
					SearchRequest createdSr = AccessLayer.createSearchRequest(sr.getTitle(), sr.getDescription(), sr.getPathToDefaultImg(),
							sr.getPossibleDelivery_from(), sr.getPossibleDelivery_to(), sr.getCostsAndReward(),
							sr.getFee(), sr.getLat(), sr.getLng(), sr.getAddress(), userCreator, "");
					ResponseObject ro = new ResponseObject(200, "Successfully created searchrequest.", response);
					ro.setSr(createdSr.getAsJsonObject());
					ro.writeResponse();
				} else {
					new ResponseObject(403, "Failed to create searchrequest. Please sign in first!", response).writeResponse();
					logger.info("Failed to save new searchrequest. User wasn't logged in!");
				}
			} else {
				new ResponseObject(400, "Missing credentials \"lat\", \"lng\", \"title\","
						+ "\"possibleDelivery_from\" and/or \"possibleDelivery_to\"", response).writeResponse();
				logger.info("Missing data to create new searchrequest.");
			}
		} catch(Exception e) {
			new ResponseObject(500, "Failed to map input with gson!", response).writeResponse();
			logger.error("Failed to map inputdata!", e);
		}
	}
}