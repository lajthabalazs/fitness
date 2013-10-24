package hu.droidium.fitness_app.model.comparators;

import hu.droidium.fitness_app.database.Program;

import java.util.Comparator;

public abstract class ProgramComparator  implements Comparator<Program> {
	private String displayName;
	private String name;

	public ProgramComparator(String displayName, String name) {
		this.displayName = displayName;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return displayName;
	}

	public String getName() {
		return name;
	}
}
