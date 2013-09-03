package hu.droidium.fitness_app.model;

import java.util.ArrayList;
import java.util.List;

public class DummyWorkout extends Workout {
	
	@Override
	public List<Block> getBlocks() {
		ArrayList<Block> workout = new ArrayList<Block>();
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		exercises.add(new SimpleExercise(0, 10, 60, 30));
		exercises.add(new SimpleExercise(1, 15, 60, 30));
		exercises.add(new SimpleExercise(2, 12, 60, 60));
		Block firstBlock = new SimpleBlock("First block", exercises);
		
		exercises = new ArrayList<Exercise>();
		exercises.add(new SimpleExercise(0, 12, 60, 30));
		exercises.add(new SimpleExercise(1, 18, 60, 30));
		exercises.add(new SimpleExercise(2, 15, 60, 60));
		Block secondBlock = new SimpleBlock("Second block", exercises);
		
		exercises = new ArrayList<Exercise>();
		exercises.add(new SimpleExercise(0, 5, 30, 0));
		exercises.add(new SimpleExercise(1, 7, 30, 0));
		exercises.add(new SimpleExercise(2, 6, 30, 60));
		Block thirdBlock = new SimpleBlock("Third block", exercises);
		
		exercises = new ArrayList<Exercise>();
		exercises.add(new SimpleExercise(0, 10, 60, 30));
		exercises.add(new SimpleExercise(1, 15, 60, 30));
		exercises.add(new SimpleExercise(2, 12, 60, 60));
		Block fourthBlock = new SimpleBlock("Fourth block", exercises);
		
		workout.add(firstBlock);
		workout.add(secondBlock);
		workout.add(thirdBlock);
		workout.add(fourthBlock);
		return workout;
	}
}