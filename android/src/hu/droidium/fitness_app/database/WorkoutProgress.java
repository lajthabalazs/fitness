package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class WorkoutProgress {
	
	@DatabaseField(generatedId=true)
	private long id;
	@DatabaseField(foreign=true)
	private ProgramProgress programProgress;
	@DatabaseField(foreign=true)
	private Workout workout;
	@DatabaseField
	private int actualBlock;
	@DatabaseField
	private int actualExercise;
	@ForeignCollectionField
	private ForeignCollection<ExerciseProgress> doneExercises;
	@DatabaseField(defaultValue="-1")
	private long finishDate = -1;
	
	public WorkoutProgress() {}
	public WorkoutProgress(ProgramProgress programProgress, Workout workout) {
		this.programProgress = programProgress;
		this.workout = workout;
		this.finishDate = -1;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
		Workout workout = databaseManager.getWorkout(this.workout.getId());
		databaseManager.addExerciseProgress(exerciseProgress);
		Block block = databaseManager.getBlock(exercise.getBlock().getId());
		int blockIndex = block.getOrder();
		int blockCount = workout.getNumberOfBlocks(databaseManager);
		int exerciseCount = block.getExerciseCount(databaseManager);
		int exerciseIndex = exercise.getOrder();
		if (exerciseIndex == exerciseCount - 1) {
			if (blockIndex == blockCount - 1) {
				// Last exercise of last block
				this.finishDate = date;
				this.actualBlock = -1;
				this.actualExercise = -1;
				databaseManager.updateWorkoutProgress(this);
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
				setProgramProgress(programProgress);
				databaseManager.updateWorkoutProgress(this);
			}
		} else {
			this.actualExercise += 1;
			databaseManager.updateWorkoutProgress(this);
			
		}
	}

	public Exercise getExercise(int actualBlockIndex, int actualExerciseIndex, DatabaseManager databaseManager) {
		workout = databaseManager.getWorkoutProgress(getId()).getWorkout();
		workout = databaseManager.getWorkout(workout.getId());
		Block block = databaseManager.getBlock(workout.getBlocks().get(actualBlockIndex).getId());		
		Exercise exercise = databaseManager.getExercise(block.getExercises().get(actualExerciseIndex).getId());
		return exercise;
	}


	@Override
	public String toString() {
		return id + " " + workout.getName();
	}
	
	public String getProgressText(DatabaseManager databaseManager) {
		if (workout == null) {
			workout = databaseManager.getWorkout(databaseManager.getWorkoutProgress(id).workout.getId());
		}
		if (doneExercises == null) {
			doneExercises = databaseManager.getWorkoutProgress(id).doneExercises;
		}
		String ret = workout.getName() + " (day " + workout.getDay() + ") " + doneExercises.size() + "/" +  workout.getTotalNumberOfExercises(databaseManager) + " done.";
		return ret;
	}
}