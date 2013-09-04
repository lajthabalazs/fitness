package hu.droidium.fitness_app.model;

public interface Exercise {
	public ExerciseType getType();
	public int getReps();
	public int getTargetSecs();
	public int getBreakSecs();
}
