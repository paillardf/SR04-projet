package fr.utc.sr04.projet.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class EventVersion {

		private final static String TAG_OWNER="owner";
		private final static String TAG_TIME="time";
		
		
		public EventVersion(JsonObject json) {
			this.owner = json.get(TAG_OWNER).getAsString();
			this.time = json.get(TAG_TIME).getAsLong();
		}
		public EventVersion() {
		}
		public String owner;
		public long time;
		
		
		public JsonElement toJson() {
			JsonObject json = new JsonObject();
			json.addProperty(TAG_OWNER, owner);
			json.addProperty(TAG_TIME, time);
			return json;
		}
		
}
