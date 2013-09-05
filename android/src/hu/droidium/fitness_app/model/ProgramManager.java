package hu.droidium.fitness_app.model;

import java.util.List;

/**
 * Interface responsible for loading available programs.
 * @author lajthabalazs
 *
 */
public interface ProgramManager {
	public List<ProgramProgressManager> getProgramProgresses();
}