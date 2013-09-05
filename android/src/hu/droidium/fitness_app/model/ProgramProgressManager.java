package hu.droidium.fitness_app.model;

import java.util.List;

/**
 * Interface to load and save user's progress in a program progress.
 * @author lajthabalazs
 *
 */
public interface ProgramProgressManager {

	public List<ProgramProgress> getProgress();
	public ProgramProgress getProgress(long programId);
	public void jumpToWorkout(int index);
	public boolean exerciseDone(ProgramProgress progress, Exercise exercise, int reps, long durationSecs, long date);
}