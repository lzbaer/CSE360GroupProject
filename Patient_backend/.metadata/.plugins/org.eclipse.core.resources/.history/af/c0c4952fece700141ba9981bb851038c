package patient_backend;

import java.util.Vector;


public class Patient extends User {
	
	private String name;
	private String password;
	private int patientID;
	private Vector <Patient_Record> pr;
	
	
	
	public Patient(String n, String p, int id)
	{
		super(n,p);
		patientID = id;
		pr = new Vector <Patient_Record> (1);
	}
	
	public Vector<Patient_Record> getRecord()
	{
		return pr;
	}
	
	public boolean addRecord(Patient_Record newRec)
	{
		boolean added = pr.add(newRec);
		return added;
	}

}
