package hu.droidium.fitness_app.model.helpers;

import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.ProgramProgress;
import hu.droidium.fitness_app.database.Workout;

import java.util.Date;

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
	
	public static long getWorkoutDate(ProgramProgress progress, Workout workout) {		
		return progress.getProgressId() + workout.getDay() * Constants.DAY_MILLIS;
	}
	
	public static String getDateOfNextWorkoutText(ProgramProgress progress, Context context) {
		String dateMessage = null;
		if (progress.isDone()) {
			// Done
			dateMessage = context.getResources().getString(R.string.programDoneLabel, Constants.dateFormatter.format(new Date(progress.getTerminationDate())));
		} else if (progress.getTerminationDate() != -1) {
			// Aborted
			dateMessage = context.getResources().getString(R.string.programAbandonnedLabel, Constants.dateFormatter.format(new Date(progress.getTerminationDate())));
		} else if (progress.getFirstMissedWorkout() != null){
			// Missed layout
			long dayDiff = (System.currentTimeMillis() - getWorkoutDate(progress, progress.getFirstMissedWorkout())) / (1000*3600*24);
			dateMessage = context.getResources().getString(R.string.programMissedWorkoutLabep, "" + dayDiff, dayDiff > 1?"s":"");
		} else {
			// Next workout
			//long dayDiff = (progress.getNextWorkoutDay() - System.currentTimeMillis()) / (1000*3600*24);
			int dayDiff = progress.getDaysTilNextWorkout();
			if (dayDiff == 0) {
				dateMessage = context.getResources().getString(R.string.hasWorkoutToday);
			} else {
				dateMessage = context.getResources().getString(R.string.programNextWorkoutLabel, "" + dayDiff, dayDiff > 1?"s":"");
			}
		}
		return dateMessage;
	}
}
