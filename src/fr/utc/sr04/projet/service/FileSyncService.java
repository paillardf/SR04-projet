package fr.utc.sr04.projet.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.jconnect.core.event.MessageEvent;
import com.jconnect.core.message.Message;
import com.jconnect.core.peergroup.AbstractPeerGroup;
import com.jconnect.impl.peergroup.service.Service;
import com.jconnect.util.uuid.PeerID;

import fr.utc.sr04.projet.data.FileUtils;
import fr.utc.sr04.projet.message.AskFileContentMessage;
import fr.utc.sr04.projet.message.FileContentMessage;
import fr.utc.sr04.projet.ui.MainController;

public class FileSyncService extends Service {

	private MainController controller;

	public FileSyncService(AbstractPeerGroup group) {
		super(group);
	}

	@Override
	protected void onHandleMessageEvent(MessageEvent m) {
		if (m.getMessage().getContent() instanceof FileContentMessage) {
			FileContentMessage content = (FileContentMessage) m.getMessage()
					.getContent();

			File f = new File(controller.bdd.getPath() + File.separator
					+ content.getEvent().id);
			if (f.exists()&&f.lastModified() > content.getEvent().time) {
				return; // ignore
			}

			try {
				f.createNewFile();
				FileOutputStream output = new FileOutputStream(f);
				output.write(content.getByte());
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (m.getMessage().getContent() instanceof AskFileContentMessage) {
			AskFileContentMessage content = (AskFileContentMessage) m
					.getMessage().getContent();

			File f = new File(controller.bdd.getPath() + File.separator
					+ content.getEvent().id);
			
			try {
				
				if (f.exists()&&FileUtils.calculateHash(f).equals(content.getEvent().value)) {
					
					byte[] data = Files.readAllBytes(Paths.get(f.toURI()));
					FileContentMessage fileMessage = new FileContentMessage();
					fileMessage.setByte(data);
					fileMessage.setEvent(content.getEvent());
					ArrayList<PeerID> receiver = new ArrayList<PeerID>();
					receiver.add(m.getMessage().getPeer());
					sendTCPMessage(new Message(group,fileMessage ), receiver);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public boolean messageEventMatcher(MessageEvent messageEvent) {

		return messageEvent.getState().equals(
				MessageEvent.State.MESSAGE_RECEIVED)
				&& ((messageEvent.getMessage().getContent() instanceof FileContentMessage) || messageEvent
						.getMessage().getContent() instanceof AskFileContentMessage);
	}

	@Override
	protected void onUpdade() {
		block();
	}

	public void setController(MainController mainController) {
		controller = mainController;

	}

}
