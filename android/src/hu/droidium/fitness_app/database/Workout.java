package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.DataHelper;
import hu.droidium.fitness_app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Pair;

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
	 * Convenience method for representing aggregated number of exercises.
	 * @param count The number of items to list, if 0, all items will be listed
	 * @param withReps Weather to include the number of reps
	 * @param context
	 * @param databaseManager
	 * @return A String containing a comma separated list of the exercises in descending order
	 */
	public String getExercisesList(int count, boolean withReps, Context context, DatabaseManager databaseManager) {
		// Aggregated exercise types
		List<Pair<String, Integer>> sortedExercises = DataHelper.getSortedExercises(getExercises(databaseManager));
		String exerciseList = "";
		for (int i = 0; (count == 0 || i < count) && i < sortedExercises.size(); i++) {
			if (i > 0) {
				exerciseList = exerciseList + ", ";
			}
			exerciseList = exerciseList + databaseManager.getExerciseType(sortedExercises.get(i).first).getName();
			if (withReps) {
				String reps = databaseManager.getExerciseType(sortedExercises.get(i).first).getUnit();
				int repsCount = sortedExercises.get(i).second;
				exerciseList = exerciseList + String.format(context.getString(R.string.repsForListingExercises), reps, repsCount);
			}
		}
		if (count != 0 && count < sortedExercises.size()) {
			exerciseList = exerciseList + context.getResources().getString(R.string.andMore);
		}
		if (sortedExercises.size() == 0) {
			return context.getResources().getString(R.string.noExercises);
		} else {
			return exerciseList;
		}
	}
	
	/**
	 * Calculate the total load of this program based on the weighted sum of the exercises reps
	 * @param databaseManager
	 * @return
	 */
	public double getTotalLoad(DatabaseManager databaseManager) {
		// TODO
		return 0;
	}
	
	/**
	 * Get number of reps by exercise type
	 * @param databaseManager
	 * @return
	 */
	public HashMap<String, Integer> getExercises(DatabaseManager databaseManager){
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
	public HashMap<Muscle, Double> getMuscleLoad(DatabaseManager databaseManager) {
		// TODO
		return null;
	}

}











