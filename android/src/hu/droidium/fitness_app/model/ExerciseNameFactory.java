package hu.droidium.fitness_app.model;

import android.content.Context;

public class ExerciseNameFactory {
	public static String getName(int type, Context context) {
		return "Type " + type;
	}
}
