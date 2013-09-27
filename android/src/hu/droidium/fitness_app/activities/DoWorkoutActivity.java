package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.BreakCountdown;
import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.WorkoutProgressView;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Exercise;
import hu.droidium.fitness_app.database.ProgramProgress;
import hu.droidium.fitness_app.database.Workout;
import hu.droidium.fitness_app.database.WorkoutProgress;
import hu.droidium.fitness_app.model.helpers.ProgramProgressHelper;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DoWorkoutActivity extends Activity implements OnClickListener {
	@SuppressWarnings("unused")
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
	private Button redoButton;
	private TextView exerciseLabel;
	private WorkoutProgress progress;
	private long endOfBreak = -1;
	private long startOfExercise = -1;
	private BreakCountdown task;
	private View endLayout;
	private DatabaseManager databaseManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		editReps = (Button)findViewById(R.id.editExercise);
		editReps.setOnClickListener(this);
		doneButton = (Button)findViewById(R.id.exerciseDone);
		doneButton.setOnClickListener(this);
		
		endLayout = findViewById(R.id.endOfExerciseLayout);
		redoButton = (Button)findViewById(R.id.restartWorkout);
		redoButton.setOnClickListener(this);
		
		databaseManager = DatabaseManager.getInstance(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (programProgressId != -1) {
			progress = ProgramProgressHelper.getActualWorkoutProgress(programProgressId, databaseManager);
			if (progress == null) {
				progressView.done();
				breakLayout.setVisibility(View.GONE);
				exerciseLayout.setVisibility(View.GONE);
				endLayout.setVisibility(View.VISIBLE);
			} else {
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
				
				Exercise exercise = ProgramProgressHelper.getExercise(progress, actualBlockIndex, actualExerciseIndex, databaseManager);
				if (endOfBreak == -1 || endOfBreak < now) {
					breakLayout.setVisibility(View.GONE);
					exerciseLayout.setVisibility(View.VISIBLE);
					exerciseLabel.setText(exercise.getType().getName());
					String unit = exercise.getType().getName();
					reps.setText(exercise.getReps() + (unit == null?"":" " + unit));
					endLayout.setVisibility(View.GONE);
					progressView.setActiveExcercise(actualBlockIndex, actualExerciseIndex);
				} else {
					if (task != null) {
						task.cancel(true);
					}
					task = new BreakCountdown(this);
					int remainingSecs = (int) ((endOfBreak - now) / 1000);
					breakDuration.setText(remainingSecs + " " + getResources().getString(R.string.secs));
					breakLayout.setVisibility(View.VISIBLE);
					exerciseLayout.setVisibility(View.GONE);
					endLayout.setVisibility(View.GONE);
					task.execute(remainingSecs);
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
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.exerciseDone : {
				long now = System.currentTimeMillis();
				int actualBlockIndex = progress.getActualBlock();
				int actualExerciseIndex = progress.getActualExercise();
				ProgramProgress programProgress = databaseManager.getProgress(programProgressId);
				WorkoutProgress workoutProgress = programProgress.getActualWorkout();
				if (workoutProgress != null) {
					Exercise exercise = ProgramProgressHelper.getExercise(workoutProgress, actualBlockIndex, actualExerciseIndex, databaseManager);
					progress.exerciseDone(exercise, exercise.getReps(), now - startOfExercise, now, databaseManager);
					if (progress.getFinishDate() == -1) {
						endOfBreak = now + 1000 * exercise.getBreakSecs();
					}
				}
				progressChanged();
				break;
			}
			case R.id.continueWorkout : {
				endOfBreak();
				progressChanged();
				break;
			}
			case R.id.restartWorkout : {
				// TODO new progress
				progressChanged();
				break;
			}
			default : {
				Toast.makeText(this, "Feature not yet implemented", Toast.LENGTH_LONG).show();
			}
		}
	}

	public void displayBreakTime(int remaining) {
		breakDuration.setText(remaining + " " + getResources().getString(R.string.secs));
	}
	
	public void endOfBreak() {
		endOfBreak = -1;
		long now = System.currentTimeMillis();
		int actualBlockIndex = progress.getActualBlock();
		int actualExerciseIndex = progress.getActualExercise();
		ProgramProgress programProgress = databaseManager.getProgress(programProgressId);
		WorkoutProgress workoutProgress = programProgress.getActualWorkout();
		Exercise exercise = ProgramProgressHelper.getExercise(workoutProgress, actualBlockIndex, actualExerciseIndex, databaseManager);
		this.exerciseLabel.setText(exercise.getType() + "");
		this.startOfExercise = now;
		this.reps.setText("" + exercise.getReps());
		progressChanged();
	}
}