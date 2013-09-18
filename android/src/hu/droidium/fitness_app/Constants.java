package hu.droidium.fitness_app;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {
	public static final String PROGRAM_PROGRESS_ID = "program progress id";
	public static final String WORKOUT_ID = "workout id";
	public static final String WORKOUT_PROGRESS_ID = "workout progress id";
	public static final int CANCEL = -1;
	public static final int STARTED_NEW_PROGRAM = 1;
	public static final String PROGRAM_ID_KEY = "program_id";
	public static final String REPS_DEFAULT_UNIT = "reps";
	
	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY/MM/DD", Locale.getDefault()); 
}
