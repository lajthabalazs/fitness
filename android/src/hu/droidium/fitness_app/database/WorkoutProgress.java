package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class WorkoutProgress {
	
	@DatabaseField(id=true)
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
	private long finishDate;
	
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

	public void exerciseDone(Exercise exercise, int reps, long l, long now, DatabaseManager databaseManager) {
		// TODO
	}

	@Override
	public String toString() {
		return id + " " + workout.getName();
	}
}