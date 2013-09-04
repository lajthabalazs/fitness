package hu.droidium.fitness_app.simple_model;

import hu.droidium.fitness_app.model.Block;
import hu.droidium.fitness_app.model.Exercise;
import hu.droidium.fitness_app.model.Workout;

import java.util.ArrayList;
import java.util.List;

public class SimpleWorkout extends Workout {
	
	@Override
	public List<Block> getBlocks() {
		ArrayList<Block> workout = new ArrayList<Block>();
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		SimpleExerciseType e1 = new SimpleExerciseType("Pushup", "reps");
		SimpleExerciseType e2 = new SimpleExerciseType("Situp", "reps");
		SimpleExerciseType e3 = new SimpleExerciseType("Pullup", "reps");
		exercises.add(new SimpleExercise(e1, 10, 60, 30));
		exercises.add(new SimpleExercise(e2, 15, 60, 30));
		exercises.add(new SimpleExercise(e3, 12, 60, 60));
		Block firstBlock = new SimpleBlock("First block", exercises);
		
		exercises = new ArrayList<Exercise>();
		exercises.add(new SimpleExercise(e1, 12, 60, 30));
		exercises.add(new SimpleExercise(e2, 18, 60, 30));
		exercises.add(new SimpleExercise(e3, 15, 60, 60));
		Block secondBlock = new SimpleBlock("Second block", exercises);
		
		exercises = new ArrayList<Exercise>();
		exercises.add(new SimpleExercise(e1, 5, 30, 0));
		exercises.add(new SimpleExercise(e2, 7, 30, 0));
		exercises.add(new SimpleExercise(e3, 6, 30, 60));
		Block thirdBlock = new SimpleBlock("Third block", exercises);
		
		exercises = new ArrayList<Exercise>();
		exercises.add(new SimpleExercise(e1, 10, 60, 30));
		exercises.add(new SimpleExercise(e2, 15, 60, 30));
		exercises.add(new SimpleExercise(e3, 12, 60, 60));
		Block fourthBlock = new SimpleBlock("Fourth block", exercises);
		
		workout.add(firstBlock);
		workout.add(secondBlock);
		workout.add(thirdBlock);
		workout.add(fourthBlock);
		return workout;
	}
}