package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.AvailableWorkoutListAdapter;
import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Program;
import hu.droidium.fitness_app.database.ProgramProgress;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ProgramDetailsActivity extends FitnessBaseActivity implements OnClickListener {
	private ListView list;
	private Button cancelButton;
	private Button addNewProgram;
	private AvailableWorkoutListAdapter workoutAdapter;
	private String programId;
	private DatabaseManager databaseManager;
	
	private TextView programName;
	private TextView programDescription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.program_details_layout);
		list = (ListView)findViewById(R.id.workoutList);
		programId = getIntent().getStringExtra(Constants.PROGRAM_ID_KEY);
		databaseManager = DatabaseManager.getInstance(this);
		workoutAdapter = new AvailableWorkoutListAdapter(this,getLayoutInflater(),databaseManager);
		list.setAdapter(workoutAdapter);
		list.setEnabled(false);
		cancelButton = (Button)findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(this);
		addNewProgram = (Button)findViewById(R.id.startNewProgram);
		addNewProgram.setOnClickListener(this);
		
		programName = (TextView)findViewById(R.id.programNameLabel);
		programDescription = (TextView)findViewById(R.id.programDescription);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Program program = databaseManager.getProgram(programId);
		workoutAdapter.updateWorkouts(program.getWorkouts());
		programName.setText(program.getName());
		if (program.getDescription() != null && program.getDescription().length() > 0) {
			programDescription.setText(program.getDescription());
			programDescription.setVisibility(View.VISIBLE);
		} else {
			programDescription.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelButton: {
			setResult(Constants.RESULT_CANCEL);
			finish();
			break;
		}
		case R.id.startNewProgram: {
			Program program = databaseManager.getProgram(programId);
			long now = System.currentTimeMillis();
			ProgramProgress progress = new  ProgramProgress(now, program);
			databaseManager.startProgram(progress);
			Intent output = new Intent();
			output.putExtra(Constants.PROGRAM_PROGRESS_ID, progress.getProgressId());
			setResult(Constants.RESULT_STARTED_NEW_PROGRAM, output);
			finish();
			break;
		}
		}
	}
}