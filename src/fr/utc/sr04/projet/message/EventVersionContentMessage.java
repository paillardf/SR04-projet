package fr.utc.sr04.projet.message;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jconnect.core.message.AbstractContentMessage;

import fr.utc.sr04.projet.model.EventVersion;

public class EventVersionContentMessage extends AbstractContentMessage {

	private static final String TAG_EVENTS= "events";
	private ArrayList<EventVersion> events;

	public EventVersionContentMessage() {
		events = new ArrayList<EventVersion>();
	}

	public void addVersion(EventVersion r) {
		events.add(r);
	}

	public void addVersion(List<EventVersion> version) {
		events.addAll(version);
	}

	public ArrayList<EventVersion> getVersion() {
		return events;
	}

	@Override
	protected JsonObject exportFields(JsonObject json) {
		JsonArray array = new JsonArray();
		for (EventVersion e : events) {
			array.add(e.toJson());
		}
		json.add(TAG_EVENTS, array);
		return json;
	}

	@Override
	protected void importFields(JsonObject json) {
		if (json.has(TAG_EVENTS)) {
			JsonArray array = json.get(TAG_EVENTS).getAsJsonArray();
			for (JsonElement jsonElement : array) {
				addVersion(new EventVersion((JsonObject) jsonElement));
			}
		}

	}

}
