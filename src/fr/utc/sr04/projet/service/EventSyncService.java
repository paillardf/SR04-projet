package fr.utc.sr04.projet.service;

import java.util.ArrayList;
import java.util.List;

import com.jconnect.core.event.MessageEvent;
import com.jconnect.core.message.Message;
import com.jconnect.core.peergroup.AbstractPeerGroup;
import com.jconnect.impl.peergroup.service.Service;
import com.jconnect.util.uuid.PeerID;

import fr.utc.sr04.projet.message.EventContentMessage;
import fr.utc.sr04.projet.message.EventVersionContentMessage;
import fr.utc.sr04.projet.model.Event;
import fr.utc.sr04.projet.model.EventVersion;
import fr.utc.sr04.projet.ui.MainController;

public class EventSyncService extends Service {

	private MainController controller;

	public EventSyncService(AbstractPeerGroup group) {
		super(group);
	}

	@Override
	protected void onHandleMessageEvent(MessageEvent m) {
		if (m.getMessage().getContent() instanceof EventContentMessage) {
			EventContentMessage content = (EventContentMessage) m.getMessage()
					.getContent();
			for (int i =  content.getEvents().size()-1; i >=0; i--) {
				controller.bdd.saveEvent(content.getEvents().get(i));
				controller.onNewEvent(content.getEvents().get(i));
				
			}

		} else if (m.getMessage().getContent() instanceof EventVersionContentMessage) {
			EventVersionContentMessage content = (EventVersionContentMessage) m
					.getMessage().getContent();
			List<String> owners = controller.bdd.loadOwners();

			List<Event> events = new ArrayList<Event>();
			for (int i = 0; i < content.getVersion().size(); i++) {
				EventVersion v = content.getVersion().get(i);
				events.addAll(controller.bdd.loadEventsStack("*", v.time,
						v.owner).getEvents());
				owners.remove(v.owner);
			}
			for (int i = 0; i < owners.size(); i++) {
				events.addAll(controller.bdd.loadEventsStack("*", -1,
						owners.get(0)).getEvents());
			}
			if (events.size() > 0) {
				EventContentMessage mContent = new EventContentMessage();
				mContent.addEvents(events);

				ArrayList<PeerID> dest = new ArrayList<PeerID>();
				dest.add(m.getMessage().getPeer());
				sendTCPMessage(new Message(group, mContent), dest);
			}

		}
	}

	@Override
	public boolean messageEventMatcher(MessageEvent messageEvent) {
		
		return messageEvent.getState().equals(
				MessageEvent.State.MESSAGE_RECEIVED)
				&& ((messageEvent.getMessage().getContent() instanceof EventContentMessage) || messageEvent
						.getMessage().getContent() instanceof EventVersionContentMessage);
	}

	@Override
	protected void onUpdade() {
		block(5000);

		EventVersionContentMessage content = new EventVersionContentMessage();
		List<EventVersion> eVersion = controller.bdd.getLastEventsOffOwners();
		content.addVersion(eVersion);
		sendMulticastMessage(new Message(group, content));

	}

	public void setController(MainController mainController) {
		controller = mainController;

	}

}
