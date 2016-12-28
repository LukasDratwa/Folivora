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

import de.folivora.model.User;
import de.folivora.util.Constants;

/** 
 * Servlet to allow users to make an account for folivora
 * <br><br>
 * ---------------------------------------------------------------<br>
 *
 * @author Lukas Dratwa
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
			logger.error("Failed to get input data from request.", e);
		}
		
		// Map inputdata
		Gson gson = new Gson();
		User signUpData = null;
		try {
			signUpData = gson.fromJson(sB.toString(), User.class);
			System.out.println(signUpData);
		} catch(Exception e) {
			logger.error("Failed to map inputdata!", e);
			response.setStatus(403);
		}
	}
}