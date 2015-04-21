package patient_backend;

public class Patient_Record {

	private int[] symptomRate = new int[9];
	private int date;
	private double urgency;
	
	public Patient_Record(int[] sr, int date)
	{
		for (int i = 0; i < 9; i++)
		{
			symptomRate[i] = sr[i];
		}
		
		this.date = date;
		urgency = -1;
	}
	
	public boolean setUrgency()
	{
		if (urgency != -1)
			return false;
		else
		{
			//determine standard values for all patient records
			//determine standard deviation of record, then set urgency as percentile
			//TO DO
			//TO BE COMPLETED
			
			return true;
		}
		
	}
	
	
}