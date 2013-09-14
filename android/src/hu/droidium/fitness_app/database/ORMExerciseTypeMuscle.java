package hu.droidium.fitness_app.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ORMExerciseTypeMuscle {

	@DatabaseField(id=true)
	private String id;
	@DatabaseField(foreign=true)
	private ORMMuscle muscle;
	@DatabaseField(foreign=true)
	private ORMExerciseType exerciseType;

	public ORMExerciseTypeMuscle(){}
	
	public ORMExerciseTypeMuscle(ORMMuscle muscle, ORMExerciseType exerciseType){
		this.id = muscle.getId() + " " + exerciseType.getId();
		this.muscle = muscle;
		this.exerciseType = exerciseType;
	}
	
	public ORMMuscle getMuscle() {
		return muscle;
	}
	
	public void setMuscle(ORMMuscle muscle) {
		this.muscle = muscle;
	}

	public ORMExerciseType getExerciseType() {
		return exerciseType;
	}

	public void setExerciseType(ORMExerciseType exerciseType) {
		this.exerciseType = exerciseType;
	}
}
