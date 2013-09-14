package hu.droidium.fitness_app.model;

import java.util.Comparator;

public interface ProgramProgress {

	public long getProgressId();
	public Program getProgram();
	public boolean isDone();
	public long getTerminationDate();
	public long getFirstMissedWorkout();
	public long getNextWorkoutDay();
	public int getDaysTilNextWorkout();
	public long getStartDate();
	public long getNextWorkoutId();
	public int getProgressPercentage();
	
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