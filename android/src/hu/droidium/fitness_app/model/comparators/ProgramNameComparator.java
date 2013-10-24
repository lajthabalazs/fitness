package hu.droidium.fitness_app.model.comparators;

import hu.droidium.fitness_app.FlurryLogConstants;
import hu.droidium.fitness_app.database.Program;

public class ProgramNameComparator extends ProgramComparator {

	public ProgramNameComparator(String displayName) {
		super(displayName, FlurryLogConstants.REORDERED_PROGRAMS_NAME);
	}

	@Override
	public int compare(Program lhs, Program rhs) {
		return lhs.getName().compareTo(rhs.getName());
	}

}
