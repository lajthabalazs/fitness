package hu.droidium.fitness_app.model.comparators;

import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.ProgramProgress;

import java.util.Comparator;

import android.util.Log;

@SuppressWarnings("unused")
public class ProgressComparator implements Comparator<ProgramProgress> {
	
	private static final String TAG = "ProgressComparator";
	private DatabaseManager databaseManager;
	
	
	public ProgressComparator(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	static int leftFirst = -1;
	static int rightFirst = 1;
	@Override
	public int compare(ProgramProgress lhs, ProgramProgress rhs) {
		if (lhs.isDone(databaseManager)) {
			if (rhs.isDone(databaseManager)) {
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
			if (rhs.isDone(databaseManager)) {
				return leftFirst;
			} else {
				// Both running
				// First is the one with a due exercise
				if (lhs.getNextWorkoutDay(databaseManager) < rhs.getNextWorkoutDay(databaseManager)){
					return leftFirst;
				} else if (lhs.getNextWorkoutDay(databaseManager) > rhs.getNextWorkoutDay(databaseManager)){
					return rightFirst;
				} else {
					// Start with the freshest
					if (lhs.getProgressId() < rhs.getProgressId()) {
						return rightFirst;
					} else {
						return leftFirst;
					}
				}
			}
		}
	}
}