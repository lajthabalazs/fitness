package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Block {

	@DatabaseField(id=true)
	private String id;
	@DatabaseField(foreign=true)
	private Workout workout;
	@DatabaseField
	private int order;
	@DatabaseField
	private String name;
	@ForeignCollectionField(orderColumnName="order")
	private ForeignCollection<Exercise> exercises;
	
	public Block() {}
	
	public Block(String id, Workout workout, String name) {
		this.id = id;
		this.workout = workout;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Workout getWorkout() {
		return workout;
	}

	public void setWorkout(Workout workout) {
		this.workout = workout;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public List<Exercise> getExercises() {
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		for (Exercise exercise : this.exercises){
			exercises.add(exercise);
		}
		return exercises;
	}
	
	public void setExercises(ForeignCollection<Exercise> exercises){
		this.exercises = exercises;
	}
	
	public int getExerciseCount(DatabaseManager databaseManager) {
		if (exercises == null) {
			exercises = databaseManager.getBlock(id).exercises;
		}
		return exercises.size();
	}
	
	@Override
	public String toString() {
		try {
			return id + " " + name + " exercises: " + getExerciseCount(null);
		} catch (Exception e) {
			return id + " " + name + " exercises not loaded ";
		}
	}
}