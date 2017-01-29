package de.folivora.model;

/**
 * Possible genders.
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public enum Gender {
	MALE, FEMALE;
	
	@Override
	public String toString() {
		return name() == "MALE" ? "m&auml;nnlich" : "weiblich";
	}
}