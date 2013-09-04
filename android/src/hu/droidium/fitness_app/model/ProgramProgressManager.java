package hu.droidium.fitness_app.model;

import java.util.List;

public interface ProgramProgressManager {
	public List<WorkoutProgressManager> getProgress();
	public int getNextWorkoutIndex();
	public void workoutDone(WorkoutProgressManager progress);
}
