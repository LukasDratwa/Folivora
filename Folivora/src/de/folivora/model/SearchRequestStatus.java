package de.folivora.model;

/**
 * Different states of a search request.
 * <br>
 * <ul>
 * 	<li>INACTIVE - the search request is not active</li>
 *  <li>ACTIVE - the search request is active</li>
 *  <li>CANCELLED - the search request was cancelled by the creating user</li>
 *  <li>IN_PROGRESS - someone wants to deliver the requested items</li>
 *  <li>STATISFIED - the search request was successfully statisified</li>
 *  <li>FAILED - the search request failed</li>
 * </ul>
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public enum SearchRequestStatus {
	INACTIVE, ACTIVE, CANCELLED, IN_PROGRESS, STATISFIED, FAILED;
}