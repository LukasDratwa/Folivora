package de.folivora.request.servlets;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class ResponseObject {
	private int httpStatus;
	private String msg;
	private String token = null;
	private JsonObject sr = null;
	private transient HttpServletResponse response;
	
	private transient static final Logger logger = Logger.getLogger(ResponseObject.class);
	
	public ResponseObject(int httpStatus, String msg, HttpServletResponse response) {
		this.httpStatus = httpStatus;
		this.msg = msg;
		this.response = response;
	}
	
	public void writeResponse() {
		response.setStatus(this.httpStatus);
		response.setContentType("application/json");
		
		try {
			ServletOutputStream sos = response.getOutputStream();
			sos.write(this.getBytes());
			sos.flush();
			sos.close();
		} catch (IOException e) {
			logger.warn("Failed to to write data to response obj.");
		}
	}
	
	public byte[] getBytes() {
		return this.toString().getBytes();
	}
	
	@Override
	public String toString() {
		return new GsonBuilder().create().toJson(this);
	}
	
	/**
	 * @return the httpStatus
	 */
	public int getHttpStatus() {
		return httpStatus;
	}
	
	/**
	 * @param httpStatus the httpStatus to set
	 */
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the sr
	 */
	public JsonObject getSr() {
		return sr;
	}

	/**
	 * @param sr the sr to set
	 */
	public void setSr(JsonObject sr) {
		this.sr = sr;
	}
}