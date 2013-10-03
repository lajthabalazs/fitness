package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.DataHelper;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Program;
import hu.droidium.fitness_app.database.ProgramProgress;
import hu.droidium.fitness_app.database.Workout;
import hu.droidium.fitness_app.database.WorkoutProgress;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
	private TextView programNameLabel;
	private TextView programStartDateLabel;
	private TextView exerciseListLabel;
	private TextView workoutStartDateLabel;
	private TextView workoutProgressLabel;
	private TextView currentWorkoutDescription;

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
		programNameLabel = (TextView)findViewById(R.id.programNameLabel);
		programStartDateLabel = (TextView)findViewById(R.id.programProgressDetailsStartDateLabel);
		programProgressBar = (ProgressBar)findViewById(R.id.programProgressBarOnProgressDetails);
		programDetailsText = (TextView)findViewById(R.id.programDescription);
		currentWorkoutLabel = (TextView)findViewById(R.id.currentWorkoutLabel);
		currentWorkoutText = (TextView)findViewById(R.id.currentWorkoutText);
		currentWorkoutDescription = (TextView)findViewById(R.id.currentWorkoutDescription);
		exerciseListLabel = (TextView)findViewById(R.id.exerciseListLabel);
		workoutStartDateLabel = (TextView)findViewById(R.id.workoutStartDateLabel);
		workoutProgressLabel = (TextView)findViewById(R.id.workoutProgressLabel);
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
		Program program  = databaseManager.getProgram(progress.getProgram().getId());
		String programStartDate = Constants.format(progress.getProgressId());
		programStartDateLabel.setText(String.format(getResources().getString(R.string.programStartedLabel), programStartDate));
		programNameLabel.setText(program.getName());
		programProgressBar.setProgress(progress.getProgressPercentage(databaseManager));
		programDetailsText.setText(program.getDescription());
		
		if (progress.getTerminationDate() != -1) {
			doWorkout.setVisibility(View.GONE);
			upcommingWorkoutsLabel.setVisibility(View.GONE);
			upcommingWorkoutsList.setVisibility(View.INVISIBLE); // This pushes button to the bottom, has to keep it's place
		} else {
			List<Workout> remainingWorkouts = progress.getRemainingWorkouts(databaseManager);
			if ((remainingWorkouts.size() > 0) &&
					(progress.getActualWorkout() == null) &&
					progress.isTodaysWorkout(remainingWorkouts.get(0))) {
				Workout todaysWorkout = remainingWorkouts.remove(0);
				progress.startWorkout(System.currentTimeMillis(), todaysWorkout, databaseManager);
			}
			// Actual workout box
			if (progress.getActualWorkout() != null) {
				WorkoutProgress workoutProgress = databaseManager.getWorkoutProgress(progress.getActualWorkout().getId());
				Workout workout = databaseManager.getWorkout(workoutProgress.getWorkout().getId());
				currentWorkoutLabel.setVisibility(View.VISIBLE);
				currentWorkoutText.setVisibility(View.VISIBLE);
				exerciseListLabel.setVisibility(View.VISIBLE);
				workoutStartDateLabel.setVisibility(View.VISIBLE);
				workoutProgressLabel.setVisibility(View.VISIBLE);
				currentWorkoutText.setText(workout.getName());
				String description = workout.getDescription();
				if (description != null) {
					currentWorkoutDescription.setVisibility(View.VISIBLE);
					currentWorkoutDescription.setText(description);
				} else {
					currentWorkoutDescription.setVisibility(View.GONE);
				}
				// Aggregated exercise types
				List<Pair<String, Integer>> sortedExercises = DataHelper.getSortedExercises(workout.getTotalReps(databaseManager));
				String exerciseList = "";
				for (int i = 0; i < 3 && i < sortedExercises.size(); i++) {
					if (i > 0) {
						exerciseList = exerciseList + ", ";
					}
					exerciseList = exerciseList + databaseManager.getExerciseType(sortedExercises.get(i).first).getName();
				}
				exerciseListLabel.setText(String.format(getResources().getString(R.string.exerciseListLabel), exerciseList));
				// Exercise due date
				doWorkout.setVisibility(View.VISIBLE);
				long today = Constants.stripDate(System.currentTimeMillis());
				// Workout has not been started
				if (workoutProgress.getWorkoutProgressExercisePercentage(databaseManager) == 0) {
					long workoutDate = progress.getWorkoutDate(workout);
					if (workoutDate < today) {
						// Already due
						int overdue = (int) ((today - workoutDate) / Constants.DAY_MILLIS);
						if (overdue == 1) {
							currentWorkoutLabel.setText(R.string.currentWorkoutLabel);
							workoutStartDateLabel.setText(R.string.workoutDueYesterdayDate);
						} else {
							currentWorkoutLabel.setText(R.string.currentWorkoutLabel);
							workoutStartDateLabel.setText(String.format(getResources().getString(R.string.workoutDuePastDate),overdue));
						}
					} else if (workoutDate > today) {
						// Due in future
						int timeLeft = (int) ((workoutDate - today) / Constants.DAY_MILLIS);
						if (timeLeft == 1) {
							currentWorkoutLabel.setText(R.string.currentWorkoutLabel);
							workoutStartDateLabel.setText(R.string.workoutDueTomorroDate);
						} else {
							currentWorkoutLabel.setText(R.string.currentWorkoutLabel);
							workoutStartDateLabel.setText(String.format(getResources().getString(R.string.workoutDueFutureDate),timeLeft));
						}	
					} else {
						currentWorkoutLabel.setText(R.string.todaysWorkoutLabel);
						workoutStartDateLabel.setVisibility(View.GONE);
					}
					workoutProgressLabel.setVisibility(View.GONE);
					doWorkout.setText(R.string.doTodaysWorkout);
				} else { // Workout already started
					long workoutStartDate = workoutProgress.getStartTime();
					int delay = (int) ((today - Constants.stripDate(workoutStartDate)) / Constants.DAY_MILLIS);
					int totalExercises = workout.getTotalNumberOfExercises(databaseManager);
					int doneExercises = workoutProgress.getDoneExercises().size();
					String workoutProgressText = doneExercises + "/" + totalExercises;
					if (delay == 0) {
						workoutStartDateLabel.setText(R.string.workoutStartToday);
					} else if (delay == 1) {
						workoutStartDateLabel.setText(R.string.workoutStartYesterday);						
					} else {
						workoutStartDateLabel.setText(String.format(getResources().getString(R.string.workoutStartDate), delay));
					}
					workoutProgressLabel.setText(String.format(getResources().getString(R.string.workoutProgressLabel), workoutProgressText));
					currentWorkoutLabel.setText(R.string.todaysWorkoutLabel);
					workoutStartDateLabel.setVisibility(View.VISIBLE);
					workoutProgressLabel.setVisibility(View.VISIBLE);
					doWorkout.setText(R.string.resumeWorkout);
				}
			} else {
				currentWorkoutLabel.setVisibility(View.GONE);
				currentWorkoutText.setVisibility(View.GONE);
				exerciseListLabel.setVisibility(View.GONE);
				workoutStartDateLabel.setVisibility(View.GONE);
				workoutProgressLabel.setVisibility(View.GONE);
				doWorkout.setVisibility(View.GONE);
			}
			// Upcomming workout box
			if (remainingWorkouts.size() == 0) {
				upcommingWorkoutsLabel.setVisibility(View.GONE);
				upcommingWorkoutsList.setVisibility(View.INVISIBLE); // This pushes button to the bottom, has to keep it's place
			} else {
				upcommingWorkoutsLabel.setVisibility(View.VISIBLE);
				upcommingWorkoutsList.setVisibility(View.VISIBLE);
				upcommingWorkoutsAdapter.clear();
				for (Workout workout : remainingWorkouts) {
					upcommingWorkoutsAdapter.add(workout);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, DoWorkoutActivity.class);
		ProgramProgress progress = databaseManager.getProgress(programId);
		Workout nextWorkout = progress.getNextWorkout(databaseManager);
		WorkoutProgress actualWorkout = progress.getActualWorkout(); 
		if (nextWorkout != null || actualWorkout != null) {
			if (actualWorkout != null && (actualWorkout.getFinishDate() != -1)) {
				// Actual workout is done, this should not happen
				Log.e(TAG, "Actual workout was set but done. Should not happen " + actualWorkout.getFinishDate());
				actualWorkout = null;
				progress.setActualWorkout(null);
				databaseManager.updateProgress(progress);
			}
			if (actualWorkout == null) {
				actualWorkout = progress.startWorkout(System.currentTimeMillis(), nextWorkout, databaseManager);
			}
			if (actualWorkout != null) {
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