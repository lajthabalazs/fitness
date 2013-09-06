package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import hu.droidium.fitness_app.model.Program;
import hu.droidium.fitness_app.model.Workout;

@DatabaseTable
public class ORMProgram implements Program {
	
	@DatabaseField(id=true)
	private String id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String description;
	@ForeignCollectionField(orderColumnName="day")
	private ForeignCollection<ORMWorkout> workouts;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Workout> getWorkouts() {
		ArrayList<Workout> workouts = new ArrayList<Workout>();
		for (ORMWorkout workout : this.workouts){
			workouts.add(workout);
		}
		return workouts;
	}
	public void setWorkouts(ForeignCollection<ORMWorkout> workouts) {
		this.workouts = workouts;
	}
	}
