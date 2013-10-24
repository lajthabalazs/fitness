package hu.droidium.fitness_app.model.comparators;

import hu.droidium.fitness_app.FlurryLogConstants;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Program;

public class ProgramLengthComparator extends ProgramComparator{

	private static int leftFirst = -1;
	private static int rightFirst = 1;
	
	private DatabaseManager databaseManager;

	public ProgramLengthComparator(String displayName, DatabaseManager databaseManager) {
		super(displayName, FlurryLogConstants.REORDERED_PROGRAMS_LENGTH);
		this.databaseManager = databaseManager;
	}

	@Override
	public int compare(Program lhs, Program rhs) {
		int leftLength = lhs.getTotalLength(databaseManager);
		int rightLength = rhs.getTotalLength(databaseManager);
		if (leftLength < rightLength) {
			return leftFirst;
		} else {
			return rightFirst;
		}
	}

}
