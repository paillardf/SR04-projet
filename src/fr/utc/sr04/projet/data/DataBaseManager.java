package fr.utc.sr04.projet.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import fr.utc.sr04.projet.model.Event;
import fr.utc.sr04.projet.model.EventsStack;

public class DataBaseManager extends DbliteConnection {



	private static String DBEVENTSPATH = "dblite.db";
	private static final String DBEVENTSTABLE = "events";

	private static final String DATEFIELD = "date";
	private static final String OWNERFIELD = "owner";
	private static final String UUIDFIELD = "uuid";
	private static final String VALUEFIELD = "value";
	private static final String NUMERICFIELD = "numeric";

	private static final int VERSION = 1;

	public DataBaseManager(String prefPath) {
		super(prefPath + DBEVENTSPATH, VERSION);

	}

	@Override
	protected void onCreate() throws SQLException {

		update("create table " + DBEVENTSTABLE + " " + "(" + DATEFIELD
				+ " numeric, " + NUMERICFIELD + " integer," + VALUEFIELD
				+ " text," + OWNERFIELD + " text, " + "PRIMARY KEY ("
				+ DATEFIELD + "," + UUIDFIELD + "," + OWNERFIELD + "));");

	}

	@Override
	protected void onUpdate() throws SQLException {
	}

	@Override
	protected void onDelete() throws SQLException {
	}

	public Event getLastEventOfAFile(String infoID) {
		Event res = null;
		try {
			ResultSet rs = query("select e." + DATEFIELD + ", e." + OWNERFIELD
					+ ",e." + NUMERICFIELD + ",e." + VALUEFIELD + " from "
					+ DBEVENTSTABLE + " e" + " where " + UUIDFIELD + "=\""
					+ infoID + "\"" + " and " + DATEFIELD + " = (select max("
					+ DATEFIELD + ") from " + DBEVENTSTABLE + " where "
					+ UUIDFIELD + "=\"" + infoID + "\"");
			while (rs.next()) {
				res = new Event(infoID);
				res.owner = rs.getString(OWNERFIELD);
				res.time = rs.getLong(DATEFIELD);
				res.index = rs.getInt(NUMERICFIELD);
				res.value = rs.getString(VALUEFIELD);

			}

		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		return res;

	}

	public void saveEvent(Event e) {
		try {
			update("Insert into " + DBEVENTSTABLE + " (" + NUMERICFIELD + ","
					+ OWNERFIELD + "," + DATEFIELD + "," + VALUEFIELD + ","
					+ UUIDFIELD + ") " + "VALUES(\"" + e.index + "\",\""
					+ e.owner + "\",\"" + e.time + "\",\"" + e.value + "\","
					+ e.id + ")");
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}

	}

	/**
	 * Obtient la liste des �v�nements d\"un Shared Folder donn� depuis
	 * une date donn�e et pour un propri�taire donn�
	 * 
	 * @param sharedFolderUID
	 *            : UID du Shared Folder dont on veut r�cup�rer les
	 *            �v�nements. Joker possible : "*"
	 * @param date
	 *            : date (en ms depuis l\"epoch) � partir de laquelle on veut
	 *            r�cup�rer les �v�nements. Joker possible : -1
	 * @param owner
	 *            : pour ne r�cup�rer que les events dont
	 *            l\""actionneur" est "owner". Joker possible : "*"
	 * @return EventsStack
	 */
	public EventsStack loadEventsStack(String infoID, long date, String owner) {
		EventsStack res = new EventsStack();
		try {
			String sqlQuery = "select e." + DATEFIELD + ",e." + VALUEFIELD
					+ ",e." + NUMERICFIELD + ",e." + OWNERFIELD + " from "
					+ DBEVENTSTABLE + " e" + " where 1 ";
			if (!infoID.equals("*"))
				sqlQuery += " and " + NUMERICFIELD + "=\"" + infoID + "\"";
			if (!owner.equals("*"))
				sqlQuery += " and " + OWNERFIELD + "=\"" + owner + "\"";
			if (date != -1)
				sqlQuery += " and " + DATEFIELD + " > " + date;

			ResultSet rs = query(sqlQuery);
			while (rs.next()) {
				Event e = new Event(rs.getString(UUIDFIELD));
				e.time = rs.getLong(DATEFIELD);
				e.index = rs.getInt(NUMERICFIELD);
				e.owner = rs.getString(OWNERFIELD);
				e.value = rs.getString(VALUEFIELD);

				res.addEvent(e);
			}

		} catch (SQLException ex) {
			// if the error message is "out of memory",
			// it probably means no database file is found

			System.err.println(ex.getMessage());
		}
		return res;
	}

}
