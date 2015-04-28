package patient_backend;

import java.util.*;

public class UrgencyCalculation {
	
	//variables for the past 15 Patient_Records
	double[] symptomMeans = new double[9];
	double[] symptomStdDev = new double[9];
	
	int pastRec = 15;
	//urgency rating for current Record
	double urgency;
	
	//urgency rating per symptom for current record
	double[] urgencySymptoms;
	//symptoms of current record
	double[] symptoms;
	//2D array of past 15 Symptom Ratings
	double[][] pastSymptomRatings;
	boolean urgent;
	
	
	//constructor 
	public UrgencyCalculation(double[] symptoms)
	{
		urgency = -1;
		urgent = false;
		pastSymptomRatings = new double[pastRec][9];
		//initialize symptom urgency ratings
		urgencySymptoms = new double[9];
		//save symptom ratings for current record
		for(int i = 0; i < 9; i++)
		{
			this.symptoms[i] = symptoms[i];
		}
	}
	
	// calculate standard deviation per symptom
	// parameter: index of symptom to be analyzed
	// return: void, saves past symptom mean and std dev to arrays
	private void calcStdDevPerSymptom(int indexSymptom) {

		for (int i = 0; i < pastRec; i++) {
			for(int j = 0; j < 9; j++)
			{
				//fill 2-D array with past record symptom ratings
				//pastSymptomRatings[i][j] = 
			}
		}

		// TO CALCULATE STANDARD DEVIATION
		double mean = 0;
		double sum = 0;

		for (int i = 0; i < pastRec; i++) {
			sum += pastSymptomRatings[i][indexSymptom];
		}
		
		mean = sum / pastRec;
		
		//save past submissions mean in array
		symptomMeans[indexSymptom] = mean;
		
		sum = 0;

		for (int i = 0; i < pastRec; i++) {
			sum += Math.pow(pastSymptomRatings[i][indexSymptom] - mean, 2);
		}
		double variance = sum / (pastRec - 1);

		double stdDev = Math.sqrt(variance);
		symptomStdDev[indexSymptom] = stdDev;

	}

	public void detectUrgencyPerRecord() {
		urgent = false;
		//set array for past standard deviations and means
		for (int i = 0; i < 9; i++)
		{
			calcStdDevPerSymptom(i);
		}
		
		//find urgency rating per symptom for current record
		for(int i = 0; i < 9; i++)
		{
			// The difference between a new submission and the current mean
			double meanDif = Math.abs(symptomMeans[i] - symptoms[i]);
			// The number of standard deviations away the new submission is from the mean
			double numStdDevs = meanDif/(symptomStdDev[i]);
			urgencySymptoms[i] = Math.pow(2, numStdDevs); 
			
		}
		
		//check if standard deviation is too high for any one symptom
		int thresholdS = 15;
		int thresholdF = thresholdS * 9;
		for (int i = 0; i < 9; i++)
		{
			
			if (urgencySymptoms[i] > thresholdS)
			{	
				urgent = true;
				urgency = urgencySymptoms[i];
			}
		}
		if (urgent == true)
			return;
		
		else {
			for (int i = 0; i < 9; i++)
			{
				urgency += urgencySymptoms[i];  
			}
			
			urgency = urgency/9;
			if (urgency > thresholdF)
			{
				urgent = true;
			}
			return;
		}
		
	}
	
	public double getUrgencyScore()
	{
		return urgency;
	}
	
	public boolean isUrgent()
	{
		return urgent;
	}
}