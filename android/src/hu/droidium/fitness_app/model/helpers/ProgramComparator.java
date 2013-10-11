package hu.droidium.fitness_app.model.helpers;

import java.util.Comparator;

import hu.droidium.fitness_app.database.Program;

public class ProgramComparator implements Comparator<Program> {

	@Override
	public int compare(Program lhs, Program rhs) {
		return lhs.getName().compareTo(rhs.getName());
	}

}
