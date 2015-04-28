
@SuppressWarnings("unused")
public class PatientList{

	public string[] returnUrgencyList(int doctorID) {
		// SEARCH DATABASE FOR DOCTOR ID and then do doctor.getPatients()

		//search for patients that have same doctor ID
		PFQuery *getRelevantRows=[PFQuery queryWithClassName:@"Data"]; //table name
    	[getRelevantRows whereKey:@"text" containsString:@"Car"];
    	//get most recent patient submission per patient
    	//of RelevantRows, search for most recent dates
    	//get urgency rating of most recent patient submission for each patient
		ParseQuery query=ParseQuery.getQuery(<Class(aka table name)>);
		//store in array
		//sort array
		//return array
	}
	public string[] returnRecencyList(int doctorID){
		//search for patients that have same doctor ID
		//get date of these patient submissions
		//find most recent submission for each patient
		//store in array
		//sort array
		//return array
	}

	public static void displayPatientInfo (int patientID) {
		// patientID.getRecord();
	}

}
