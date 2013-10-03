package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.Constants;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ExerciseProgress {
	
	@DatabaseField(generatedId=true)
	private long id;
	@DatabaseField(foreign=true)
	private WorkoutProgress workoutProgress;
	@DatabaseField(foreign=true)
	private Exercise exercise;
	@DatabaseField
	private int doneReps;
	@DatabaseField
	private int completionTime;
	@DatabaseField
	private long competedOn;
	
	public ExerciseProgress() {}
	public ExerciseProgress(WorkoutProgress workoutProgress, Exercise exercise, int reps, long workoutTime, long date) {
		this.workoutProgress = workoutProgress;
		this.exercise = exercise;
		this.doneReps = reps;
		this.completionTime = (int)workoutTime;
		this.competedOn = date;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public WorkoutProgress getWorkoutProgress() {
		return workoutProgress;
	}
	
	public void setWorkoutProgress(WorkoutProgress workoutProgress) {
		this.workoutProgress = workoutProgress;
	}

	public Exercise getExercise() {
		return exercise;
	}
	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}
	public int getDoneReps() {
		return doneReps;
	}
	public void setDoneReps(int doneReps) {
		this.doneReps = doneReps;
	}
	public int getCompletionTime() {
		return completionTime;
	}
	public void setCompletionTime(int completionTime) {
		this.completionTime = completionTime;
	}

	public long getCompetedOn() {
		return competedOn;
	}

	public void setCompetedOn(long competedOn) {
		this.competedOn = competedOn;
	}

	@Override
	public String toString() {
		return id + " " + Constants.format(competedOn) + " done " + doneReps + " " + exercise.getType().getUnit() + " of " + exercise.getType().getName();
	}
}