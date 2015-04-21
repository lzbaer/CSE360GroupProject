package patient_backend;

import java.util.Vector;


public class Patient extends User {
	
	private String name;
	private String password;
	private int patientID;
	//private Doctor myDoc;
	private Vector <Patient_Record> pr;
	
	
	
	public Patient(String n, String p, int p_id, int d_id)
	{
		super(n,p);
		patientID = p_id;
		//myDoc = findDoctor(d_id);
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