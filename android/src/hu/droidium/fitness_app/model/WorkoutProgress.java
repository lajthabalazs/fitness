package hu.droidium.fitness_app.model;

import java.util.List;

public abstract class WorkoutProgress {
	
	public WorkoutProgress(){
	}
	
	public boolean exerciseDone(int blockIndex, int exerciseIndex, int reps, long durationSecs, Workout workout, long date) {
		if (isDone()) {
			throw new IllegalArgumentException("Workout already finished.");
		}
		int[] actual = getActualExercise();
		if ((blockIndex != actual[0]) || (exerciseIndex != actual[1])) {
			throw new IllegalArgumentException("Shold be block " + actual[0] + " exercise " + actual[1]);
		}		
		// Saves reps to temporary storage
		saveReps(blockIndex, exerciseIndex, reps);
		List<Block> blocks = workout.getBlocks();
		if (exerciseIndex == blocks.get(blockIndex).getExercises().size() - 1) {
			if (blockIndex == blocks.size() - 1) {
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