package hu.droidium.fitness_app.database;

import java.util.List;

import hu.droidium.fitness_app.model.Exercise;
import hu.droidium.fitness_app.model.ProgramProgress;

public class ProgramProgressManager implements hu.droidium.fitness_app.model.ProgramProgressManager {

	@Override
	public List<ProgramProgress> getProgress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void jumpToWorkout(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean exerciseDone(ProgramProgress progress, Exercise exercise,
			int reps, long durationSecs, long date) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ProgramProgress getProgress(long programId) {
		return null;
	}

}
