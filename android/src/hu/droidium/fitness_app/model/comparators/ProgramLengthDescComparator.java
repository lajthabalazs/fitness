package hu.droidium.fitness_app.model.comparators;

import hu.droidium.fitness_app.FlurryLogConstants;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Program;

public class ProgramLengthDescComparator extends ProgramComparator{

	private static int leftFirst = -1;
	private static int rightFirst = 1;
	
	private DatabaseManager databaseManager;

	public ProgramLengthDescComparator(String displayName, DatabaseManager databaseManager) {
		super(displayName, FlurryLogConstants.REORDERED_PROGRAMS_LENGTH_DESC);
		this.databaseManager = databaseManager;
	}

	@Override
	public int compare(Program lhs, Program rhs) {
		int leftLength = lhs.getTotalLength(databaseManager);
		int rightLength = rhs.getTotalLength(databaseManager);
		if (leftLength > rightLength) {
			return leftFirst;
		} else {
			return rightFirst;
		}
	}

}
