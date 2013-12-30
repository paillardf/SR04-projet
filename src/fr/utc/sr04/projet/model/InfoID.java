package fr.utc.sr04.projet.model;

import java.util.UUID;

import com.jconnect.util.uuid.AbstractUUID;

public class InfoID extends AbstractUUID {
	
public static final String prefix="info";
	

	@Override
	public String getPrefix() {
		return prefix;
	}

	public static InfoID generate(){
		
		return new InfoID(AbstractUUID.generateUUID());
	}

	public InfoID(UUID uuid)
	{
		super(uuid);
	}
	
	
	public InfoID(String uuid)
	{
		super(uuid);
		if(!getPrefix().equals(prefix))
			throw new IllegalArgumentException("Invalid prefix : "+getPrefix()+" instead of "+prefix);
		
	}

	

}
