package de.folivora.request.servlets;

import java.io.BufferedReader;
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
import de.folivora.controller.DataContainer;
import de.folivora.util.Constants;

/**
 * Servlet implementation class GetSrDataServlet
 */
@WebServlet("/getsrdataservlet")
public class GetSrDataServlet extends HttpServlet {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;
	private static final Logger logger = Logger.getLogger(GetSrDataServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSrDataServlet() {
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
		
		// TODO Allow some data configurations
		/*Gson gson = new GsonBuilder().create();
		try {
			InputData input = gson.fromJson(sB.toString(), InputData.class);
		} catch(Exception e) {
			new ResponseObject(500, "Failed to map input with gson!", response).writeResponse();
			logger.error("Failed to map inputdata!", e);
		}*/
		
		response.setStatus(200);
		response.setContentType("application/json");
		
		try {
			Gson gson = new Gson();
			DataContainer dC = ApplicationManager.getApplicationManagerInstance().getdC();
			String s = gson.toJson(dC.getActiveSearchRequestListAsJsonArray());
			ServletOutputStream sos = response.getOutputStream();
			sos.write(s.getBytes());
			sos.flush();
			sos.close();
		} catch (IOException e) {
			logger.warn("Failed to to write data to response obj.");
		}
	}
}