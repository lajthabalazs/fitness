package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.model.Exercise;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ORMBlock implements hu.droidium.fitness_app.model.Block{

	@DatabaseField(id=true)
	private String id;
	@DatabaseField(foreign=true)
	private ORMWorkout workout;
	@DatabaseField
	private int order;
	@DatabaseField
	private String name;
	@ForeignCollectionField(orderColumnName="order")
	private ForeignCollection<ORMExercise> exercises;
	
	public ORMBlock() {}
	
	public ORMBlock(String id, ORMWorkout workout, String name) {
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
	
	public ORMWorkout getWorkout() {
		return workout;
	}

	public void setWorkout(ORMWorkout workout) {
		this.workout = workout;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	@Override
	public List<Exercise> getExercises() {
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		for (ORMExercise exercise : this.exercises){
			exercises.add(exercise);
		}
		return exercises;
	}
	
	public void setExercises(ForeignCollection<ORMExercise> exercises){
		this.exercises = exercises;
	}
}