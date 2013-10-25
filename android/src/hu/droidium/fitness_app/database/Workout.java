package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.DataHelper;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.Translator;

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
	@DatabaseField(id=true)
	private String id;
	@DatabaseField
	private String name;
	@DatabaseField
	private int day; // The day of the program the workout is scheduled, starting from the beginning of the program
	@DatabaseField
	private String description;
	@DatabaseField(foreign=true)
	private Program program;
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
	
	public boolean refresh(DatabaseManager databaseManager) {
		return refresh(databaseManager, false);
	}

	public boolean refresh(DatabaseManager databaseManager, boolean forced) {
		if (name == null || blocks == null || program == null || forced) {
			Workout other = databaseManager.getWorkout(id);
			this.name = other.name;
			this.day = other.day;
			 this.description = other.description;
			 this.program = other.program;
			 this.blocks = other.blocks;
			 this.skipped = other.skipped;
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
		refresh(databaseManager);
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
		refresh(databaseManager);
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
			ExerciseType exerciseType = databaseManager.getExerciseType(sortedExercises.get(i).first);
			if (i > 0) {
				exerciseList = exerciseList + ", ";
			}
			exerciseList = exerciseList + Translator.getTranslation(exerciseType.getName());
			if (withReps) {
				int repsCount = sortedExercises.get(i).second;
				String repsString = "";
				if (repsCount > 750 && exerciseType.getKUnit() != null) {
					float kRepsCount = ((float)repsCount) / 1000;
					String kReps = exerciseType.getKUnit();
					repsString = String.format(context.getString(R.string.kRepsForListingExercises), Translator.getTranslation(kReps), kRepsCount);
				} else {
					String reps = databaseManager.getExerciseType(sortedExercises.get(i).first).getUnit();
					repsString = String.format(context.getString(R.string.repsForListingExercises), Translator.getTranslation(reps), repsCount);
				}
				exerciseList = exerciseList + repsString;
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
	 * Calculate the total units for this program based on the weighted sum of the exercises reps
	 * @param databaseManager
	 * @return
	 */
	public float getTotalUnits(DatabaseManager databaseManager) {
		refresh(databaseManager);
		float totalUnits = 0;
		for (Block block : blocks) {
			totalUnits += block.getTotalUnits(databaseManager);
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
		for (Block block : blocks) {
			totalTime += block.getTotalTime(databaseManager);
		}
		return totalTime;
	}

	/**
	 * Get number of reps by exercise type
	 * @param databaseManager
	 * @return
	 */
	public HashMap<String, Integer> getExercises(DatabaseManager databaseManager){
		refresh(databaseManager);
		HashMap<String, Integer> reps = new HashMap<String, Integer>();
		for (Block block : blocks) {
			block.refresh(databaseManager);
			for (Exercise exercise : block.getExercises()){
				exercise.refresh(databaseManager);
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

	public String getDueDateString(long progressStart, DatabaseManager databaseManager, Context context) {
		long today = Constants.stripDate(System.currentTimeMillis());
		long workoutDate = Constants.stripDate(getDay() * Constants.DAY_MILLIS + progressStart);;
		if (workoutDate < today) {
			// Already due
			int overdue = (int) ((today - workoutDate) / Constants.DAY_MILLIS);
			if (overdue == 1) {
				return context.getString(R.string.workoutDueYesterdayDate);
			} else {
				 return String.format(context.getString(R.string.workoutDuePastDate),overdue);
			}
		} else if (workoutDate > today) {
			// Due in future
			int timeLeft = (int) ((workoutDate - today) / Constants.DAY_MILLIS);
			if (timeLeft == 1) {
				return context.getString(R.string.workoutDueTomorroDate);
			} else {
				return String.format(context.getString(R.string.workoutDueFutureDate),timeLeft);
			}	
		} else {
			return context.getString(R.string.workoutDueForToday);
		}
	}

}











