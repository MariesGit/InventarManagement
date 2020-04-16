package gui;

import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
import java.sql.*;

public class DB_Connection {

//	private static String myDBVerbindung = "jdbc:mysql://172.17.0.1:3306/D_InventarProjekt";
//	private static String myDBBenutzer = "root";
//	private static String myDBPassword = "example";
	
	private static String myDBVerbindung = "jdbc:mysql://127.0.0.1:3306/D_InventarProjekt";
	private static String myDBBenutzer = "root";
	private static String myDBPassword = "";
	private static int myDBInit = 0;
	
	public DB_Connection( ) {
		if(myDBInit == 0) {
			
		}
	}

	public String[][] getRaeumeListe() {

		ArrayList<Räume> rliste = new ArrayList<Räume>();
		String[][] result = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);

			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("SELECT * FROM T_Raeume");
			while (rs.next()) {
				rliste.add(new Räume(rs.getInt("raumNR"), rs.getString("funktion"), rs.getString("maximalePAnzahl")));
			}
			if (Maingui.Debug.getDebug() == gui.Maingui.Debug.ON) {
				System.out.println(rliste);
			}

			result = new String[rliste.size()][3];
			for (int i = 0; i < rliste.size(); i++) {
				result[i][0] = "" + rliste.get(i).getRaumNR();
				result[i][1] = rliste.get(i).getFunktion();
				result[i][2] = rliste.get(i).getMaximalePAnzahl();

			}

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

