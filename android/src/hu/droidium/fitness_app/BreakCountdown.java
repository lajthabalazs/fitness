package hu.droidium.fitness_app;

import android.os.AsyncTask;
import android.util.Log;

public class BreakCountdown extends AsyncTask<Integer, Integer, Void> {
	
	private static final String TAG = "BreakCountdown";
	private ExerciseActivity activity;
	private boolean canceled;
	
	public BreakCountdown(ExerciseActivity activity) {
		this.activity = activity;
	}

	@Override
	protected Void doInBackground(Integer... params) {
		int secs = params[0];
		while (secs > 0) {
			publishProgress(secs);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
			secs --;
			
		}
		return null;
	}
	
	@Override
	protected void onCancelled() {
		canceled = true;
		super.onCancelled();
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		if (!canceled) {
			activity.displayBreakTime(values[0]);
		} else {
			Log.e(TAG, "Already canceled.");
		}
	}
	
	@Override
	protected void onPostExecute(Void result) {
		if (!canceled) {
			activity.endOfBreak();
		} else {
			Log.e(TAG, "Already canceled.");
		}
	}	
}
