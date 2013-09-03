package hu.droidium.fitness_app.model;

import java.util.List;

public class SimpleBlock implements Block {
	
	private String name;
	private List<Exercise> exercises;

	public SimpleBlock(String name, List<Exercise> exercises) {
		this.name = name;
		this.exercises = exercises;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Exercise> getExercises() {
		return exercises;
	}

}
