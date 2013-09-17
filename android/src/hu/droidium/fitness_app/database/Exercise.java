package hu.droidium.fitness_app.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Exercise {

	@DatabaseField(id=true)
	private String id;
	@DatabaseField(foreign=true)
	private Block block;
	@DatabaseField
	private int order;
	@DatabaseField(foreign=true)
	private ExerciseType type;
	@DatabaseField
	private int reps;
	@DatabaseField
	private int targetSecs;
	@DatabaseField
	private int breakSecs;
	
	public Exercise() {
		
	}
	
	public Exercise(String id, Block block, int order, ExerciseType type, int reps, int targetSecs, int breakSecs) {
		this.id = id;
		this.block = block;
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
	
	public void setType(ExerciseType type){
		this.type = type;
	}
	
	public ExerciseType getType() {
		return type;
	}

	public void setReps(int reps) {
		this.reps = reps;
	}

	public int getReps() {
		return reps;
	}

	public void setTargetSecs(int targetSecs){
		this.targetSecs = targetSecs;
	}
	
	public int getTargetSecs() {
		return targetSecs;
	}

	public void setBreakSecs(int breakSecs){
		this.breakSecs = breakSecs;
	}

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