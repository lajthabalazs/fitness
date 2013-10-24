package hu.droidium.fitness_app.model.comparators;

import hu.droidium.fitness_app.database.Program;

public class ProgramDifficultyComparator extends ProgramComparator {

	public ProgramDifficultyComparator(String displayName, String name) {
		super(displayName, name);
	}

	@Override
	public int compare(Program lhs, Program rhs) {
		return lhs.getName().compareTo(rhs.getName());
	}

}
