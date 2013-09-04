package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.model.Workout;
import hu.droidium.fitness_app.model.WorkoutProgressManager;

public class WorkoutProgressHelper extends WorkoutProgressManager {

	@Override
	protected void actualExerciseChanged(int blockIndex, int exerciseIndex) {
	}

	@Override
	protected void saveReps(int blockIndex, int exerciseIndex, int reps) {
	}

	@Override
	protected void setFinishDate(long date) {
	}

	@Override
	public int[] getActualExerciseIndex() {
		return null;
	}

	@Override
	public long getFinishDate() {
		return 0;
	}

	@Override
	public Workout getWorkout() {
		return null;
	}
}
