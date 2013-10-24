package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.model.comparators.WorkoutComparator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ProgramProgress {

	private static final String TAG = "ProgramProgress";
	@DatabaseField(id = true)
	private long progressId;
	@DatabaseField (foreign = true)
	private Program program;
	@DatabaseField (defaultValue="-1")
	private long terminationDate;

	@ForeignCollectionField
	private ForeignCollection<WorkoutProgress> doneWorkouts;
	@DatabaseField(foreign=true, canBeNull=true)
	private WorkoutProgress actualWorkout;
	
	public ProgramProgress() {}
	
	public ProgramProgress(long id, Program program) {
		this.progressId = id;
		this.program = program;		
	}
	
	public boolean refresh(DatabaseManager databaseManager, boolean forced) {
		if (program == null || forced) {
			ProgramProgress other = databaseManager.getProgress(progressId);
			program = other.program;
			terminationDate = other.terminationDate;
			doneWorkouts = other.doneWorkouts;
			actualWorkout = other.actualWorkout;
			return true;
		} else {
			return false;
		}
	}


	public long getProgressId() {
		return progressId;
	}
	
	public void setProgressId(long progressId) {
		this.progressId = progressId;
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
		if (this.doneWorkouts == null) {
			return null;
		}
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
	
	public boolean isDone(DatabaseManager databaseManager) {
		loadFields(databaseManager);
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
	 * Searches for a workout the user missed.
	 * @return Null if no workout was missed. The workout otherwise.
	 */
	public Workout getFirstMissedWorkout(DatabaseManager databaseManager) {
		Workout nextWorkout = getNextWorkout(databaseManager);
		if (nextWorkout == null) {
			return null;
		} else {
			// Compares due date to current date
			long nextDate = Constants.stripDate(nextWorkout.getDay() * Constants.DAY_MILLIS + progressId);
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
			nextWorkout = databaseManager.getWorkout(nextWorkout.getId());
			int nextWorkoutDay = nextWorkout.getDay();
			return Constants.stripDate(progressId) + Constants.DAY_MILLIS * nextWorkoutDay;
		}
	}
	
	public int getDaysTilNextWorkout(DatabaseManager databaseManager) {
		long nextWorkoutDay = getNextWorkoutDay(databaseManager);
		if (nextWorkoutDay == -1) {
			return -1;
		} else {
			long now = Constants.stripDate(System.currentTimeMillis());
			int dayDiff = (int)((nextWorkoutDay - now) / Constants.DAY_MILLIS);
			return dayDiff;
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
		if (program.getWorkouts() == null) {
			program = databaseManager.getProgram(this.program.getId());
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
		return nextWorkout;
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
			if (actualWorkout != null) {
				if (workoutProgress.getWorkout().getId().equals(actualWorkout.getId())) {
					hasActualBeenDone = true;
				}
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
		return progressId + " (" + program.getName() + ")";
	}

	public List<Workout> getRemainingWorkouts(DatabaseManager databaseManager) {
		loadFields(databaseManager);
		program = databaseManager.getProgram(program.getId());
		HashSet<String> doneWorkoutIds = new HashSet<String>();
		if (actualWorkout != null) {
			doneWorkoutIds.add(actualWorkout.getWorkout().getId());
		}
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
		if (isDone(databaseManager)) {
			// Done
			dateMessage = context.getString(R.string.programDoneLabel, Constants.format(getTerminationDate()));
		} else if (getTerminationDate() != -1) {
			// Aborted
			dateMessage = context.getString(R.string.programAbandonnedLabel, Constants.format(getTerminationDate()));
		} else {
			long now = Constants.stripDate(System.currentTimeMillis());
			if (getFirstMissedWorkout(databaseManager) != null){
				// Missed layout
				long missedWorkout = Constants.stripDate(getWorkoutDate(getFirstMissedWorkout(databaseManager)));
				long dayDiff = (now - missedWorkout) / Constants.DAY_MILLIS;
				if (dayDiff == 1) {
					// Missed yesterday
					dateMessage = context.getString(R.string.programMissedWorkoutYesterday);
				} else {
					dateMessage = context.getString(R.string.programMissedWorkout, "" + dayDiff, dayDiff > 1?"s":"");	
				}
			} else {
				int dayDiff = getDaysTilNextWorkout(databaseManager);
				if (dayDiff == 0) {
					dateMessage = context.getString(R.string.programNextWorkoutToday);
				} else if (dayDiff == 1) {
					dateMessage = context.getString(R.string.programNextWorkoutTomorrow);
				} else {
					dateMessage = context.getString(R.string.programNextWorkout, "" + dayDiff, dayDiff > 1?"s":"");
				}
			}
		}
		return dateMessage;
	}

	public boolean isTodaysWorkout(Workout workout) {
		return getWorkoutDate(workout) == Constants.stripDate(System.currentTimeMillis());
	}
	
	public WorkoutProgress startWorkout(long now, Workout workout, DatabaseManager databaseManager) {
		Log.i(TAG, "Start workout " + workout.getId());
		// Check if there are any workouts that need to be skipped
		HashSet<String> doneWorkoutIds = new HashSet<String>();
		doneWorkouts = databaseManager.getProgress(progressId).doneWorkouts;
		if (actualWorkout != null) {
			actualWorkout = databaseManager.getWorkoutProgress(actualWorkout.getId());
			doneWorkoutIds.add(actualWorkout.getWorkout().getId());
		}
		for (WorkoutProgress doneWorkout : doneWorkouts) {
			doneWorkout = databaseManager.getWorkoutProgress(doneWorkout.getId());
			doneWorkoutIds.add(doneWorkout.getWorkout().getId());
		}
		program = databaseManager.getProgram(program.getId());
		// Add skipped workouts
		for (Workout programWorkout : program.getWorkouts()) {
			if (!doneWorkoutIds.contains(programWorkout.getId()) && (workout.getDay() > programWorkout.getDay())) {
				WorkoutProgress skippedProgress = new WorkoutProgress(now, programWorkout);
				skippedProgress.setFinishDate(now);
				skippedProgress.setProgramProgress(this);
				databaseManager.addWorkoutProgress(skippedProgress);
			}
		}
		if (actualWorkout != null) {
			actualWorkout = databaseManager.getWorkoutProgress(actualWorkout.getId());
			doneWorkoutIds.add(actualWorkout.getWorkout().getId());
			actualWorkout.setFinishDate(now);
			actualWorkout.setProgramProgress(this);
			databaseManager.updateWorkoutProgress(actualWorkout);
		}
		program = databaseManager.getProgram(program.getId());
		WorkoutProgress actualWorkout = new WorkoutProgress(now, workout);
		if (!databaseManager.addWorkoutProgress(actualWorkout)){
			actualWorkout = null;
		}
		setActualWorkout(actualWorkout);
		databaseManager.updateProgress(this);
		return actualWorkout;
	}
}












