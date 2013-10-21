package hu.droidium.fitness_app.model.comparators;

import hu.droidium.fitness_app.database.Program;

import java.util.Comparator;

public abstract class ProgramComparator  implements Comparator<Program> {
	private String displayName;

	public ProgramComparator(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
}
