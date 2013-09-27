package hu.droidium.fitness_app.database;

import hu.droidium.fitness_app.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
	public Workout getFirstMissedWorkout() {
		Workout nextWorkout = getNextWorkout();
		if (nextWorkout == null) {
			return null;
		} else {
			// Compares due date to current date
			if ((nextWorkout.getDay() + 1) * Constants.DAY_MILLIS + progressId < System.currentTimeMillis()){
				return nextWorkout;
			} else {
				return null;
			}
		}
	}
	
	public long getNextWorkoutDay() {
		Workout nextWorkout = getNextWorkout();
		if (nextWorkout == null) {
			return -1;
		} else {
			return progressId + Constants.DAY_MILLIS * nextWorkout.getDay();
		}
	}
	
	public int getDaysTilNextWorkout() {
		long nextWorkoutDay = getNextWorkoutDay();
		if (nextWorkoutDay == -1) {
			return -1;
		} else {
			return  (int)((nextWorkoutDay - System.currentTimeMillis()) / Constants.DAY_MILLIS);
		}
	}
	
	public long getStartDate() {
		return progressId;
	}
	
	public Workout getNextWorkout() {
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
	
	public int getProgressPercentage() {
		HashSet<String> doneWorkoutIds = new HashSet<String>();
		for (WorkoutProgress workoutProgress : doneWorkouts) {
			doneWorkoutIds.add(workoutProgress.getWorkout().getId());
		}
		return (100 * doneWorkoutIds.size()) / program.getWorkouts().size();
	}
	
	@Override
	public String toString() {
		return progressId + " " + progressName + " (" + program.getName() + ")";
	}
}