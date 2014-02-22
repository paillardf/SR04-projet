package fr.utc.sr04.projet.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.jconnect.core.event.MessageEvent;
import com.jconnect.core.message.Message;
import com.jconnect.core.peergroup.AbstractPeerGroup;
import com.jconnect.impl.peergroup.service.Service;
import com.jconnect.util.uuid.PeerID;

import fr.utc.sr04.projet.data.FileUtils;
import fr.utc.sr04.projet.message.AskFileContentMessage;
import fr.utc.sr04.projet.model.Event;
import fr.utc.sr04.projet.model.InfoID;
import fr.utc.sr04.projet.ui.MainController;

public class ScanService extends Service {

	private MainController controller;

	public ScanService(AbstractPeerGroup group) {
		super(group);
	}

	@Override
	protected void onHandleMessageEvent(MessageEvent m) {

	}

	@Override
	public boolean messageEventMatcher(MessageEvent messageEvent) {

		return false;
	}

	@Override
	protected void onUpdade() {
		block(5000);

		File folder = new File(controller.bdd.getPath());
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				return fileName.endsWith(".txt");
			}
		};

		for (int i = 0; i < folder.listFiles(filter).length; i++) {
			File f = folder.listFiles(filter)[i];
			try {
				String hash = FileUtils.calculateHash(f);

				Event event = controller.bdd.getLastEventOfAFile(f.getName());
				if (event == null) {
					Event e = new Event(f.getName());
					e.index = 0;
					e.owner = getPeerGroup().getPeerID().toString();
					e.time = f.lastModified();
					e.value = hash;
					controller.bdd.saveEvent(e);
					controller.onNewEvent(e);
				} else if (f.lastModified() > event.time
						&& !event.value.equals(hash)) {
					Event e = new Event(f.getName());
					e.index = event.index + 1;
					e.owner = getPeerGroup().getPeerID().toString();
					e.time = f.lastModified();
					e.value = hash;
					controller.bdd.saveEvent(e);
					controller.onNewEvent(e);
				}

			} catch (NoSuchAlgorithmException | IOException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < controller.informations.size() - 1; i++) {
			Event e = controller.informations
					.get(i)
					.getEvents()
					.get(0);
			if (!(e.id.indexOf(InfoID.prefix) > 0)) {
				if (!e.owner.equals(group.getPeerID().toString())) {
					File f = new File(
							controller.bdd.getPath()
									+ File.separator
									+ e.id);
					try {
						
						if (!f.exists()
								|| !e.value.equals(FileUtils.calculateHash(f))) {
							System.out.println("demande de fichieré");
							AskFileContentMessage askFile = new AskFileContentMessage();
							askFile.setEvent(e);
							ArrayList<PeerID> receiver = new ArrayList<PeerID>();
							receiver.add(new PeerID(e.owner));
							sendTCPMessage(new Message(group, askFile),
									receiver);

						}
					} catch (NoSuchAlgorithmException | IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		}

	}

	public void setController(MainController mainController) {
		controller = mainController;

	}

}
