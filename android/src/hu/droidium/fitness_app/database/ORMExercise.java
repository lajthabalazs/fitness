package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.model.ExerciseType;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ORMExercise implements hu.droidium.fitness_app.model.Exercise {

	@DatabaseField(id=true)
	private String id;
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
	@DatabaseField(foreign=true)
	private ORMBlock block;
	
	public ORMExercise() {
		
	}
	
	public ORMExercise(String id, int order, ORMExerciseType type, int reps, int targetSecs, int breakSecs) {
		this.id = id;
		this.order = order;
		this.type = type;
		this.reps = reps;
		this.targetSecs = targetSecs;
		this.breakSecs = breakSecs;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id){
		this.id = id;
	}
	
	public void setType(ORMExerciseType type){
		this.type = type;
	}
	
	@Override
	public ExerciseType getType() {
		return type;
	}

	public void setReps(int reps) {
		this.reps = reps;
	}

	@Override
	public int getReps() {
		return reps;
	}

	public void setTargetSecs(int targetSecs){
		this.targetSecs = targetSecs;
	}
	
	@Override
	public int getTargetSecs() {
		return targetSecs;
	}

	public void setBreakSecs(int breakSecs){
		this.breakSecs = breakSecs;
	}

	@Override
	public int getBreakSecs() {
		return breakSecs;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

		


}