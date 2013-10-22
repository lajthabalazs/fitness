package hu.droidium.fitness_app.activities;

import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.util.Log;

public class FitnessBaseActivity extends Activity {
	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.setUserId("Hello user");
		Log.e("FlurrySession", "Starting session");
		FlurryAgent.onStartSession(this, "FXTJ4F3Y4GJ24PHFV2TJ");
		FlurryAgent.logEvent("WTF");
	}

	@Override
	protected void onStop() {
		FlurryAgent.onEndSession(this);
		Log.e("FlurrySession", "Session ended");
		super.onStop();
	}
}
