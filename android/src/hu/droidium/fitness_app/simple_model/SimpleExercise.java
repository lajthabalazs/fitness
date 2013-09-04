package hu.droidium.fitness_app.simple_model;

import hu.droidium.fitness_app.model.Exercise;
import hu.droidium.fitness_app.model.ExerciseType;

public class SimpleExercise implements Exercise{

	private ExerciseType type;
	private int reps;
	private int targetSecs;
	private int breakSecs;

	public SimpleExercise(ExerciseType type, int reps, int targetSecs, int breakSecs) {
		this.type = type;
		this.reps = reps;
		this.targetSecs = targetSecs;
		this.breakSecs = breakSecs;
	}
	
	@Override
	public ExerciseType getType() {
		return type;
	}

	@Override
	public int getReps() {
		return reps;
	}

	@Override
	public int getTargetSecs() {
		return targetSecs;
	}

	@Override
	public int getBreakSecs() {
		return breakSecs;
	}
}
