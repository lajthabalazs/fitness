package hu.droidium.fitness_app.model.helpers;

import hu.droidium.fitness_app.database.Workout;

import java.util.Comparator;

public class WorkoutComparator implements Comparator<Workout> {
	static int leftFirst = -1;
	static int rightFirst = 1;
	@Override
	public int compare(Workout lhs, Workout rhs) {
		if (lhs.getDay() > rhs.getDay()) {
			return rightFirst;
		} else if (lhs.getDay() < rhs.getDay()) {
			return leftFirst;
		} else {
			return 0;
		}
	}
}