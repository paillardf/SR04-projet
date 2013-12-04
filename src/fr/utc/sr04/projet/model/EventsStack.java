package fr.utc.sr04.projet.model;

import java.util.ArrayList;
import java.util.List;

public class EventsStack{

	
	private List<Event> events = new ArrayList<Event>();
	
	
	public EventsStack() {
	}

	
	public void addEvent(Event e){
		events.add(0, e);
	}
	
	public List<Event> getEvents() {
		return events;
	}
	
	
	
	
}
