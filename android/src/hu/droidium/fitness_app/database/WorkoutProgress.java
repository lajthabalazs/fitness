package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class WorkoutProgress {
	
	private static final String TAG = "WorkoutProgress";
	@DatabaseField(generatedId=true)
	private long id;
	@DatabaseField
	private long startTime;
	@DatabaseField(foreign=true)
	private Workout workout;
	@DatabaseField(foreign=true)
	private ProgramProgress programProgress;
	@DatabaseField
	private int actualBlock;
	@DatabaseField
	private int actualExercise;
	@ForeignCollectionField
	private ForeignCollection<ExerciseProgress> doneExercises;
	@DatabaseField(defaultValue="-1")
	private long finishDate = -1;
	
	public WorkoutProgress() {}
	
	public WorkoutProgress(long startTime, Workout workout) {
		this.startTime = startTime;
		this.workout = workout;
		this.finishDate = -1;
	}
	
	public boolean refresh(DatabaseManager databaseManager) {
		return refresh(databaseManager, false);
	}

	public boolean refresh(DatabaseManager databaseManager, boolean forced) {
		if (workout == null || forced) {
			WorkoutProgress other = databaseManager.getWorkoutProgress(id);
			this.startTime = other.startTime;
			this.workout = other.workout;
			this.programProgress = other.programProgress;
			this.actualBlock = other.actualBlock;
			this.actualExercise = other.actualExercise;
			this.doneExercises = other.doneExercises;
			this.finishDate = other.finishDate;
			return true;
		} else {
			return false;
		}
	}

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public Workout getWorkout() {
		return workout;
	}

	public void setWorkout(Workout workout) {
		this.workout = workout;
	}

	public int getActualBlock() {
		return actualBlock;
	}

	public void setActualBlock(int actualBlock) {
		this.actualBlock = actualBlock;
	}

	public List<ExerciseProgress> getDoneExercises() {
		if (this.doneExercises == null) {
			return null;
		}
		ArrayList<ExerciseProgress> exerciseProgresses = new ArrayList<ExerciseProgress>();
		for (ExerciseProgress exercise : doneExercises) {
			exerciseProgresses.add(exercise);
		}
		return exerciseProgresses;
	}

	public void setDoneExercises(ForeignCollection<ExerciseProgress> doneExercises) {
		this.doneExercises = doneExercises;
	}

	public void setActualExercise(int actualExercise) {
		this.actualExercise = actualExercise;
	}
	
	public int getActualExercise(){
		return actualExercise;
	}
	
	public long getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(long finishDate) {
		this.finishDate = finishDate;
	}

	public ProgramProgress getProgramProgress() {
		return programProgress;
	}

	public void setProgramProgress(ProgramProgress programProgress) {
		this.programProgress = programProgress;
	}

	public void exerciseDone(ProgramProgress programProgress, Exercise exercise, int reps, long workoutTime, long date, DatabaseManager databaseManager) {
		ExerciseProgress exerciseProgress = new ExerciseProgress(this, exercise, reps, workoutTime, date);
		refresh(databaseManager);
		workout.refresh(databaseManager);
		databaseManager.addExerciseProgress(exerciseProgress);
		exercise.refresh(databaseManager);
		exercise.getBlock().refresh(databaseManager);
		int blockIndex = exercise.getBlock().getOrder();
		int blockCount = workout.getNumberOfBlocks(databaseManager);
		int exerciseCount = exercise.getBlock().getExerciseCount(databaseManager);
		int exerciseIndex = exercise.getOrder();
		Log.i(TAG, "Finished exercise " + exerciseIndex + "/" + exerciseCount + " of block " + blockIndex + "/" + blockCount);
		if (exerciseIndex == exerciseCount - 1) {
			if (blockIndex == blockCount - 1) {
				// Last exercise of last block
				setFinishDate(date);
				setActualBlock(-1);
				setActualExercise(-1);
				setProgramProgress(programProgress);
				databaseManager.updateWorkoutProgress(this);
				// Remove workout as actual workout
				programProgress.setActualWorkout(null);
				// Check if there are more workouts in the program
				if (programProgress.getRemainingWorkouts(databaseManager).size() == 0) {
					programProgress.setTerminationDate(date);
				}
				programProgress.setActualWorkout(null);
				databaseManager.updateProgress(programProgress);
			} else {
				this.actualBlock += 1;
				this.actualExercise = 0;
				databaseManager.updateWorkoutProgress(this);
			}
		} else {
			this.actualExercise += 1;
			databaseManager.updateWorkoutProgress(this);
			
		}
	}

	public Exercise getExercise(int actualBlockIndex, int actualExerciseIndex, DatabaseManager databaseManager) {
		refresh(databaseManager);
		workout.refresh(databaseManager);
		Block block = workout.getBlocks().get(actualBlockIndex);
		block.refresh(databaseManager);
		Exercise exercise = block.getExercises().get(actualExerciseIndex);
		return exercise;
	}
	
	/**
	 * Database intensive task. Counts the number of reps done for each exercise type
	 * @param databaseManager Database manager used to update database entities.
	 * @return A hashmap indexed with the exercise type id, containing total done reps of each type.
	 */
	public HashMap<String, Integer> getReps(DatabaseManager databaseManager) {
		HashMap<String, Integer> reps = new HashMap<String, Integer>();
		if (doneExercises == null) {
			doneExercises = databaseManager.getWorkoutProgress(id).doneExercises;
		}
		for (ExerciseProgress exerciseProgress : doneExercises) {
			exerciseProgress = databaseManager.getExerciseProgress(exerciseProgress.getId());
			Exercise exercise = databaseManager.getExercise(exerciseProgress.getExercise().getId());
			String exerciseTypeId = exercise.getType().getId();
			Integer savedReps = reps.get(exerciseTypeId);
			if (savedReps == null) {
				reps.put(exerciseTypeId, exerciseProgress.getDoneReps());
			} else {
				reps.put(exerciseTypeId, savedReps + exerciseProgress.getDoneReps());
			}
		}
		return reps;
	}

	@Override
	public String toString() {
		return id + " " + workout.getName();
	}
	
	
	/**
	 * Counts the completion percentage of the workout based on exercise count. Database intensive task, don't use in lists!
	 * @param databaseManager DatabaseManager used to load required entities.
	 * @return the completion percentage between 0 and 100. 0 if divided by zero, returns 0.
	 */
	public int getWorkoutProgressExercisePercentage(DatabaseManager databaseManager) {
		workout = databaseManager.getWorkout(databaseManager.getWorkoutProgress(id).workout.getId());
		if (doneExercises == null) {
			doneExercises = databaseManager.getWorkoutProgress(id).doneExercises;
		}
		try {
			return (100 * doneExercises.size()) / workout.getTotalNumberOfExercises(databaseManager);
		} catch (ArithmeticException e) {
			return 0;
		}
	}
	
	/**
	 * Counts the completion percentage of the workout based on done reps. As every exercise type is aggregated, value will be only a
	 * bad estimation of progress. Database intensive task, don't use in lists!
	 * @param databaseManager DatabaseManager used to load required entities.
	 * @return the completion percentage between 0 and 100. 0 if divided by zero, returns 0.
	 */
	public int getWorkoutProgressRepsPercentage(DatabaseManager databaseManager) {
		HashMap<String, Integer> done = getReps(databaseManager);
		int totalDone = 0;
		for (Integer reps : done.values()) {
			totalDone += reps;
		}
		HashMap<String, Integer> total = getReps(databaseManager);
		int totalTotal = 0;
		for (Integer reps : total.values()) {
			totalTotal += reps;
		}
		try {
			if (totalDone > totalTotal) {
				return 100;
			}
			return (100 * totalDone) / totalTotal;
		} catch (ArithmeticException e) {
			return 0;
		}
	}
}