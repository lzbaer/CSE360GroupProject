package patient_backend;

public class SimilarityCalculation {
	//find average of each symptom
	int numIllnesses = 20;
	double[][] previousSymptomRatings;
	double[] averageRatings;
	
	public void method(){
		for(int i=0;i<numIllnesses;i++){
			for(int j=0;j<9;j++){
				averageRatings[j]+= getPatientSymptomRating(j);
			}
		}
	}

	private double getPatientSymptomRating(int j) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