		return result;
	}

	public void refreshRäume(DefaultTableModel tablemodel) {

		ArrayList<Räume> rliste = new ArrayList<Räume>();
		tablemodel.setRowCount(0);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("SELECT * FROM T_Raeume");
			while (rs.next()) {
				rliste.add(new Räume(rs.getInt("raumNR"), rs.getString("funktion"), rs.getString("maximalePAnzahl")));
			}
			if (Maingui.Debug.getDebug() == gui.Maingui.Debug.ON) {
				System.out.println(rliste);
			}
			tablemodel.setRowCount(rliste.size());
			for (int i = 0; i < rliste.size(); i++) {
				tablemodel.setValueAt(rliste.get(i).getRaumNR() + "", i, 0);
				tablemodel.setValueAt(rliste.get(i).getFunktion() + "", i, 1);
				tablemodel.setValueAt(rliste.get(i).getMaximalePAnzahl() + "", i, 2);

			}

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

	}

	public void searchRäume(DefaultTableModel tablemodel, int raumnummer, String funktion, int maximalePAnzahl) {

		ArrayList<Räume> rliste = new ArrayList<Räume>();
		tablemodel.setRowCount(0);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			ResultSet rs = null;
			if (raumnummer != 0) {
				rs = st.executeQuery("SELECT * FROM T_Raeume WHERE raumNR LIKE '" + raumnummer + "%'");
			} else if (!funktion.contains("-")) {
				rs = st.executeQuery("SELECT * FROM T_Raeume WHERE funktion LIKE '" + funktion + "%'");
			} else if (maximalePAnzahl != 0) {
				rs = st.executeQuery("SELECT * FROM T_Raeume WHERE maximalePAnzahl LIKE '" + maximalePAnzahl + "%'");

			}

			while (rs.next()) {
				rliste.add(new Räume(rs.getInt("raumNR"), rs.getString("funktion"), rs.getString("maximalePAnzahl")));
			}
			if (Maingui.Debug.getDebug() == gui.Maingui.Debug.ON) {
				System.out.println(rliste);
			}
			tablemodel.setRowCount(rliste.size());
			for (int i = 0; i < rliste.size(); i++) {
				tablemodel.setValueAt(rliste.get(i).getRaumNR() + "", i, 0);
				tablemodel.setValueAt(rliste.get(i).getFunktion() + "", i, 1);
				tablemodel.setValueAt(rliste.get(i).getMaximalePAnzahl() + "", i, 2);

			}

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

	}

	public void addRaum(int raumid, String funktion, int maxpanzahl) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			st.executeUpdate("INSERT INTO T_Raeume (raumNR, funktion, maximalePAnzahl) VALUES (" + raumid + ",'"
					+ funktion + "'," + maxpanzahl + ")");

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			if (ex.getMessage().contains("Duplicate entry")) {
				javax.swing.JOptionPane.showMessageDialog(null, "Es gibt schon einen Raum mit der Raumid " + raumid,
						"SQL Error(addRaum)", JOptionPane.ERROR_MESSAGE);
			} else {
				javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage() + "", "SQL Error(addRaum)",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public void editRaum(int raumid, int oldraumid, String funktion, int maxpanzahl) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			st.executeUpdate("UPDATE T_Raeume SET raumNR = '" + raumid + "' ,funktion = '" + funktion
					+ "' , maximalePAnzahl = " + maxpanzahl + " WHERE raumNR = '" + oldraumid + "'");

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			if (ex.getMessage().contains("Duplicate entry")) {
				javax.swing.JOptionPane.showMessageDialog(null, "Es gibt schon einen Raum mit der Raumid " + raumid,
						"SQL Error(editRaum)", JOptionPane.ERROR_MESSAGE);
			} else {
				javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage() + "", "SQL Error(editRaum)",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public void deleteRaum(int raumid) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			st.executeUpdate("DELETE FROM T_Raeume WHERE raumNR = '" + raumid + "'");

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage() + "", "SQL Error(deleteRaum)",
					JOptionPane.ERROR_MESSAGE);

		}

	}

	public void getSingelRaum(DefaultTableModel tablemodel, int id) {

		ArrayList<Räume> rliste = new ArrayList<Räume>();
		tablemodel.setRowCount(0);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("SELECT * FROM T_Raeume WHERE raumNR = '" + id + "'");
			while (rs.next()) {
				rliste.add(new Räume(rs.getInt("raumNR"), rs.getString("funktion"), rs.getString("maximalePAnzahl")));
			}

			if (Maingui.Debug.getDebug() == gui.Maingui.Debug.ON) {
				System.out.println(rliste);
			}
			tablemodel.setRowCount(rliste.size());
			for (int i = 0; i < rliste.size(); i++) {
				tablemodel.setValueAt(rliste.get(i).getRaumNR() + "", i, 0);
				tablemodel.setValueAt(rliste.get(i).getFunktion() + "", i, 1);
				tablemodel.setValueAt(rliste.get(i).getMaximalePAnzahl() + "", i, 2);

			}

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

	}

	// ======================================================================================================================================
	// END RÄUME

	public String[][] getMitarbeiterListe() {

		ArrayList<Mitarbeiter> rliste = new ArrayList<Mitarbeiter>();
		String[][] result = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("SELECT * FROM T_Mitarbeiter");
			while (rs.next()) {
				rliste.add(
						new Mitarbeiter(rs.getString("vorname"), rs.getString("nachname"), rs.getInt("mitarbeiterID")));
			}

			if (Maingui.Debug.getDebug() == gui.Maingui.Debug.ON) {
				System.out.println(rliste);
			}

			result = new String[rliste.size()][3];
			for (int i = 0; i < rliste.size(); i++) {
				result[i][0] = "" + rliste.get(i).getMitarbeiter_id();
				result[i][1] = rliste.get(i).getVorname();
				result[i][2] = rliste.get(i).getNachname();

			}

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

		return result;
	}

	public void addMitarbeiter(int mitarbeiterid, String vorname, String nachname) {

		ArrayList<Mitarbeiter> rliste = new ArrayList<Mitarbeiter>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			st.executeUpdate("INSERT INTO T_Mitarbeiter (mitarbeiterID, vorname, nachname) VALUES (" + mitarbeiterid
					+ ",'" + vorname + "','" + nachname + "')");

			ResultSet rs = st.executeQuery("SELECT * FROM T_Mitarbeiter");

			while (rs.next()) {
				rliste.add(
						new Mitarbeiter(rs.getString("vorname"), rs.getString("nachname"), rs.getInt("mitarbeiterID")));
			}

			System.out.println(rliste);

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			if (ex.getMessage().contains("Duplicate entry")) {
				javax.swing.JOptionPane.showMessageDialog(null,
						"Es gibt schon einen Mitarbeiter mit der Mitarbeiterid " + mitarbeiterid,
						"SQL Error(addMitarbeiter)", JOptionPane.ERROR_MESSAGE);
			} else {
				javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage() + "", "SQL Error(addMitarbeiter)",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public void editMitarbeiter(int mitarbeiterid, int oldmitarbeiterid, String vorname, String nachname) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			st.executeUpdate("UPDATE T_Mitarbeiter SET mitarbeiterID = '" + mitarbeiterid + "' ,vorname = '" + vorname
					+ "' , nachname = '" + nachname + "' WHERE mitarbeiterID = '" + oldmitarbeiterid + "'");

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			if (ex.getMessage().contains("Duplicate entry")) {
				javax.swing.JOptionPane.showMessageDialog(null,
						"Es gibt schon einen Mitarbeiter mit der Mitarbeiterid " + mitarbeiterid,
						"SQL Error(editMitarbeiter)", JOptionPane.ERROR_MESSAGE);
			} else {
				javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage() + "", "SQL Error(editMitarbeiter)",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public void deleteMitarbeiter(int mitarbeiterid) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			st.executeUpdate("DELETE FROM T_Mitarbeiter WHERE mitarbeiterID = '" + mitarbeiterid + "'");

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage() + "", "SQL Error(deleteMitarbeiter)",
					JOptionPane.ERROR_MESSAGE);

		}

	}

	public void getSingelMitarbeiter(DefaultTableModel tablemodel, int id) {

		ArrayList<Mitarbeiter> rliste = new ArrayList<Mitarbeiter>();
		tablemodel.setRowCount(0);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("SELECT * FROM T_Mitarbeiter WHERE mitarbeiterID = '" + id + "'");
			while (rs.next()) {
				rliste.add(
						new Mitarbeiter(rs.getString("vorname"), rs.getString("nachname"), rs.getInt("mitarbeiterID")));
			}
			if (Maingui.Debug.getDebug() == gui.Maingui.Debug.ON) {
				System.out.println(rliste);
			}
			tablemodel.setRowCount(rliste.size());
			for (int i = 0; i < rliste.size(); i++) {
				tablemodel.setValueAt(rliste.get(i).getMitarbeiter_id() + "", i, 0);
				tablemodel.setValueAt(rliste.get(i).getVorname() + "", i, 1);
				tablemodel.setValueAt(rliste.get(i).getNachname() + "", i, 2);

			}

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

	}

	public void searchMitarbeiter(DefaultTableModel tablemodel, int mitarbeiterid, String vorname, String nachname) {

		ArrayList<Mitarbeiter> rliste = new ArrayList<Mitarbeiter>();
		tablemodel.setRowCount(0);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			ResultSet rs = null;
			if (mitarbeiterid != 0) {
				rs = st.executeQuery("SELECT * FROM T_Mitarbeiter WHERE mitarbeiterID LIKE '" + mitarbeiterid + "%'");
			} else if (!vorname.contains("-")) {
				rs = st.executeQuery("SELECT * FROM T_Mitarbeiter WHERE vorname LIKE '" + vorname + "%'");
			} else if (!nachname.contains("-")) {
				rs = st.executeQuery("SELECT * FROM T_Mitarbeiter WHERE nachname LIKE '" + nachname + "%'");

			}

			while (rs.next()) {
				rliste.add(
						new Mitarbeiter(rs.getString("vorname"), rs.getString("nachname"), rs.getInt("mitarbeiterID")));
			}
			if (Maingui.Debug.getDebug() == gui.Maingui.Debug.ON) {
				System.out.println(rliste);
			}
			tablemodel.setRowCount(rliste.size());
			for (int i = 0; i < rliste.size(); i++) {
				tablemodel.setValueAt(rliste.get(i).getMitarbeiter_id() + "", i, 0);
				tablemodel.setValueAt(rliste.get(i).getVorname() + "", i, 1);
				tablemodel.setValueAt(rliste.get(i).getNachname() + "", i, 2);

			}

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

	}

	public void refreshMitarbeiter(DefaultTableModel tablemodel) {

		ArrayList<Mitarbeiter> rliste = new ArrayList<Mitarbeiter>();
		tablemodel.setRowCount(0);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(myDBVerbindung, myDBBenutzer, myDBPassword);
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("SELECT * FROM T_Mitarbeiter");
			while (rs.next()) {
				rliste.add(
						new Mitarbeiter(rs.getString("vorname"), rs.getString("nachname"), rs.getInt("mitarbeiterID")));
			}

			if (Maingui.Debug.getDebug() == gui.Maingui.Debug.ON) {
				System.out.println(rliste);
			}

			tablemodel.setRowCount(rliste.size());
			for (int i = 0; i < rliste.size(); i++) {
				tablemodel.setValueAt(rliste.get(i).getMitarbeiter_id() + "", i, 0);
				tablemodel.setValueAt(rliste.get(i).getVorname() + "", i, 1);
				tablemodel.setValueAt(rliste.get(i).getNachname() + "", i, 2);

			}

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

	}

	public  String getMyDBVerbindung() {
		return myDBVerbindung;
	}

	public void setMyDBVerbindung(String myDBVerbindung) {
		DB_Connection.myDBVerbindung = myDBVerbindung;
	}

	public  String getMyDBBenutzer() {
		return myDBBenutzer;
	}

	public  void setMyDBBenutzer(String myDBBenutzer) {
		DB_Connection.myDBBenutzer = myDBBenutzer;
	}

	public  String getMyDBPassword() {
		return myDBPassword;
	}

	public  void setMyDBPassword(String myDBPassword) {
		DB_Connection.myDBPassword = myDBPassword;
	}

	public  int getMyDBInit() {
		return myDBInit;
	}

	public  void setMyDBInit(String string) {
		DB_Connection.myDBInit = Integer.parseInt(string);
	}
}
