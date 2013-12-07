package fr.utc.sr04.projet.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Event {

	private final static String TAG_ID = "id";
	private final static String TAG_VALUE = "value";
	private final static String TAG_INDEX = "index";
	private final static String TAG_OWNER = "owner";
	private final static String TAG_TIME = "time";

	public String id;

	public Event(String infoID) {
		this.id = infoID;
	}

	public Event(JsonObject json) {
		this.id = json.get(TAG_ID).getAsString();
		this.time = json.get(TAG_TIME).getAsLong();
		this.value = json.get(TAG_VALUE).getAsString();
		this.owner = json.get(TAG_OWNER).getAsString();
		this.index = json.get(TAG_INDEX).getAsInt();
		
	}
	public String value;
	public String owner;
	public long time;
	public int index;

	public JsonElement toJson() {
		JsonObject json = new JsonObject();
		json.addProperty(TAG_ID, id);
		json.addProperty(TAG_VALUE, value);
		json.addProperty(TAG_INDEX, index);
		json.addProperty(TAG_OWNER, owner);
		json.addProperty(TAG_TIME, time);
		return json;
	}

}
