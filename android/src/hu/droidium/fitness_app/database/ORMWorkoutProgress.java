package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ORMWorkoutProgress extends hu.droidium.fitness_app.model.WorkoutProgress {
	
	@DatabaseField(id=true)
	private long id;
	
	@DatabaseField
	private int actualBlock;

	@DatabaseField
	private int actualExercise;
	
	@ForeignCollectionField
	private ForeignCollection<ORMExerciseProgress> doneExercises;
	
	/* ************************ OrmLite helper methods *************************** */
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getActualBlock() {
		return actualBlock;
	}

	public void setActualBlock(int actualBlock) {
		this.actualBlock = actualBlock;
	}

	public List<ORMExerciseProgress> getDoneExercises() {
		ArrayList<ORMExerciseProgress> exerciseProgresses = new ArrayList<ORMExerciseProgress>();
		for (ORMExerciseProgress exercise : doneExercises) {
			exerciseProgresses.add(exercise);
		}
		return exerciseProgresses;
	}

	public void setDoneExercises(List<ORMExerciseProgress> doneExercises) {
		// TOOD
	}

	public void setActualExercise(int actualExercise) {
		this.actualExercise = actualExercise;
	}
	
	@Override
	protected void actualExerciseChanged(int blockIndex, int exerciseIndex) {
		setActualBlock(blockIndex);
		setActualExercise(exerciseIndex);
	}

	@Override
	protected void saveReps(int blockIndex, int exerciseIndex, int reps) {
	}

	@Override
	protected void done() {
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public int[] getActualExercise() {
		return null;
	}
}