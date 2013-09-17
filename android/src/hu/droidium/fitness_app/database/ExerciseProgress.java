package hu.droidium.fitness_app.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ExerciseProgress {
	
	@DatabaseField(id=true)
	private long id;
	@DatabaseField(foreign=true)
	private WorkoutProgress workoutProgress;
	@DatabaseField(foreign=true)
	private Exercise exercise;
	@DatabaseField
	private int doneReps;
	@DatabaseField
	private int completionTime;
	
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
}