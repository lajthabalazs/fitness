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
	@DatabaseField
	private int order;
	@DatabaseField
	private String name;
	@ForeignCollectionField(orderColumnName="order")
	private ForeignCollection<ORMExercise> exercises;
	
	public ORMBlock() {}
	
	public ORMBlock(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
	/**
	 * WARNING can call only after an object has been stored and reloaded
	 * @param exercises
	 */
	public void updateExercises(List<ORMExercise> exercises){
		this.exercises.clear();
		this.exercises.addAll(exercises);
	}
	
	public void setExercises(ForeignCollection<ORMExercise> exercises){
		this.exercises = exercises;
	}
}