package hu.droidium.fitness_app.model.helpers;

import hu.droidium.fitness_app.database.ProgramProgress;

import java.util.Comparator;

public class ProgressComparator implements Comparator<ProgramProgress> {
	static int leftFirst = -1;
	static int rightFirst = 1;
	@Override
	public int compare(ProgramProgress lhs, ProgramProgress rhs) {
		if (lhs.isDone()) {
			if (rhs.isDone()) {
				// Both done
				if (lhs.getTerminationDate() > rhs.getTerminationDate()) {
					return leftFirst;
				} else {
					return rightFirst;
				}
			} else {
				return rightFirst;
			}
		} else {
			if (rhs.isDone()) {
				return leftFirst;
			} else {
				// Both running
				// First is the one with a due exercise
				if (lhs.getNextWorkoutDay() < rhs.getNextWorkoutDay()){
					return leftFirst;
				} else {
					return rightFirst;
				}
			}
		}
	}
}