package fr.utc.sr04.projet.message;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jconnect.core.message.AbstractContentMessage;
import com.jconnect.core.model.RouteModel;

import fr.utc.sr04.projet.model.Event;

public class EventContentMessage extends AbstractContentMessage {

	private static final String TAG_EVENTS = "events";
	private ArrayList<Event> events;

	public EventContentMessage() {
		events = new ArrayList<Event>();
	}

	public void addEvents(Event r) {
		events.add(r);
	}

	public void addEvents(List<Event> peerRoute) {
		events.addAll(peerRoute);
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	@Override
	protected JsonObject exportFields(JsonObject json) {
		JsonArray array = new JsonArray();
		for (Event r : events) {
			array.add(r.toJson());
		}
		json.add(TAG_EVENTS, array);
		return json;
	}

	@Override
	protected void importFields(JsonObject json) {
		if (json.has(TAG_EVENTS)) {
			JsonArray array = json.get(TAG_EVENTS).getAsJsonArray();
			for (JsonElement jsonElement : array) {
				addEvents(new Event((JsonObject) jsonElement));
			}
		}

	}

}
