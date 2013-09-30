package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.ProgramProgress;
import hu.droidium.fitness_app.database.Workout;
import hu.droidium.fitness_app.database.WorkoutProgress;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProgramProgressDetailsActivity extends Activity implements OnClickListener {
	
	private static final String TAG = "ProgramProgressDetailsActivity";
	private ProgressBar programProgressBar;
	private TextView programDetailsText;
	private Button doWorkout;
	private long programId;
	private DatabaseManager databaseManager;
	private View upcommingWorkoutsLabel;
	private ListView upcommingWorkoutsList;
	private ArrayAdapter<Workout> upcommingWorkoutsAdapter;
	private TextView currentWorkoutLabel;
	private TextView currentWorkoutText;

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
		currentWorkoutLabel = (TextView)findViewById(R.id.currentWorkoutLabel);
		currentWorkoutText = (TextView)findViewById(R.id.currentWorkoutText);
		upcommingWorkoutsLabel = findViewById(R.id.upcommingWorkoutsLabel);
		upcommingWorkoutsList = (ListView)findViewById(R.id.upcommingWorkoutsList);
		upcommingWorkoutsAdapter = new ArrayAdapter<Workout>(this, android.R.layout.simple_list_item_1);
		upcommingWorkoutsList.setAdapter(upcommingWorkoutsAdapter);
		doWorkout = (Button)findViewById(R.id.doTodaysWorkoutOnProgressDetails);
		doWorkout.setOnClickListener(this);
		databaseManager = DatabaseManager.getInstance(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ProgramProgress progress = databaseManager.getProgress(programId);
		progress.setProgram(databaseManager.getProgram(progress.getProgram().getId()));
		setTitle(progress.getProgram().getName());
		programProgressBar.setProgress(progress.getProgressPercentage(databaseManager));
		programDetailsText.setText(progress.getProgram().getDescription());
		
		if (progress.getTerminationDate() != -1) {
			doWorkout.setVisibility(View.GONE);
			upcommingWorkoutsLabel.setVisibility(View.GONE);
			upcommingWorkoutsList.setVisibility(View.INVISIBLE); // This pushes button to the bottom, has to keep it's place
		} else {
			// We have workouts
			List<Workout> remainingWorkouts = progress.getRemainingWorkouts(databaseManager);
			upcommingWorkoutsAdapter.clear();
			for (Workout workout : remainingWorkouts) {
				upcommingWorkoutsAdapter.add(workout);
			}
		}
		if (progress.getActualWorkout() != null) {
			currentWorkoutLabel.setVisibility(View.VISIBLE);
			currentWorkoutText.setVisibility(View.VISIBLE);
			currentWorkoutText.setText(progress.getActualWorkout().getProgressText(databaseManager));
		} else {
			currentWorkoutLabel.setVisibility(View.GONE);
			currentWorkoutText.setVisibility(View.GONE);
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
		progress.setProgram(databaseManager.getProgram(progress.getProgram().getId()));
		Workout nextWorkout = progress.getNextWorkout();
		WorkoutProgress actualWorkout = progress.getActualWorkout(); 
		if (nextWorkout != null || actualWorkout != null) {
			if (actualWorkout != null && (actualWorkout.getFinishDate() != -1)) {
				// Actual workout is done
				Log.e(TAG, "Actual workout was set but done. Should not happen " + actualWorkout.getFinishDate());
				actualWorkout = null;
				progress.setActualWorkout(null);
				databaseManager.updateProgress(progress);
			}
			if (actualWorkout == null) {
				actualWorkout = new WorkoutProgress(progress, nextWorkout);
				if (!databaseManager.addWorkoutProgress(actualWorkout)){
					actualWorkout = null;
				}
			}
			if (actualWorkout != null) {
				progress.setActualWorkout(actualWorkout);
				databaseManager.updateProgress(progress);
				intent.putExtra(Constants.PROGRAM_PROGRESS_ID, progress.getProgressId());
				intent.putExtra(Constants.WORKOUT_PROGRESS_ID, actualWorkout.getId());
				startActivity(intent);
			} else {
				Toast.makeText(this, "Workout progress couldn't be created", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "No next workout", Toast.LENGTH_LONG).show();
		}
	}
}
