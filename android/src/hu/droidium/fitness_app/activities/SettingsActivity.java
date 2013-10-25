package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingsActivity extends FitnessBaseActivity implements OnCheckedChangeListener, OnClickListener {
	private CheckBox flurryCheck;
	private CheckBox showDone;
	private CheckBox sounds;
	private CheckBox notifications;
	private Button resetData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
		flurryCheck = (CheckBox)findViewById(R.id.settingsFlurry);
		flurryCheck.setChecked(isFlurryEnabled());
		flurryCheck.setOnCheckedChangeListener(this);
		
		showDone = (CheckBox)findViewById(R.id.settingsShowDone);
		showDone.setChecked(prefs.getBoolean(Constants.SETTINGS_SHOW_DONE_PROGRAMS, true));
		showDone.setOnCheckedChangeListener(this);
		
		sounds = (CheckBox)findViewById(R.id.settingsEnableSound);
		sounds.setChecked(prefs.getBoolean(Constants.SETTINGS_SOUND, false));
		sounds.setOnCheckedChangeListener(this);
		
		notifications = (CheckBox)findViewById(R.id.settingsEnableNotifications);
		notifications.setChecked(prefs.getBoolean(Constants.SETTINGS_NOTIFICATIONS, false));
		notifications.setOnCheckedChangeListener(this);
		
		resetData = (Button) findViewById(R.id.resetDataButton);
		resetData.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
		resetData.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch(buttonView.getId()) {
			case R.id.settingsFlurry : {
				if (isChecked) {
					enableFlurry();
				} else {
					disableFlurry();
				}
				break;
			}
			case R.id.settingsShowDone : {
				SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
				prefs.edit().putBoolean(Constants.SETTINGS_SHOW_DONE_PROGRAMS, isChecked);
				break;
			}
			case R.id.settingsEnableSound : {
				SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
				prefs.edit().putBoolean(Constants.SETTINGS_SOUND, isChecked);
				break;
			}
			case R.id.settingsEnableNotifications : {
				SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
				prefs.edit().putBoolean(Constants.SETTINGS_NOTIFICATIONS, isChecked);
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		Builder builder = new Builder(this);
		builder.setTitle(R.string.deleteAllProgramProgressTitle);
		builder.setMessage(R.string.deleteAllProgramProgressMessage);
		builder.setPositiveButton(R.string.deleteAllProgramProgressDelete, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DatabaseManager databaseManager = DatabaseManager.getInstance(SettingsActivity.this);
				databaseManager.removeAllUserData(SettingsActivity.this);
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}
}
