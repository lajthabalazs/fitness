package hu.droidium.fitness_app.simple_model;

import hu.droidium.fitness_app.model.ExerciseType;

public class SimpleExerciseType implements ExerciseType {

	private String name;
	private String unit;
	
	public SimpleExerciseType(String name, String unit) {
		this.name = name;
		this.unit = unit;
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUnit() {
		return unit;
	}

}
