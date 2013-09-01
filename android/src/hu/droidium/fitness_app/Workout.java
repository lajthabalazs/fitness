package hu.droidium.fitness_app;

import java.util.List;

public abstract class Workout {
	
	public abstract List<List<Exercise>> getExercises();
	
	public final int getMaxRep(){
		int maxRep = 0;
		for(List<Exercise> exercises : getExercises()) {
			for (Exercise exercise : exercises) {
				maxRep = Math.max(maxRep, exercise.reps);
			}
		}
		return maxRep;
	}
	
	public final int getNumberOfBlocks(){
		return getExercises().size();
	}
	
	public final int getNumberOfExercises() {
		int total = 0;
		for(List<Exercise> exercises : getExercises()) {
			total += exercises.size();
		}
		return total;
	}
}