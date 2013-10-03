package hu.droidium.fitness_app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Constants {
	public static final String PROGRAM_PROGRESS_ID = "program progress id";
	public static final String WORKOUT_ID = "workout id";
	public static final String WORKOUT_PROGRESS_ID = "workout progress id";
	public static final int RESULT_CANCEL = -1;
	public static final int RESULT_STARTED_NEW_PROGRAM = 1;
	public static final String PROGRAM_ID_KEY = "program_id";
	public static final String REPS_DEFAULT_UNIT = "reps";
	
	private static final Date date = new Date();
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	public static final long DAY_MILLIS = 24l * 3600 * 1000; 
	private static final Calendar calendar = Calendar.getInstance();
	
	public static long stripDate(long date) {
		calendar.setTimeInMillis(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTimeInMillis();
	}
	
	public static String format(long millis){
		date.setTime(millis);
		return dateFormatter.format(date);
	}
}
