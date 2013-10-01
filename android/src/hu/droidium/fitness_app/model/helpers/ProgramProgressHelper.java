package hu.droidium.fitness_app.model.helpers;

import hu.droidium.fitness_app.database.ProgramProgress;
import android.content.Context;
import android.graphics.Color;

public class ProgramProgressHelper {
	
	public static int getBackgroundColor(ProgramProgress progress, Context context) {
		int color;
		if (progress.isDone()) {
			// Done
			color = Color.LTGRAY;
		} else if (progress.getTerminationDate() != -1) {
			// Aborted
			color = Color.DKGRAY;
		} else if (progress.getFirstMissedWorkout() != null){
			// Missed layout
			color = Color.RED;
		} else {
			// Active workout
			color = Color.GREEN;
		}
		return color;
	}	
}
