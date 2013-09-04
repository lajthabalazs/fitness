package hu.droidium.fitness_app.simple_model;

import hu.droidium.fitness_app.model.Workout;
import hu.droidium.fitness_app.model.WorkoutProgressManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SimpleSharedPrefsWorkoutProgress extends WorkoutProgressManager {
	private static final String WORKOUT_PROGRESS_STORAGE = "Workout progress storage";
	private static final String ACTUAL_BLOCK_SUFFIX = "_actual_block";
	private static final String ACTUAL_EXERCISE_SUFFIX = "_actual_exercise";
	private static final String FINISH_DATE_SUFFIX = "_finish";
	private static final String SEPARATOR = " ";
	private SharedPreferences prefs;
	
	private Workout workout;
	
	private Context context;
	private long eventId;


	public SimpleSharedPrefsWorkoutProgress(long eventId, Workout workout, Context context){
		this.eventId = eventId;
		this.context = context;
		this.workout = workout;
		prefs = context.getSharedPreferences(WORKOUT_PROGRESS_STORAGE, Context.MODE_PRIVATE);
	}
	
	public int[] getActualExerciseIndex() {
		int actualBlock = prefs.getInt(eventId + ACTUAL_BLOCK_SUFFIX, 0);
		int actualExercise = prefs.getInt(eventId + ACTUAL_EXERCISE_SUFFIX, 0);
		return new int[] {actualBlock, actualExercise};
	}

	@Override
	protected void actualExerciseChanged(int blockIndex, int exerciseIndex) {
		SharedPreferences prefs = context.getSharedPreferences(WORKOUT_PROGRESS_STORAGE, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt(eventId + ACTUAL_BLOCK_SUFFIX, blockIndex);
		editor.putInt(eventId + ACTUAL_EXERCISE_SUFFIX, exerciseIndex);
		editor.commit();
	}

	@Override
	protected void saveReps(int actualBlock, int actualExercise, int reps) {
		Editor editor = prefs.edit();
		// Saves reps to temporary storage
		editor.putInt(eventId + SEPARATOR + actualBlock + SEPARATOR + actualExercise, reps);
		editor.commit();
	}

	@Override
	protected void setFinishDate(long date) {
		Editor editor = prefs.edit();
		editor.putLong(eventId + FINISH_DATE_SUFFIX, date);
		editor.commit();
	}

	@Override
	public long getFinishDate() {
		return prefs.getLong(eventId + FINISH_DATE_SUFFIX, -1);
	}

	@Override
	public Workout getWorkout() {
		return workout;
	}

}