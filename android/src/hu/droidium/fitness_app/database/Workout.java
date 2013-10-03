package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Workout{
	
	@SuppressWarnings("unused")
	private static final String TAG = "Workout";
	@DatabaseField(id=true)
	private String id;
	@DatabaseField(foreign=true)
	private Program program;
	@DatabaseField
	private String name;
	@DatabaseField
	private int day; // The day of the program the workout is scheduled, starting from the beginning of the program
	@DatabaseField
	private String description;
	@ForeignCollectionField(orderColumnName="order")
	private ForeignCollection<Block> blocks;
	@DatabaseField(defaultValue="false")
	private boolean skipped;

	public Workout(){
	}
	
	public Workout(String id, Program program, int day, String name, String description) {
		this.id = id; 
		this.program = program;
		this.day = day;
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBlocks(ForeignCollection<Block> blocks) {
		this.blocks = blocks;
	}

	public List<Block> getBlocks() {
		if (this.blocks == null) {
			return null;
		}
		ArrayList<Block> ret = new ArrayList<Block>();
		
		for (Block block : blocks) {
			ret.add(block);
		}
		return ret;
	}

	public boolean isSkipped() {
		return skipped;
	}

	public void setSkipped(boolean skipped) {
		this.skipped = skipped;
	}

	public final int getNumberOfBlocks(DatabaseManager databaseManager){
		if (blocks == null) {
			blocks = databaseManager.getWorkout(id).blocks;
		}
		return getBlocks().size();
	}
	
	@Override
	public String toString() {
		try {
			return id + " " + name + " blocks: " + getNumberOfBlocks(null);
		} catch (Exception e) {
			return id + " " + name + " blocks not loaded.";
		}
	}

	public int getTotalNumberOfExercises(DatabaseManager databaseManager) {
		if (blocks == null) {
			blocks = databaseManager.getWorkout(id).blocks;
		}
		int exerciseCount = 0;
		for (Block block : blocks) {
			exerciseCount += block.getExerciseCount(databaseManager);
		}
		return exerciseCount;
	}
	
	/**
	 * Database intensive task. Counts the number of reps in a workout for each exercise type
	 * @param databaseManager Database manager used to update database entities.
	 * @return A hashmap indexed with the exercise type id, containing total reps of each type.
	 */
	public HashMap<String, Integer> getTotalReps(DatabaseManager databaseManager) {
		HashMap<String, Integer> reps = new HashMap<String, Integer>();
		if (blocks == null) {
			blocks = databaseManager.getWorkout(id).blocks;
		}
		for (Block block : blocks) {
			block = databaseManager.getBlock(block.getId());
			for (Exercise exercise : block.getExercises()){
				exercise = databaseManager.getExercise(exercise.getId());
				String exerciseTypeId = exercise.getType().getId();
				Integer savedReps = reps.get(exerciseTypeId);
				if (savedReps == null) {
					reps.put(exerciseTypeId, exercise.getReps());
				} else {
					reps.put(exerciseTypeId, savedReps + exercise.getReps());
				}
			}
		}
		return reps;
	}
}











