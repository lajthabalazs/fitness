package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.model.helpers.WorkoutComparator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ProgramProgress {

	@DatabaseField(id = true)
	private long progressId;
	@DatabaseField
	private String progressName;
	@DatabaseField (foreign = true)
	private Program program;
	@DatabaseField (defaultValue="-1")
	private long terminationDate;

	@ForeignCollectionField
	private ForeignCollection<WorkoutProgress> doneWorkouts;
	@DatabaseField(foreign=true, canBeNull=true)
	private WorkoutProgress actualWorkout;
	
	public ProgramProgress() {}
	
	public ProgramProgress(long id, String progressName, Program program) {
		this.progressId = id;
		this.progressName = progressName;
		this.program = program;		
	}

	public long getProgressId() {
		return progressId;
	}
	
	public void setProgressId(long progressId) {
		this.progressId = progressId;
	}
	
	public String getProgressName() {
		return progressName;
	}
	
	public void setProgressName(String progressName) {
		this.progressName = progressName;
	}
	
	public Program getProgram() {
		return program;
	}
	
	public void setProgram(Program program) {
		this.program = program;
	}
	
	public long getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(long terminationDate) {
		this.terminationDate = terminationDate;
	}

	public WorkoutProgress getActualWorkout() {
		return actualWorkout;
	}
	
	public void setActualWorkout(WorkoutProgress actualWorkout) {
		this.actualWorkout = actualWorkout;
	}
		
	public List<WorkoutProgress> getDoneWorkouts() {
		ArrayList<WorkoutProgress> workouts = new ArrayList<WorkoutProgress>();
		for (WorkoutProgress workout : doneWorkouts) {
			workouts.add(workout);
		}
		return workouts;
	}
	
	public void setDoneWorkouts(ForeignCollection<WorkoutProgress> doneWorkouts) {
		this.doneWorkouts = doneWorkouts;
	}
	
	/* ******************************************************************** */
	/* ***********************    CONVINIENCE METHODS  ******************** */
	/* ******************************************************************** */
	
	public boolean isDone() {
		if (terminationDate == -1) {
			return false;
		}
		HashSet<String> doneWorkoutIds = new HashSet<String>();
		for (WorkoutProgress workoutProgress : doneWorkouts) {
			doneWorkoutIds.add(workoutProgress.getWorkout().getId());
		}
		for (Workout workout : program.getWorkouts()) {
			if (!doneWorkoutIds.contains(workout.getId())){
				return false;
			}
		}
		return true;
	}

	/**
	 * Searches for a workout the user missed. A workout counts as missed if a day has passed since it's due date.
	 * @return Null if no workout was missed. The workout otherwise.
	 */
	public Workout getFirstMissedWorkout(DatabaseManager databaseManager) {
		Workout nextWorkout = getNextWorkout(databaseManager);
		if (nextWorkout == null) {
			return null;
		} else {
			// Compares due date to current date
			long nextDate = Constants.stripDate((nextWorkout.getDay() + 1) * Constants.DAY_MILLIS + progressId);
			long now = Constants.stripDate(System.currentTimeMillis());
			if (nextDate < now){
				return nextWorkout;
			} else {
				return null;
			}
		}
	}
	
	public long getNextWorkoutDay(DatabaseManager databaseManager) {
		Workout nextWorkout = getNextWorkout(databaseManager);
		if (nextWorkout == null) {
			return -1;
		} else {
			return progressId + Constants.DAY_MILLIS * nextWorkout.getDay();
		}
	}
	
	public int getDaysTilNextWorkout(DatabaseManager databaseManager) {
		long nextWorkoutDay = getNextWorkoutDay(databaseManager);
		if (nextWorkoutDay == -1) {
			return -1;
		} else {
			return  (int)((nextWorkoutDay - System.currentTimeMillis()) / Constants.DAY_MILLIS);
		}
	}
	
	public long getStartDate() {
		return progressId;
	}

	public void loadFields(DatabaseManager databaseManager) {
		if (program == null || doneWorkouts == null) {
			ProgramProgress loadedProgress = databaseManager.getProgress(progressId);
			actualWorkout = loadedProgress.actualWorkout;
			program = loadedProgress.program;
			doneWorkouts = loadedProgress.doneWorkouts;
			
		}
	}
	
	public Workout getNextWorkout(DatabaseManager databaseManager) {
		loadFields(databaseManager);
		if (actualWorkout != null) {
			actualWorkout = databaseManager.getWorkoutProgress(actualWorkout.getId());
			return actualWorkout.getWorkout();
		}
		HashSet<String> doneWorkoutIds = new HashSet<String>();
		for (WorkoutProgress workoutProgress : doneWorkouts) {
			doneWorkoutIds.add(workoutProgress.getWorkout().getId());
		}
		Workout nextWorkout = null;
		for (Workout workout : program.getWorkouts()) {
			if (!doneWorkoutIds.contains(workout.getId())){
				if (nextWorkout == null){
					nextWorkout = workout;
					continue;
				} else if (nextWorkout.getDay() > workout.getDay()) {
					nextWorkout = workout;
					continue;
				}
			}
		}
		if (nextWorkout == null) {
			return null;
		} else {
			return nextWorkout;
		}
	}
	
	public int getProgressPercentage(DatabaseManager databaseManager) {
		loadFields(databaseManager);
		HashSet<String> doneWorkoutIds = new HashSet<String>();
		program = databaseManager.getProgram(program.getId());
		// Add actual workout as "done"
		if (actualWorkout != null) {
			actualWorkout = databaseManager.getWorkoutProgress(actualWorkout.getId());
			doneWorkoutIds.add(actualWorkout.getWorkout().getId());
		}
		boolean hasActualBeenDone = false;
		for (WorkoutProgress workoutProgress : doneWorkouts) {
			workoutProgress = databaseManager.getWorkoutProgress(workoutProgress.getId());
			doneWorkoutIds.add(workoutProgress.getWorkout().getId());
			if (workoutProgress.getWorkout().getId().equals(actualWorkout.getId())) {
				hasActualBeenDone = true;
			}
		}
		float doneWorkouts = doneWorkoutIds.size();
		if (actualWorkout!=null && !hasActualBeenDone) {
			doneWorkouts -= 0.5f;
		}
		return (int) ((100 * doneWorkouts) / program.getWorkouts().size());
	}
	
	@Override
	public String toString() {
		return progressId + " " + progressName + " (" + program.getName() + ")";
	}

	public List<Workout> getRemainingWorkouts(DatabaseManager databaseManager) {
		loadFields(databaseManager);
		HashSet<String> doneWorkoutIds = new HashSet<String>();
		program = databaseManager.getProgram(program.getId());
		for (WorkoutProgress workoutProgress : doneWorkouts) {
			workoutProgress = databaseManager.getWorkoutProgress(workoutProgress.getId());
			doneWorkoutIds.add(workoutProgress.getWorkout().getId());
		}
		TreeSet<Workout> workoutOrderer = new TreeSet<Workout>(new WorkoutComparator());
		for (Workout workout : program.getWorkouts()) {
			if (!doneWorkoutIds.contains(workout.getId())) {
				workoutOrderer.add(workout);
			}
		}
		return new ArrayList<Workout>(workoutOrderer);
	}
	
	public long getWorkoutDate(Workout workout) {
		return Constants.stripDate(getProgressId() + workout.getDay() * Constants.DAY_MILLIS);
	}
	
	public String getDateOfNextWorkoutText(DatabaseManager databaseManager, Context context) {
		loadFields(databaseManager);
		String dateMessage = null;
		if (isDone()) {
			// Done
			dateMessage = context.getResources().getString(R.string.programDoneLabel, Constants.dateFormatter.format(new Date(getTerminationDate())));
		} else if (getTerminationDate() != -1) {
			// Aborted
			dateMessage = context.getResources().getString(R.string.programAbandonnedLabel, Constants.dateFormatter.format(new Date(getTerminationDate())));
		} else if (getFirstMissedWorkout(databaseManager) != null){
			// Missed layout
			long dayDiff = (System.currentTimeMillis() - getWorkoutDate(getFirstMissedWorkout(databaseManager))) / (1000*3600*24);
			dateMessage = context.getResources().getString(R.string.programMissedWorkoutLabep, "" + dayDiff, dayDiff > 1?"s":"");
		} else {
			// Next workout
			//long dayDiff = (progress.getNextWorkoutDay() - System.currentTimeMillis()) / (1000*3600*24);
			int dayDiff = getDaysTilNextWorkout(databaseManager);
			if (dayDiff == 0) {
				dateMessage = context.getResources().getString(R.string.hasWorkoutToday);
			} else {
				dateMessage = context.getResources().getString(R.string.programNextWorkoutLabel, "" + dayDiff, dayDiff > 1?"s":"");
			}
		}
		return dateMessage;
	}
}