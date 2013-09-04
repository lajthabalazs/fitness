package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ORMWorkoutProgress {
	
	@DatabaseField(id=true)
	private long id;
	@DatabaseField
	private int actualBlock;
	@DatabaseField
	private int actualExercise;
	@ForeignCollectionField
	private ForeignCollection<ORMExerciseProgress> doneExercises;
	@DatabaseField(defaultValue="-1")
	private long finishDate;
	
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

	public void setDoneExercises(ForeignCollection<ORMExerciseProgress> doneExercises) {
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

}