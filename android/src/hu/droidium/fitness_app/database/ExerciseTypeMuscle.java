package hu.droidium.fitness_app.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ExerciseTypeMuscle {

	@DatabaseField(id=true)
	private String id;
	@DatabaseField(foreign=true)
	private Muscle muscle;
	@DatabaseField(foreign=true, foreignAutoRefresh=true)
	private ExerciseType exerciseType;

	public ExerciseTypeMuscle(){}
	
	public ExerciseTypeMuscle(Muscle muscle, ExerciseType exerciseType){
		this.id = muscle.getId() + " " + exerciseType.getId();
		this.muscle = muscle;
		this.exerciseType = exerciseType;
	}
	
	public Muscle getMuscle() {
		return muscle;
	}
	
	public void setMuscle(Muscle muscle) {
		this.muscle = muscle;
	}

	public ExerciseType getExerciseType() {
		return exerciseType;
	}

	public void setExerciseType(ExerciseType exerciseType) {
		this.exerciseType = exerciseType;
	}
}
