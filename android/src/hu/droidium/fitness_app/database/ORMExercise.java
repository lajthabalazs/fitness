package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.model.ExerciseType;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ORMExercise implements hu.droidium.fitness_app.model.Exercise {

	@DatabaseField(id=true, generatedId=true)
	private long id;
	@DatabaseField
	private int order;
	@DatabaseField(foreign=true)
	private ORMExerciseType type;
	@DatabaseField
	private int reps;
	@DatabaseField
	private int targetSecs;
	@DatabaseField
	private int breakSecs;
	
	public void setId(long id){
		this.id = id;
	}
	
	public void setType(ORMExerciseType type){
		this.type = type;
	}
	
	public void setReps(int reps) {
		this.reps = reps;
	}
	
	public void setTargetSecs(int targetSecs){
		this.targetSecs = targetSecs;
	}
	
	public void setBreakSecs(int breakSecs){
		this.breakSecs = breakSecs;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	
	public long getId() {
		return id;
	}
	
	@Override
	public ExerciseType getType() {
		return type;
	}

	@Override
	public int getReps() {
		return reps;
	}

	@Override
	public int getTargetSecs() {
		return targetSecs;
	}

	@Override
	public int getBreakSecs() {
		return breakSecs;
	}

}