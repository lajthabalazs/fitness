package hu.droidium.fitness_app.model;

import java.util.List;

public interface Program {
	
	public String getId();
	public String getName();
	public String getDescription();
	public List<Workout> getWorkouts();
}
