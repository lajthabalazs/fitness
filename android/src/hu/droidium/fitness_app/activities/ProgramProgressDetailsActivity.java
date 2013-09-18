package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.ProgramProgress;
import hu.droidium.fitness_app.model.helpers.ProgramProgressHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProgramProgressDetailsActivity extends Activity implements OnClickListener {
	
	private ProgressBar programProgressBar;
	private TextView programDetailsText;
	private TextView nextWorkoutText;
	private Button doWorkout;
	private long programId;
	private DatabaseManager databaseManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.program_progress_details_activity);
		if (savedInstanceState != null && savedInstanceState.containsKey(Constants.PROGRAM_PROGRESS_ID)) {
			programId = savedInstanceState.getLong(Constants.PROGRAM_PROGRESS_ID);
		} else if (getIntent() != null && getIntent().getExtras().containsKey(Constants.PROGRAM_PROGRESS_ID)){
			programId = getIntent().getExtras().getLong(Constants.PROGRAM_PROGRESS_ID);
		} else {
			Toast.makeText(this, "Couldn't start activity, no workout program specified", Toast.LENGTH_LONG).show();
			finish();
		}
		programProgressBar = (ProgressBar)findViewById(R.id.programProgressBarOnProgressDetails);
		programDetailsText = (TextView)findViewById(R.id.programDescription);
		nextWorkoutText = (TextView)findViewById(R.id.nextWorkoutOnProgressDetails);
		doWorkout = (Button)findViewById(R.id.doTodaysWorkoutOnProgressDetails);
		doWorkout.setOnClickListener(this);
		databaseManager = DatabaseManager.getInstance(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ProgramProgress progress = databaseManager.getProgress(programId);
		setTitle(progress.getProgram().getName());
		programProgressBar.setProgress(progress.getProgressPercentage());
		programDetailsText.setText(progress.getProgram().getDescription());
		nextWorkoutText.setText(ProgramProgressHelper.getDateOfNextWorkoutText(progress, this));
		if (progress.getTerminationDate() != -1) {
			doWorkout.setVisibility(View.GONE);
		}
		int days = progress.getDaysTilNextWorkout();
		if (days == 0) {
			doWorkout.setText(R.string.doTodaysWorkout);
		} else {
			doWorkout.setText(R.string.doNextWorkoutToday);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, DoWorkoutActivity.class);
		ProgramProgress progress = databaseManager.getProgress(programId);
		progress.getNextWorkoutId();
		intent.putExtra(Constants.WORKOUT_ID, progress.getNextWorkoutId());
		startActivity(intent);
	}
}
