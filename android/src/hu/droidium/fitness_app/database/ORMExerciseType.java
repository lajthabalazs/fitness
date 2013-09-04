package hu.droidium.fitness_app.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import hu.droidium.fitness_app.model.ExerciseType;

@DatabaseTable
public class ORMExerciseType implements ExerciseType {

	@DatabaseField
	private String name;
	@DatabaseField
	private String unit;
	
	public void setName(String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setUnit(String unit){
		this.unit = unit;
	}
	@Override
	public String getUnit() {
		return unit;
	}

}
