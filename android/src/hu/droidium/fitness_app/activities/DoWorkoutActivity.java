package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.BreakCountdown;
import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.WorkoutProgressView;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Exercise;
import hu.droidium.fitness_app.database.ExerciseType;
import hu.droidium.fitness_app.database.ProgramProgress;
import hu.droidium.fitness_app.database.Workout;
import hu.droidium.fitness_app.database.WorkoutProgress;

import java.security.InvalidParameterException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DoWorkoutActivity extends Activity implements OnClickListener {
	private static final String TAG = "ExerciseActivity";
	private WorkoutProgressView progressView;
	private long programProgressId;
	private View exerciseLayout;
	private View breakLayout;
	private TextView breakDuration;
	private TextView reps;
	private Button addFifteenButton;
	private Button continueWorkout;
	private Button editReps;
	private Button doneButton;
	private Button backToWorkouts;
	private TextView exerciseLabel;
	private WorkoutProgress progress;
	private long endOfBreak = -1;
	private long startOfExercise = -1;
	private BreakCountdown breakCountdown;
	private View endLayout;
	private DatabaseManager databaseManager;
	private TextView repsLabel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		databaseManager = DatabaseManager.getInstance(this);
		setContentView(R.layout.do_workout_activity);
		programProgressId = getIntent().getLongExtra(Constants.PROGRAM_PROGRESS_ID, -1);

		progressView = (WorkoutProgressView)findViewById(R.id.workoutProgress);
		breakLayout = findViewById(R.id.breakLayout);
		breakDuration = (TextView)findViewById(R.id.breakDuration);
		addFifteenButton = (Button)findViewById(R.id.addFifteen);
		addFifteenButton.setOnClickListener(this);
		continueWorkout = (Button)findViewById(R.id.continueWorkout);
		continueWorkout.setOnClickListener(this);
		
		exerciseLayout = findViewById(R.id.exerciseLayout);
		exerciseLabel = (TextView)findViewById(R.id.exerciseLabel);
		reps = (TextView)findViewById(R.id.reps);
		repsLabel = (TextView)findViewById(R.id.repsLabel);
		editReps = (Button)findViewById(R.id.editExercise);
		editReps.setOnClickListener(this);
		doneButton = (Button)findViewById(R.id.exerciseDone);
		doneButton.setOnClickListener(this);
		
		backToWorkouts = (Button)findViewById(R.id.backToWorkoutList);
		backToWorkouts.setOnClickListener(this);
		
		endLayout = findViewById(R.id.endOfExerciseLayout);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (programProgressId != -1) {
			ProgramProgress programProgress = databaseManager.getProgress(programProgressId);
			progress = programProgress.getActualWorkout();
			if (progress == null) {
				progressView.done();
				breakLayout.setVisibility(View.GONE);
				exerciseLayout.setVisibility(View.GONE);
				endLayout.setVisibility(View.VISIBLE);
			} else {
				// Needs deep loading
				progress = databaseManager.getWorkoutProgress(progress.getId());
				Workout workout = databaseManager.getWorkout(progress.getWorkout().getId());
				progressView.setWorkout(workout);
				progressChanged();
			}
		}
	}

	private void progressChanged() {
		long now = System.currentTimeMillis();
		if (progress.getFinishDate() == -1) {
			int actualBlockIndex = progress.getActualBlock();
			int actualExerciseIndex = progress.getActualExercise();
			if (progress == null) {
				// TODO this should not happen
				progressView.done();
				breakLayout.setVisibility(View.GONE);
				exerciseLayout.setVisibility(View.GONE);
				endLayout.setVisibility(View.VISIBLE);
			} else {
				Exercise exercise = progress.getExercise(actualBlockIndex, actualExerciseIndex, databaseManager);
				ExerciseType exerciseType = databaseManager.getExerciseType(exercise.getType().getId());
				if (endOfBreak == -1 || endOfBreak < now) {
					breakLayout.setVisibility(View.GONE);
					exerciseLayout.setVisibility(View.VISIBLE);
					exerciseLabel.setText(exerciseType.getName());
					String unit = exerciseType.getUnit();
					reps.setText("" + exercise.getReps());
					if (unit == null) {
						repsLabel.setVisibility(View.GONE);
					} else {
						repsLabel.setVisibility(View.VISIBLE);
						repsLabel.setText(unit);
					}
					endLayout.setVisibility(View.GONE);
					progressView.setActiveExcercise(actualBlockIndex, actualExerciseIndex);
				} else {
					if (breakCountdown != null) {
						breakCountdown.cancel(true);
					}
					breakCountdown = new BreakCountdown(this);
					int remainingSecs = (int) ((endOfBreak - now) / 1000);
					breakDuration.setText("" + remainingSecs);
					breakLayout.setVisibility(View.VISIBLE);
					exerciseLayout.setVisibility(View.GONE);
					endLayout.setVisibility(View.GONE);
					breakCountdown.execute(remainingSecs);
					progressView.setActiveBreak(actualBlockIndex, actualExerciseIndex);
				}
			}
		} else {
			progressView.done();
			breakLayout.setVisibility(View.GONE);
			exerciseLayout.setVisibility(View.GONE);
			endLayout.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onPause() {
		if (breakCountdown != null) {
			breakCountdown.cancel(true);
		}
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.exerciseDone : {
				exerciseDone();
				break;
			}
			case R.id.continueWorkout : {
				endOfBreak();
				progressChanged();
				break;
			}
			case R.id.backToWorkoutList : {
				finish();
				break;
			} case R.id.addFifteen : {
				if (breakCountdown != null && breakCountdown.getRemainingTimeInSecs() > 0) {
					endOfBreak += 15000;
					breakCountdown.addSecs(15);
				} 
				break;
			} case R.id.editExercise : {
				AlertDialog.Builder builder = new Builder(this);
				builder.setTitle(R.string.editRepsDialogTitle);
				final EditText input = new EditText(this);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				input.setGravity(Gravity.CENTER);
				builder.setView(input);
				builder.setPositiveButton(R.string.cancel, null);
				builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							int reps = Integer.parseInt(input.getText().toString());
							exerciseDone(reps);
						} catch (Exception e) {
							exerciseDone();
						}
					}
				});
				builder.create();
				builder.show();
				break;
			}
			default : {
				Toast.makeText(this, "Feature not yet implemented", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void exerciseDone(){
		exerciseDone(-1);
	}

	private void exerciseDone(int reps) {
		long now = System.currentTimeMillis();
		int actualBlockIndex = progress.getActualBlock();
		int actualExerciseIndex = progress.getActualExercise();
		ProgramProgress programProgress = databaseManager.getProgress(programProgressId);
		WorkoutProgress workoutProgress = programProgress.getActualWorkout();
		if (workoutProgress != null) {
			Log.i(TAG, "Done exercise " + actualExerciseIndex + " of block " + actualBlockIndex);
			Exercise exercise = workoutProgress.getExercise(actualBlockIndex, actualExerciseIndex, databaseManager);
			if (reps == -1) {
				reps = exercise.getReps();
			}
			progress.exerciseDone(programProgress, exercise, reps, now - startOfExercise, now, databaseManager);
			if (progress.getFinishDate() == -1) {
				endOfBreak = now + 1000 * exercise.getBreakSecs();
			}
		} else {
			throw new InvalidParameterException("There should be an actual workout!");
		}
		progressChanged();
	}

	public void displayBreakTime(int remaining) {
		breakDuration.setText("" + remaining);
	}
	
	public void endOfBreak() {
		endOfBreak = -1;
		long now = System.currentTimeMillis();
		int actualBlockIndex = progress.getActualBlock();
		int actualExerciseIndex = progress.getActualExercise();
		ProgramProgress programProgress = databaseManager.getProgress(programProgressId);
		WorkoutProgress workoutProgress = programProgress.getActualWorkout();
		Exercise exercise = workoutProgress.getExercise(actualBlockIndex, actualExerciseIndex, databaseManager);
		this.exerciseLabel.setText(exercise.getType() + "");
		this.startOfExercise = now;
		this.reps.setText("" + exercise.getReps());
		progressChanged();
	}
}