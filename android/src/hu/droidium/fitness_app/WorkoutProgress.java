package hu.droidium.fitness_app;

import java.util.List;

import android.content.Context;

public abstract class WorkoutProgress {
	
	protected Context context;
	protected long eventId;
	protected Workout workout;
	
	public WorkoutProgress(long eventId, Workout workout, Context context){
		this.eventId = eventId;
		this.workout = workout;
		this.context = context;
	}
	
	public boolean exerciseDone(int blockIndex, int exerciseIndex, int reps, int durationSecs, long date) {
		if (isDone()) {
			throw new IllegalArgumentException("Workout already finished.");
		}
		int[] actual = getActualExercise();
		if ((blockIndex != actual[0]) || (exerciseIndex != actual[1])) {
			throw new IllegalArgumentException("Shold be block " + actual[0] + " exercise " + actual[1]);
		}		
		// Saves reps to temporary storage
		saveReps(blockIndex, exerciseIndex, reps);
		List<List<Exercise>> exercises = workout.getExercises();
		if (exerciseIndex == exercises.get(blockIndex).size() - 1) {
			if (blockIndex == exercises.size() - 1) {
				done();
				return true;
			} else {
				actualExerciseChanged(blockIndex + 1, 0);
				return false;
			}
		} else {
			actualExerciseChanged(blockIndex, exerciseIndex + 1);
			return false;
		}
	}
	
	protected abstract void actualExerciseChanged(int blockIndex, int exerciseIndex);
	protected abstract void saveReps(int blockIndex, int exerciseIndex, int reps);
	protected abstract void done();
	public abstract boolean isDone();
	public abstract int[] getActualExercise();
}