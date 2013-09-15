package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.BreakCountdown;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.WorkoutProgressView;
import hu.droidium.fitness_app.R.id;
import hu.droidium.fitness_app.R.layout;
import hu.droidium.fitness_app.R.string;
import hu.droidium.fitness_app.model.Exercise;
import hu.droidium.fitness_app.model.Workout;
import hu.droidium.fitness_app.model.WorkoutProgress;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.newrelic.agent.android.NewRelic;

public class DoWorkoutActivity extends Activity implements OnClickListener {
	@SuppressWarnings("unused")
	private static final String TAG = "ExerciseActivity";
	private WorkoutProgressView progressView; 
	private Workout workout;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NewRelic.withApplicationToken(
				"AAf0a0158c59a11bc4f28eabd1cec40e370a37fc4f"
				).start(this.getApplication());		
		setContentView(R.layout.exercise_layout);
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
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// TODO get a workout and the progress
		//progressView.setWorkout(workout);
		//progressChanged();
	}

	private void progressChanged() {
		long now = System.currentTimeMillis();
		if (progress.getFinishedDate() == -1) {
			int[] actualExercise = progress.getActualExerciseIndex();
			Exercise exercise = workout.getBlocks().get(actualExercise[0]).getExercises().get(actualExercise[1]);
			if (endOfBreak == -1 || endOfBreak < now) {
				breakLayout.setVisibility(View.GONE);
				exerciseLayout.setVisibility(View.VISIBLE);
				exerciseLabel.setText(exercise.getType().getName());
				String unit = exercise.getType().getName();
				reps.setText(exercise.getReps() + (unit == null?"":" " + unit));
				endLayout.setVisibility(View.GONE);
				progressView.setActiveExcercise(actualExercise[0], actualExercise[1]);
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
				progressView.setActiveBreak(actualExercise[0], actualExercise[1]);
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
				int[] actualExercise = progress.getActualExerciseIndex();
				Exercise exercise = workout.getBlocks().get(actualExercise[0]).getExercises().get(actualExercise[1]);
				progress.exerciseDone(exercise, exercise.getReps(), now - startOfExercise, now);
				if (progress.getFinishedDate() == -1) {
					endOfBreak = now + 1000 * exercise.getBreakSecs();
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
		int[] actualExercise = progress.getActualExerciseIndex();
		Exercise exercise = workout.getBlocks().get(actualExercise[0]).getExercises().get(actualExercise[1]);
		this.exerciseLabel.setText(exercise.getType() + "");
		this.startOfExercise = now;
		this.reps.setText("" + exercise.getReps());
		progressChanged();
	}
}