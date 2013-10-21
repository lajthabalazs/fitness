package hu.droidium.fitness_app.model.comparators;

import hu.droidium.fitness_app.database.Program;

public class ProgramUnitComparator extends ProgramComparator {

	public ProgramUnitComparator(String displayName) {
		super(displayName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Program lhs, Program rhs) {
		return lhs.getName().compareTo(rhs.getName());
	}

}
