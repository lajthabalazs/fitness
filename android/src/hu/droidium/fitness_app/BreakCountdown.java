package hu.droidium.fitness_app;

import hu.droidium.fitness_app.activities.DoWorkoutActivity;
import android.os.AsyncTask;
import android.util.Log;

public class BreakCountdown extends AsyncTask<Integer, Integer, Void> {
	
	private static final String TAG = "BreakCountdown";
	private DoWorkoutActivity activity;
	private boolean canceled;
	private Integer secs = 0;
	private long addedAt = -1;
	private long lastRun = 0;
	
	public BreakCountdown(DoWorkoutActivity activity) {
		this.activity = activity;
	}

	@Override
	protected Void doInBackground(Integer... params) {
		secs  = params[0];
		while (secs > 0) {
			publishProgress(secs);
			lastRun = System.currentTimeMillis();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
			// Time has been added, add a little to avoid flickering
			if (addedAt > lastRun) {
				long delay = addedAt - lastRun;
				addedAt = -1;
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
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

	public int getRemainingTimeInSecs() {
		return secs;
	}

	public void addSecs(int secs) {
		addedAt = System.currentTimeMillis();
		this.secs += secs;
		publishProgress(this.secs);
	}	
}
