package fr.utc.sr04.projet.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import com.jconnect.core.event.PeerEventListener;
import com.jconnect.core.peergroup.peer.PeerEvent;
import com.jconnect.impl.peergroup.NetPeerGroup;

import fr.utc.sr04.projet.data.DataBaseManager;
import fr.utc.sr04.projet.model.Event;
import fr.utc.sr04.projet.model.EventsStack;
import fr.utc.sr04.projet.model.InfoID;
import fr.utc.sr04.projet.service.EventSyncService;

public class MainController implements Initializable, PeerEventListener {
	@FXML
	public VBox connectedPeerList;
	@FXML
	public VBox informationsList;

	private ObservableList<String> connectedPeer;
	private ObservableList<EventsStack> informations;
	private NetPeerGroup pg;
	public DataBaseManager bdd;
	private EventSyncService eventSyncService;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		iniConnectedPeerList();
		iniInformationList();
	}

	private void iniInformationList() {
		informations = FXCollections.observableArrayList();
		final ListView<EventsStack> listView = new ListView<EventsStack>();
		listView.setItems(informations);
		listView.setCellFactory(new Callback<ListView<EventsStack>, ListCell<EventsStack>>() {
			@Override
			public ListCell<EventsStack> call(ListView<EventsStack> list) {
				return new InformationListCell(MainController.this);
			}
		});
		informationsList.getChildren().add(listView);

	}

	private void iniConnectedPeerList() {
		connectedPeer = FXCollections.observableArrayList();
		final ListView<String> listView = new ListView<String>();
		listView.setItems(connectedPeer);
		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		connectedPeerList.getChildren().add(listView);
	}

	public void populate() {
		
		
		informations.add(new EventsStack());
		EventsStack events = bdd.loadEventsStack("*", -1, "*");
		for (int i = events.getEvents().size()-1; i>=0; i--) {
			onNewEvent(events.getEvents().get(i));
		}
	}

	public void createNewEvent(EventsStack info, String text) {

		Event e = null;
		if (info.getEvents().size() > 0) {
			e = new Event(info.getEvents().get(0).id);
			e.index = info.getEvents().get(0).index+1;
		} else {
			e = new Event(InfoID.generate().toString());
			e.index = 0;
		}

		e.owner = pg.getPeerID().toString();
		e.time = System.currentTimeMillis();
		e.value = text;
		info.addEvent(e);
		bdd.saveEvent(e);
		if (info.getEvents().size() == 1) {
			informations.add(new EventsStack());
		} else {
			int index = informations.indexOf(info);
			informations.remove(index);
			informations.add(index, info);
		}

	}

	public void setPeerGroup(NetPeerGroup peerGroup) {
		this.pg = peerGroup;
		eventSyncService = (EventSyncService) peerGroup.getService(EventSyncService.class);
		eventSyncService.setController(this);
		peerGroup.registerPeerEventListener(this);

	}

	public void setBDD(DataBaseManager bdd) {
		this.bdd = bdd;
	}

	public void onNewEvent(final Event e) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for (int i =0; i< informations.size(); i++) {
					EventsStack info = informations.get(i);
					if (info.getEvents().size() > 0) {
						if (info.getEvents().get(0).id.equals(e.id)) {
							info.addEvent(e);
							int index = informations.indexOf(info);
							informations.remove(index);
							informations.add(index, info);
							return;
						}
					}else{
						info.addEvent(e);
						informations.add(new EventsStack());
						
						return;
					}
				}
			}
		});
	}

	@Override
	public void onPeerEvent(final PeerEvent peerEvent) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				switch (peerEvent.getEvent()) {

				case CONNECT:
					connectedPeer.add(peerEvent.getPeerId().toString());

					break;
				case DISCONNECT:
					connectedPeer.remove(peerEvent.getPeerId().toString());
					break;
				case NEW_ROUTE:
					break;
				default:
					break;
				}
			}
		});

	}
}
