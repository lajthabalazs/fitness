package hu.droidium.fitness_app;

import java.util.ArrayList;
import java.util.List;

public class DummyWorkout extends Workout {
	
	public List<List<Exercise>> getExercises() {
		ArrayList<List<Exercise>> training = new ArrayList<List<Exercise>>();
		ArrayList<Exercise> firstBlock = new ArrayList<Exercise>();
		firstBlock.add(new Exercise(0, 10, 60, 30));
		firstBlock.add(new Exercise(1, 15, 60, 30));
		firstBlock.add(new Exercise(2, 12, 60, 60));
		ArrayList<Exercise> secondBlock = new ArrayList<Exercise>();
		secondBlock.add(new Exercise(0, 12, 60, 30));
		secondBlock.add(new Exercise(1, 18, 60, 30));
		secondBlock.add(new Exercise(2, 15, 60, 60));
		ArrayList<Exercise> thirdBlock = new ArrayList<Exercise>();
		thirdBlock.add(new Exercise(0, 5, 30, 0));
		thirdBlock.add(new Exercise(1, 7, 30, 0));
		thirdBlock.add(new Exercise(2, 6, 30, 60));
		ArrayList<Exercise> fourthBlock = new ArrayList<Exercise>();
		fourthBlock.add(new Exercise(0, 10, 60, 30));
		fourthBlock.add(new Exercise(1, 15, 60, 30));
		fourthBlock.add(new Exercise(2, 12, 60, 60));
		training.add(firstBlock);
		training.add(secondBlock);
		training.add(thirdBlock);
		training.add(fourthBlock);
		return training;
	}
}