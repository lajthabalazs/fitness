package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Program {
	
	private static final String TAG = "ORMProgram";
	@DatabaseField(id=true)
	private String id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String description;
	@ForeignCollectionField
	private ForeignCollection<Workout> workouts;
	
	public Program() {}
	
	public Program(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
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
		for (Workout workout : this.workouts){
			workouts.add(workout);
		}
		return workouts;
	}
	public void setWorkouts(ForeignCollection<Workout> workouts) {
		this.workouts = workouts;
	}
	
	@Override
	public String toString() {
		int workoutSize = 0;
		int length = 0;
		try {
			workoutSize =  workouts.size();
			for (Workout workout:workouts) {
				length = Math.max(length, workout.getDay());
			}
		} catch (NullPointerException e) {
			Log.w(TAG, "No workouts");
		}
		return id + " " + name + " " + description + " " + workoutSize + " workouts in " + (length + 1) + " days";
	}
}