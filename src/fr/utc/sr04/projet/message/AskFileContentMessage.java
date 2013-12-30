package fr.utc.sr04.projet.message;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jconnect.core.message.AbstractContentMessage;

import fr.utc.sr04.projet.model.Event;

public class AskFileContentMessage extends AbstractContentMessage {

	private static final String TAG_FILE = "file";
	private Event event;
	

	public AskFileContentMessage() {
		
	}

	public void setEvent(Event e){
		this.event = e;
		
	}
	

	@Override
	protected JsonObject exportFields(JsonObject json) {
		JsonArray array = new JsonArray();
		array.add(event.toJson());
		json.add(TAG_FILE, array);
		return json;
	}

	@Override
	protected void importFields(JsonObject json) {
		if (json.has(TAG_FILE)) {
			JsonArray array = json.get(TAG_FILE).getAsJsonArray();
			for (JsonElement jsonElement : array) {
				setEvent((new Event((JsonObject) jsonElement)));
			}
		}

	}

	public Event getEvent() {
		return event;
	}

}
