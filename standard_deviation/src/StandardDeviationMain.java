// THRESHOLD IS 13
public class StandardDeviationMain {
	
	public static void main (String[] args) {
		double[] urgency = new double[9];
		StandardDeviation stdDev = new StandardDeviation();
		double totalUrgencyRating = 0;
		
		for (int i = 0; i < 9; i++) {
			urgency[i] = stdDev.detectUrgency();
			totalUrgencyRating += urgency[i];
		}
		
		System.out.println("Urgency Rating: " + totalUrgencyRating);
	}
}
