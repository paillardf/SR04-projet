package fr.utc.sr04.projet.model;

import java.util.UUID;

import com.jconnect.util.uuid.AbstractUUID;

public class ConversationID extends AbstractUUID {
	
private static final String prefix="info";
	

	@Override
	public String getPrefix() {
		return prefix;
	}

	public static ConversationID generate(){
		
		return new ConversationID(AbstractUUID.generateUUID());
	}

	public ConversationID(UUID uuid)
	{
		super(uuid);
	}
	
	
	public ConversationID(String uuid)
	{
		super(uuid);
		if(!getPrefix().equals(prefix))
			throw new IllegalArgumentException("Invalid prefix : "+getPrefix()+" instead of "+prefix);
		
	}

	

}
