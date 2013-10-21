package hu.droidium.fitness_app.model.comparators;

import hu.droidium.fitness_app.database.Program;

public class ProgramNameComparator extends ProgramComparator {

	public ProgramNameComparator(String displayName) {
		super(displayName);
	}

	@Override
	public int compare(Program lhs, Program rhs) {
		return lhs.getName().compareTo(rhs.getName());
	}

}
