package hu.droidium.fitness_app;

public class Exercise {
	public final int type;
	public final int reps;
	public final int targetSecs;
	public final int breakSecs;
	
	public Exercise(int type, int reps, int targetSecs, int breakSecs){
		this.type = type;
		this.reps = reps;
		this.targetSecs = targetSecs;
		this.breakSecs = breakSecs;
	}
}
