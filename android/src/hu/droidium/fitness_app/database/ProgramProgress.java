package hu.droidium.fitness_app.database;

import java.util.ArrayList;
import java.util.Comparator;
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
	@DatabaseField
	private Program program;
	@DatabaseField
	private int actualWorkout;
	@DatabaseField
	private int actualBlock;
	@DatabaseField
	private int actualExercise;
	@ForeignCollectionField
	private ForeignCollection<WorkoutProgress> doneWorkouts;

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
	
	public int getActualWorkout() {
		return actualWorkout;
	}
	
	public void setActualWorkout(int actualWorkout) {
		this.actualWorkout = actualWorkout;
	}
	
	public int getActualBlock() {
		return actualBlock;
	}
	
	public void setActualBlock(int actualBlock) {
		this.actualBlock = actualBlock;
	}
	
	public int getActualExercise() {
		return actualExercise;
	}
	
	public void setActualExercise(int actualExercise) {
		this.actualExercise = actualExercise;
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
	
	public boolean isDone() {
		return false;
	}
	public long getTerminationDate() {
		return -1;
	}
	public long getFirstMissedWorkout() {
		return -1;
	}
	public long getNextWorkoutDay() {
		return -1;
	}
	public int getDaysTilNextWorkout() {
		return 1;
	}
	public long getStartDate() {
		return -1;
	}
	public long getNextWorkoutId() {
		return -1;
	}
	public int getProgressPercentage() {
		return 1;
	}
	
	public static class ProgressComparator implements Comparator<ProgramProgress> {
		static int leftFirst = 1;
		static int rightFirst = 0;
		@Override
		public int compare(ProgramProgress lhs, ProgramProgress rhs) {
			if (!lhs.isDone() && !rhs.isDone()){
				if ((lhs.getFirstMissedWorkout() == -1) && (rhs.getFirstMissedWorkout() != -1)) {
					return rightFirst;
				} else if ((lhs.getFirstMissedWorkout() != -1) && (rhs.getFirstMissedWorkout() == -1)) {
					return leftFirst;
				}else if ((lhs.getFirstMissedWorkout() == -1) && (rhs.getFirstMissedWorkout() == -1)) {
					if (lhs.getNextWorkoutDay() > rhs.getNextWorkoutDay()) {
						return rightFirst;
					} else if (lhs.getNextWorkoutDay() < rhs.getNextWorkoutDay()) {
						return leftFirst;
					} else {
						return decideOnStart(lhs, rhs);
					}
				} else {
					if (lhs.getFirstMissedWorkout() > rhs.getFirstMissedWorkout()) {
						return rightFirst;
					} else if (lhs.getFirstMissedWorkout() < rhs.getFirstMissedWorkout()) {
						return leftFirst;
					} else {
						return decideOnStart(lhs, rhs);
					}
				}


			} else if (lhs.isDone() && !rhs.isDone()){
				return rightFirst;
			} else if (!lhs.isDone() && rhs.isDone()) {
				return leftFirst;
			} else {
				if (lhs.getTerminationDate() < rhs.getTerminationDate()){
					return rightFirst;
				} else if (lhs.getTerminationDate() > rhs.getTerminationDate()){
					return leftFirst;
				} else {
					return decideOnStart(lhs, rhs);
				}
			}
		}
		
		private int decideOnStart(ProgramProgress lhs, ProgramProgress rhs) {
			if (lhs.getStartDate() < rhs.getStartDate()){
				return rightFirst;
			} else if (lhs.getStartDate() > rhs.getStartDate()){
				return leftFirst;
			} else {
				return 0;
			}
		}
	}
}