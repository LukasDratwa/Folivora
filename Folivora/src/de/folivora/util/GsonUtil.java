package de.folivora.util;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class GsonUtil {
	/**
	 * Small utility method to create a Gson which will handle date as long values.
	 * 
	 * @return
	 */
	public static Gson getGsonDateAsLongHandling() {
		// Creates the json object which will manage the information received 
		GsonBuilder builder = new GsonBuilder(); 

		// Register an adapter to manage the date types as long values 
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
			@Override
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {
				return new Date(json.getAsJsonPrimitive().getAsLong()); 
			} 
		});

		return builder.create();
	}
}