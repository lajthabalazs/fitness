package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Program {
	
	private static final String TAG = "Program";
	@DatabaseField(id=true)
	private String id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String description;
	@DatabaseField
	private int color;
	@ForeignCollectionField()
	private ForeignCollection<Workout> workouts;
	
	// Cache
	private int totalLength = -1;
	private float totalUnits = -1;
	
	public Program() {}
	
	public Program(String id, String name, String description, int color) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.color = color;
	}
	
	public boolean refresh(DatabaseManager databaseManager) {
		return refresh(databaseManager, false);
	}

	public boolean refresh(DatabaseManager databaseManager, boolean forced) {
		if (name == null || workouts == null || forced) {
			Program other = databaseManager.getProgram(id);
			this.name = other.name;
			this.description = other.description;
			this.color = other.color;
			this.workouts = other.workouts;
			return true;
		} else {
			return false;
		}
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
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Workout> getWorkouts() {
		if (this.workouts == null) {
			return null;
		}
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

	public int getTotalLength(DatabaseManager databaseManager) {
		if (totalLength != -1) {
			return totalLength;
		}
		refresh(databaseManager);
		for (Workout workout : workouts) {
			totalLength = Math.max(totalLength, workout.getDay());
		}
		// First exercise is on day 0, add one to compensate.
		totalLength = totalLength + 1;
		return totalLength;
	}
	
	/**
	 * Calculate the total units for this program based on the weighted sum of the exercises reps
	 * @param databaseManager
	 * @return
	 */
	public float getTotalUnits(DatabaseManager databaseManager) {
		if (totalUnits != -1) {
			return totalUnits;
		} else {
			totalUnits = 0f;
		}
		refresh(databaseManager);
		for (Workout workout : workouts) {
			totalUnits += workout.getTotalUnits(databaseManager);
		}
		return totalUnits;
	}
	
	/**
	 * Get number of exercises by type
	 * @param databaseManager
	 * @return
	 */
	public HashMap<String, Integer> getExercises(DatabaseManager databaseManager){
		// TODO
		return null;
	}
	
	public double getTotalStamina(DatabaseManager databaseManager) {
		// TODO
		return 0;
	}

	public double getTotalStrength(DatabaseManager databaseManager) {
		// TODO
		return 0;
	}
	
	/**
	 * Return the total amount of speed
	 * @param databaseManager
	 * @return
	 */
	public double getTotalSpeed(DatabaseManager databaseManager) {
		// TODO
		return 0;
	}

	/**
	 * Return the total amount of flexibility
	 * @param databaseManager
	 * @return
	 */
	public double getTotalFlexibility(DatabaseManager databaseManager) {
		// TODO
		return 0;
	}

	/**
	 * Return the total amount of balance
	 * @param databaseManager
	 * @return
	 */
	public double getTotalBalance(DatabaseManager databaseManager) {
		// TODO
		return 0;
	}
	
	/**
	 * Get the weighted load of the program on each muscle
	 * @param databaseManager
	 * @return
	 */
	public HashMap<String, Double> getMuscleLoad(DatabaseManager databaseManager) {
		// TODO
		return null;
	}
}