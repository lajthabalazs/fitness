package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.FlurryLogConstants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.Translator;
import hu.droidium.fitness_app.UpcomingWorkoutAdapter;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Program;
import hu.droidium.fitness_app.database.ProgramProgress;
import hu.droidium.fitness_app.database.Workout;
import hu.droidium.fitness_app.database.WorkoutProgress;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProgramProgressDetailsActivity extends FitnessBaseActivity implements OnClickListener {
	
	private ProgressBar programProgressBar;
	private TextView programDetailsText;
	private Button doWorkout;
	private Button backToPrograms;
	private long programId;
	private DatabaseManager databaseManager;
	private View upcomingWorkoutsLabel;
	private ListView upcomingWorkoutsList;
	private UpcomingWorkoutAdapter upcomingWorkoutAdapter;
	private TextView currentWorkoutLabel;
	private TextView currentWorkoutText;
	private TextView programNameLabel;
	private TextView programStartDateLabel;
	private TextView exerciseListLabel;
	private TextView workoutStartDateLabel;
	private TextView workoutProgressLabel;
	private TextView currentWorkoutDescription;
	private ProgramProgress programProgress;
	private TextView currentWorkoutUnits;
	private TextView currentWorkoutTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		databaseManager = DatabaseManager.getInstance(this);
		setContentView(R.layout.program_progress_details_activity);
		if (savedInstanceState != null && savedInstanceState.containsKey(Constants.PROGRAM_PROGRESS_ID)) {
			programId = savedInstanceState.getLong(Constants.PROGRAM_PROGRESS_ID);
		} else if (getIntent() != null && getIntent().getExtras().containsKey(Constants.PROGRAM_PROGRESS_ID)){
			programId = getIntent().getExtras().getLong(Constants.PROGRAM_PROGRESS_ID);
		} else {
			Toast.makeText(this, "Couldn't start activity, no workout program specified", Toast.LENGTH_LONG).show();
			finish();
		}
		programProgress = databaseManager.getProgress(programId);
		programNameLabel = (TextView)findViewById(R.id.programNameLabel);
		programStartDateLabel = (TextView)findViewById(R.id.programProgressDetailsStartDateLabel);
		programProgressBar = (ProgressBar)findViewById(R.id.programProgressBarOnProgressDetails);
		programDetailsText = (TextView)findViewById(R.id.programDescription);
		currentWorkoutLabel = (TextView)findViewById(R.id.currentWorkoutLabel);
		currentWorkoutText = (TextView)findViewById(R.id.currentWorkoutText);
		currentWorkoutUnits = (TextView)findViewById(R.id.currentWorkoutUnits);
		currentWorkoutTime = (TextView)findViewById(R.id.currentWorkoutTime);
		currentWorkoutDescription = (TextView)findViewById(R.id.currentWorkoutDescription);
		exerciseListLabel = (TextView)findViewById(R.id.exerciseListLabel);
		workoutStartDateLabel = (TextView)findViewById(R.id.workoutStartDateLabel);
		workoutProgressLabel = (TextView)findViewById(R.id.workoutProgressLabel);
		upcomingWorkoutsLabel = findViewById(R.id.upcommingWorkoutsLabel);
		upcomingWorkoutsList = (ListView)findViewById(R.id.upcommingWorkoutsList);
		upcomingWorkoutAdapter = new UpcomingWorkoutAdapter(programProgress, this, databaseManager, getLayoutInflater());
		upcomingWorkoutsList.setAdapter(upcomingWorkoutAdapter);
		doWorkout = (Button)findViewById(R.id.doTodaysWorkoutOnProgressDetails);
		doWorkout.setOnClickListener(this);
		backToPrograms = (Button)findViewById(R.id.backToProgramList);
		backToPrograms.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateUI();
	}
	
	private void updateUI() {
		programProgress = databaseManager.getProgress(programId);
		Program program  = programProgress.getProgram();
		program.refresh(databaseManager);
		String programStartDate = Constants.format(programProgress.getProgressId());
		programStartDateLabel.setText(String.format(getResources().getString(R.string.programStartedLabel), programStartDate));
		programNameLabel.setText(Translator.getTranslation(program.getName()));
		programProgressBar.setProgress(programProgress.getProgressPercentage(databaseManager));
		programDetailsText.setText(Translator.getTranslation(program.getDescription()));
		
		if (programProgress.getTerminationDate() != -1) {
			String programEndDate = Constants.format(programProgress.getTerminationDate());
			programStartDateLabel.setText(String.format(getResources().getString(R.string.programDoneLabel), programEndDate));
			programStartDateLabel.setVisibility(View.VISIBLE);
			doWorkout.setVisibility(View.GONE);
			upcomingWorkoutsLabel.setVisibility(View.GONE);
			upcomingWorkoutsList.setVisibility(View.INVISIBLE);
			currentWorkoutLabel.setVisibility(View.GONE);
			currentWorkoutText.setVisibility(View.GONE);
			currentWorkoutTime.setVisibility(View.GONE);
			currentWorkoutDescription.setVisibility(View.GONE);
			currentWorkoutUnits.setVisibility(View.GONE);
			exerciseListLabel.setVisibility(View.GONE);
			workoutStartDateLabel.setVisibility(View.GONE);
			workoutProgressLabel.setVisibility(View.GONE);
			doWorkout.setVisibility(View.GONE);
		} else {
			List<Workout> remainingWorkouts = programProgress.getRemainingWorkouts(databaseManager);
			if ((remainingWorkouts.size() > 0) &&
					(programProgress.getActualWorkout() == null) &&
					programProgress.isTodaysWorkout(remainingWorkouts.get(0))) {
				Workout todaysWorkout = remainingWorkouts.remove(0);
				programProgress.startWorkout(System.currentTimeMillis(), todaysWorkout, databaseManager);
			}
			// Actual workout box
			if (programProgress.getActualWorkout() != null) {
				WorkoutProgress workoutProgress = programProgress.getActualWorkout();
				workoutProgress.refresh(databaseManager);
				Workout workout = workoutProgress.getWorkout();
				workout.refresh(databaseManager);
				currentWorkoutLabel.setVisibility(View.VISIBLE);
				currentWorkoutText.setVisibility(View.VISIBLE);
				exerciseListLabel.setVisibility(View.VISIBLE);
				currentWorkoutTime.setVisibility(View.VISIBLE);
				currentWorkoutUnits.setVisibility(View.VISIBLE);
				workoutStartDateLabel.setVisibility(View.VISIBLE);
				workoutProgressLabel.setVisibility(View.VISIBLE);
				currentWorkoutText.setText(Translator.getTranslation(workout.getName()));
				String description = workout.getDescription();
				if (description != null) {
					currentWorkoutDescription.setVisibility(View.VISIBLE);
					currentWorkoutDescription.setText(Translator.getTranslation(description));
				} else {
					currentWorkoutDescription.setVisibility(View.GONE);
				}
				String timeText = "";
				int secs = (int) workout.getTotalTime(databaseManager);
				if (secs > 3600) {
					int hours = secs / 3600;
					int minutes = (secs - 3600 * hours) / 60;
					secs = secs - 3600 * hours - minutes * 60;
					timeText = String.format(getString(R.string.estimatedTimeWithHour), hours, minutes, secs);
				} else {
					int minutes = secs / 60;
					secs = secs - minutes * 60;
					timeText = String.format(getString(R.string.estimatedTimeWithMinutes), minutes, secs);
				}
				String unitsString = String.format(getString(R.string.totalUnits), (int)workout.getTotalUnits(databaseManager));
				currentWorkoutTime.setText(timeText);
				currentWorkoutUnits.setText(unitsString);
				String exerciseList = workout.getExercisesList(3, false, this, databaseManager);
				exerciseListLabel.setText(String.format(getResources().getString(R.string.exerciseListLabel), exerciseList));
				// Exercise due date
				doWorkout.setVisibility(View.VISIBLE);
				String dueDateString = workout.getDueDateString(programProgress.getProgressId(), databaseManager, this);
				long today = Constants.stripDate(System.currentTimeMillis());
				// Workout has not been started
				if (workoutProgress.getWorkoutProgressExercisePercentage(databaseManager) == 0) {
					workoutProgressLabel.setVisibility(View.GONE);
					
					long workoutDate = programProgress.getWorkoutDate(workout);
					if (workoutDate < today) {
						currentWorkoutLabel.setText(R.string.currentWorkoutLabel);
						doWorkout.setText(R.string.doTodaysWorkout);
						// Already due
					} else if (workoutDate > today) {
						// Due in future
						currentWorkoutLabel.setText(R.string.currentWorkoutLabel);
						doWorkout.setText(R.string.doThisWorkout);
					} else {
						currentWorkoutLabel.setText(R.string.todaysWorkoutLabel);
						doWorkout.setText(R.string.doTodaysWorkout);
						workoutStartDateLabel.setVisibility(View.GONE);
					}
					workoutStartDateLabel.setText(dueDateString);
				} else { // Workout already started
					long workoutStartDate = workoutProgress.getStartTime();
					int delay = (int) ((today - Constants.stripDate(workoutStartDate)) / Constants.DAY_MILLIS);
					int totalExercises = workout.getTotalNumberOfExercises(databaseManager);
					int doneExercises = workoutProgress.getDoneExercises().size();
					if (delay == 0) {
						workoutStartDateLabel.setText(R.string.workoutStartToday);
					} else if (delay == 1) {
						workoutStartDateLabel.setText(R.string.workoutStartYesterday);						
					} else {
						workoutStartDateLabel.setText(String.format(getResources().getString(R.string.workoutStartDate), delay));
					}
					workoutProgressLabel.setText(String.format(getResources().getString(R.string.workoutProgressLabel), doneExercises, totalExercises));
					currentWorkoutLabel.setText(R.string.todaysWorkoutLabel);
					workoutStartDateLabel.setVisibility(View.VISIBLE);
					workoutProgressLabel.setVisibility(View.VISIBLE);
					doWorkout.setText(R.string.resumeWorkout);
				}
			} else {

				currentWorkoutLabel.setVisibility(View.GONE);
				currentWorkoutText.setVisibility(View.GONE);
				exerciseListLabel.setVisibility(View.GONE);
				currentWorkoutDescription.setVisibility(View.GONE);				
				currentWorkoutTime.setVisibility(View.GONE);
				currentWorkoutUnits.setVisibility(View.GONE);
				workoutStartDateLabel.setVisibility(View.GONE);
				workoutProgressLabel.setVisibility(View.GONE);
				doWorkout.setVisibility(View.GONE);
			}
			// Upcomming workout box
			if (remainingWorkouts.size() == 0) {
				upcomingWorkoutsLabel.setVisibility(View.GONE);
				upcomingWorkoutsList.setVisibility(View.INVISIBLE); // This pushes button to the bottom, has to keep it's place
			} else {
				upcomingWorkoutsLabel.setVisibility(View.VISIBLE);
				upcomingWorkoutsList.setVisibility(View.VISIBLE);
				upcomingWorkoutAdapter.setWorkouts(remainingWorkouts, programProgress.getActualWorkout() != null);
			}
		}
	}
	
	public void startWorkout() {
		Intent intent = new Intent(this, DoWorkoutActivity.class);
		ProgramProgress progress = databaseManager.getProgress(programId);
		Workout nextWorkout = progress.getNextWorkout(databaseManager);
		WorkoutProgress actualWorkout = progress.getActualWorkout(); 
		if (nextWorkout != null || actualWorkout != null) {
			if (actualWorkout != null && (actualWorkout.getFinishDate() != -1)) {
				// Actual workout is done, this should not happen
				actualWorkout = null;
				progress.setActualWorkout(null);
				databaseManager.updateProgress(progress);
			}
			if (actualWorkout == null) {
				actualWorkout = progress.startWorkout(System.currentTimeMillis(), nextWorkout, databaseManager);
			}
			if (actualWorkout != null) {
				actualWorkout.refresh(databaseManager);
				// Log with flurry
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(Constants.PROGRAM_ID_KEY, progress.getProgram().getId());
				params.put(Constants.WORKOUT_ID, actualWorkout.getWorkout().getId());
				log(FlurryLogConstants.STARTED_WORKOUT, params);
				
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

	public void skipToWorkout(int index, final Workout workout) {
		if (index == 0 && programProgress.getActualWorkout() == null) {
			// Start next workout, no questions asked
			programProgress.startWorkout(System.currentTimeMillis(), workout, databaseManager);
			log(FlurryLogConstants.STARTED_WORKOUT_EARLY);
			startWorkout();
		} else if (index == 0 && programProgress.getActualWorkout() != null){
			Builder builder = new Builder(this);
			builder.setTitle(R.string.skipCurrentWorkoutTitle);
			builder.setMessage(R.string.skipCurrentWorkoutMessage);
			builder.setPositiveButton(R.string.skipWorkoutYes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					programProgress.startWorkout(System.currentTimeMillis(), workout, databaseManager);
					startWorkout();
				}
			});
			builder.setNegativeButton(R.string.skipWorkoutNo, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			builder.create().show();
		} else {
			Builder builder = new Builder(this);
			builder.setTitle(R.string.skipWorkoutsTitle);
			builder.setMessage(R.string.skipWorkoutsMessage);
			builder.setPositiveButton(R.string.skipWorkoutYes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					programProgress.startWorkout(System.currentTimeMillis(), workout, databaseManager);
					startWorkout();
				}
			});
			builder.setNegativeButton(R.string.skipWorkoutNo, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			builder.create().show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.doTodaysWorkoutOnProgressDetails: {
				startWorkout();
				break;
			}
			case R.id.backToProgramList: {
				finish();
				break;
			}
		}
	}
}