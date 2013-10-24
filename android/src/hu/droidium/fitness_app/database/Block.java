package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	public Block(String id, Workout workout, String name, int order) {
		this.id = id;
		this.workout = workout;
		this.name = name;
		this.order = order;
	}
	
	public boolean refresh(DatabaseManager databaseManager) {
		return refresh(databaseManager, false);
	}

	public boolean refresh(DatabaseManager databaseManager, boolean forced) {
		if (workout == null || forced) {
			Block otherBlock = databaseManager.getBlock(id);
			this.workout = otherBlock.workout;
			this.name = otherBlock.name;
			this.order = otherBlock.order;
			this.exercises = otherBlock.exercises;
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
		if (this.exercises == null) {
			return null;
		}
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
		refresh(databaseManager);
		return exercises.size();
	}
	
	/**
	 * Calculate the total units for this program based on the weighted sum of the exercises reps
	 * @param databaseManager
	 * @return
	 */
	public float getTotalUnits(DatabaseManager databaseManager) {
		refresh(databaseManager);
		float totalUnits = 0;
		for (Exercise exercise : exercises){
			exercise.refresh(databaseManager);
			ExerciseType type = exercise.getType();
			type.refresh(databaseManager);
			totalUnits += exercise.getReps() * type.getUnitWeight();
		}
		return totalUnits;
	}

	/**
	 * Calculate the total time of this program based on the time of each rep and breaktime
	 * @param databaseManager
	 * @return
	 */
	public float getTotalTime(DatabaseManager databaseManager) {
		refresh(databaseManager);
		float totalTime = 0;
		for (Exercise exercise : exercises){
			exercise.refresh(databaseManager);
			ExerciseType type = exercise.getType();
			type.refresh(databaseManager);
			if (exercise.getTargetSecs() > 0) {
				totalTime += exercise.getTargetSecs();
			} else {
				totalTime += exercise.getReps() * type.getUnitTime();
			}
			totalTime += exercise.getBreakSecs();
		}
		return totalTime;
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
	
	@Override
	public String toString() {
		try {
			return id + " " + name + " exercises: " + getExerciseCount(null);
		} catch (Exception e) {
			return id + " " + name + " exercises not loaded ";
		}
	}
}