package fr.utc.sr04.projet.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {
	
	public static String calculateHash(File f) throws NoSuchAlgorithmException, IOException
	{
		String res = new String();
		if(f.isFile())
		{
			MessageDigest md = MessageDigest.getInstance("SHA1"); 
			DigestInputStream dis = new DigestInputStream(new FileInputStream(f), md);  
			byte[] dataBytes = new byte[1024];

			int nread = 0; 

			while ((nread = dis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			};

			byte[] mdbytes = md.digest();

			//convert the byte to hex format
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			res = sb.toString();
			dis.close();

		}
		else
			return null;
		return res;
	}

	public static boolean copy( File source, File destination ){ //Methode permettant la copie d'un fichier 
		boolean resultat = false; 
		if(source.getAbsolutePath().equals(destination.getAbsolutePath()))
			return true;
		// Declaration des flux 
		java.io.FileInputStream sourceFile=null; 
		java.io.FileOutputStream destinationFile=null; 
		try { 
			
			// Ouverture des flux 
			sourceFile = new java.io.FileInputStream(source); 
			
			// Création du fichier : 
			destination.createNewFile(); 
						
						
			destinationFile = new java.io.FileOutputStream(destination); 
			// Lecture par segment de 0.5Mo 
			byte buffer[]=new byte[512*1024]; 
			int nbLecture; 
			while( (nbLecture = sourceFile.read(buffer)) != -1 ) { 
				destinationFile.write(buffer, 0, nbLecture); 
			} 

			// Copie réussie 
			resultat = true; 
		} catch( java.io.FileNotFoundException f ) { 
		} catch( java.io.IOException e ) { 
		} finally { 
			// Quoi qu'il arrive, on ferme les flux 
			try { 
				sourceFile.close(); 
			} catch(Exception e) { } 
			try { 
				destinationFile.close(); 
			} catch(Exception e) { } 
		} 
		return( resultat ); 
	}
	
	/** Suppression d'un fichier
	 * Si dossier, il faut qu'il soit vide
	 * @param fileToRemove
	 * @return true si ok, false sinon
	 */
	public static boolean deleteFile( File fileToRemove ){ //Methode permettant la suppression d'un fichier 
		// TODO : que faire si pas la permission de supprimer ?
		if(fileToRemove.exists())
			return fileToRemove.delete();
		else
			return true;
	}
	
	

}
