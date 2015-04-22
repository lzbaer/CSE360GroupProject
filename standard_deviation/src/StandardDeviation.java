import java.util.*;

@SuppressWarnings("unused")
public class StandardDeviation {
	double mean = 0;
	double stdDev = 0;
	double sum = 0;
	int num = 15;
	
	//CONSTRUCTOR
	public StandardDeviation() {
		
	}
	
	public double calcStdDev () {
		
		double[] listOfInts = new double[num];
		
		for (int i = 0; i < num; i++) {
			listOfInts[i] = (Math.random()*10);
		}
		
		// TO CALCULATE STANDARD DEVIATION
		mean = 0;
		sum = 0;
		
		for (int i = 0; i < num; i++) {
			sum += listOfInts[i];
		}
		mean = sum/num;
		
		sum = 0;
		
		for (int i = 0; i < num; i++) {
			sum += Math.pow(listOfInts[i]-mean,2);
		}
		double variance = sum/(num-1);
		
		stdDev = Math.sqrt(variance);
		//System.out.println("Standard Deviation: " + stdDev);
		return stdDev;
	}
	
	public double detectUrgency(int submission) {
		double standardDeviation = calcStdDev();
		// Grab stdDev from Patient in real program
		double meanDif = Math.abs(mean-submission); // The difference between a new submission and the current mean
		double numStdDevs = meanDif/standardDeviation; // The number of standard deviations away the new submission is from the mean
		
		//System.out.println("Mean: " + mean);
		return Math.pow(2,numStdDevs); // Returns 2^(number of standard deviations away)
	}
	/*
	public static void main (String[] args) {
		double[] stdDev = new double[100];
		double average = 0;
		for (int i = 0; i < 100; i++) {
			stdDev[i] = calcStdDev();
			average += stdDev[i];
		}
		
		average = average/100;
		System.out.println(average);
	}*/
}
