package hu.droidium.fitness_app.model.helpers;

import hu.droidium.fitness_app.database.ProgramProgress;
import hu.droidium.fitness_app.database.Workout;

import java.util.Comparator;

public class ProgressComparator implements Comparator<ProgramProgress> {
	static int leftFirst = 1;
	static int rightFirst = -1;
	@Override
	public int compare(ProgramProgress lhs, ProgramProgress rhs) {
		if (!lhs.isDone() && !rhs.isDone()){
			Workout lMissedWorkout = lhs.getFirstMissedWorkout();
			Workout rMissedWorkout = rhs.getFirstMissedWorkout();
			if ((lMissedWorkout == null) && (rMissedWorkout != null)) {
				return rightFirst;
			} else if ((lMissedWorkout != null) && (rMissedWorkout == null)) {
				return leftFirst;
			}else if ((lMissedWorkout == null) && (rMissedWorkout == null)) {
				if (lhs.getNextWorkoutDay() > rhs.getNextWorkoutDay()) {
					return rightFirst;
				} else if (lhs.getNextWorkoutDay() < rhs.getNextWorkoutDay()) {
					return leftFirst;
				} else {
					return decideOnStart(lhs, rhs);
				}
			} else {
				if (ProgramProgressHelper.getWorkoutDate(lhs, lMissedWorkout) > ProgramProgressHelper.getWorkoutDate(rhs, rMissedWorkout)) {
					return rightFirst;
				} else if (ProgramProgressHelper.getWorkoutDate(lhs, lMissedWorkout) > ProgramProgressHelper.getWorkoutDate(rhs, rMissedWorkout)) {
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