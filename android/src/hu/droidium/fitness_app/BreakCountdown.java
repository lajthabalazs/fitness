package hu.droidium.fitness_app;

import android.os.AsyncTask;

public class BreakCountdown extends AsyncTask<Integer, Integer, Void> {
	
	private ExerciseActivity activity;
	
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
	protected void onProgressUpdate(Integer... values) {
		activity.displayBreakTime(values[0]);
	}
	
	@Override
	protected void onPostExecute(Void result) {
		activity.endOfBreak();
	}	
}
