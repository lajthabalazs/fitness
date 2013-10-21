package hu.droidium.fitness_app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

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
	
	public static String getEstimatedTimeString(int secs, Context context) {
		String timeText = "";
		if (secs > 3600) {
			int hours = secs / 3600;
			int minutes = (secs - 3600 * hours) / 60;
			secs = secs - 3600 * hours - minutes * 60;
			if (secs > 30) {
				minutes ++;
			}
			timeText = String.format(context.getString(R.string.estimatedTimeWithHour), hours, minutes);
		} else {
			int minutes = secs / 60;
			secs = secs - minutes * 60;
			if (secs > 30) {
				minutes ++;
			}
			timeText = String.format(context.getString(R.string.estimatedTimeWithMinutes), minutes);
		}
		return timeText;
	}
}
