package fr.utc.sr04.projet.message;

import java.io.UnsupportedEncodingException;

import sun.misc.IOUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jconnect.core.message.AbstractContentMessage;

import fr.utc.sr04.projet.model.Event;

public class FileContentMessage extends AbstractContentMessage {

	private static final String TAG_FILE = "file";
	private static final String TAG_DATA = "data";
	private Event event;
	private byte[] data;
	

	public FileContentMessage() {
		
	}

	public void setEvent(Event e){
		this.event = e;
		
	}
	public void setByte(byte[] data2){
		this.data = data2;
	}
	

	@Override
	protected JsonObject exportFields(JsonObject json) {
		JsonArray array = new JsonArray();
		array.add(event.toJson());
		json.add(TAG_FILE, array);
		try {
			json.addProperty(TAG_DATA, new String(data, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		return json;
	}

	@Override
	protected void importFields(JsonObject json) {
		if (json.has(TAG_FILE)) {
			
			try {
				setByte(json.get(TAG_DATA).getAsString().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			JsonArray array = json.get(TAG_FILE).getAsJsonArray();
			for (JsonElement jsonElement : array) {
				setEvent((new Event((JsonObject) jsonElement)));
			}
		}

	}

	public Event getEvent() {
		return event;
		
	}
	public byte[] getByte() {
		return data;

	}

}
