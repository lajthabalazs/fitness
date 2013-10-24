package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.R;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

import com.flurry.android.FlurryAgent;

public class FitnessBaseActivity extends Activity {
	
	private static final String FLURRY_PREFS = "Flurry preferences";
	private static final String FLURRY_ENABLED_KEY = "Flurry enabled key";
	private static final String FLURRY_USER_ID_KEY = "Flurry user id";
	private static final int FLURRY_UNKNOWN = -1;
	private static final int FLURRY_DISABLED = 1;
	private static final int FLURRY_ENABLED = 0;
	private SharedPreferences prefs;
	@Override
	protected void onStart() {
		super.onStart();
		prefs = getSharedPreferences(FLURRY_PREFS, Context.MODE_PRIVATE);
		int flurryDecision = prefs.getInt(FLURRY_ENABLED_KEY, FLURRY_UNKNOWN);
		if (flurryDecision == FLURRY_ENABLED) {
			long userId = prefs.getLong(FLURRY_USER_ID_KEY, -1);
			Log.e("FlurrySession", "Starting session");
			FlurryAgent.setUserId("" + userId);
			FlurryAgent.onStartSession(this, "FXTJ4F3Y4GJ24PHFV2TJ");
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		int flurryDecision = prefs.getInt(FLURRY_ENABLED_KEY, FLURRY_UNKNOWN);
		if (flurryDecision == FLURRY_UNKNOWN) {
			Builder builder = new Builder(this);
			builder.setTitle(R.string.flurryQuestionTitle);
			builder.setMessage(R.string.flurryQuestionMessage);
			builder.setPositiveButton(R.string.flurryQuestionPositive, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					prefs.edit().putLong(FLURRY_USER_ID_KEY, (long)(Math.random() * 1000000000)).
						putInt(FLURRY_ENABLED_KEY, FLURRY_ENABLED).commit();
				}
			});
			builder.setNegativeButton(R.string.flurryQuestionNegative, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					prefs.edit().putInt(FLURRY_ENABLED_KEY, FLURRY_DISABLED).commit();
				}
			});
			builder.create();
			builder.show();
		}
	}
	
	protected void log(String event) {
		int flurryDecision = prefs.getInt(FLURRY_ENABLED_KEY, FLURRY_UNKNOWN);
		if (flurryDecision == FLURRY_ENABLED) {
			FlurryAgent.logEvent(event);
		}
	}

	protected void log(String event, Map<String, String> params) {
		int flurryDecision = prefs.getInt(FLURRY_ENABLED_KEY, FLURRY_UNKNOWN);
		if (flurryDecision == FLURRY_ENABLED) {
			FlurryAgent.logEvent(event, params);
		}
	}

	@Override
	protected void onStop() {
		prefs = getSharedPreferences(FLURRY_PREFS, Context.MODE_PRIVATE);
		int flurryDecision = prefs.getInt(FLURRY_ENABLED_KEY, FLURRY_UNKNOWN);
		if (flurryDecision == FLURRY_ENABLED) {
			FlurryAgent.onEndSession(this);
			Log.e("FlurrySession", "Session ended");
		}
		super.onStop();
	}
}
