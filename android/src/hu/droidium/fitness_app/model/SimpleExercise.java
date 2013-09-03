package hu.droidium.fitness_app.model;

public class SimpleExercise implements Exercise{

	private int type;
	private int reps;
	private int targetSecs;
	private int breakSecs;

	public SimpleExercise(int type, int reps, int targetSecs, int breakSecs) {
		this.type = type;
		this.reps = reps;
		this.targetSecs = targetSecs;
		this.breakSecs = breakSecs;
	}
	
	@Override
	public int getType() {
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
