package hu.droidium.fitness_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ExerciseActivity extends Activity implements OnClickListener {
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
	private TextView exerciseLabel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		doneButton = (Button)findViewById(R.id.exerciseDone);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Load actual workout
		workout = new DummyWorkout();
		progressView.setWorkout(workout);
		progressView.setActiveExcercise(0, 0);
		// Load progress
		
	}

	@Override
	public void onClick(View v) {
	}
}