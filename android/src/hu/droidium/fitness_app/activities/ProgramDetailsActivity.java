package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Program;
import hu.droidium.fitness_app.database.ProgramProgress;
import hu.droidium.fitness_app.database.Workout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ProgramDetailsActivity extends Activity implements OnClickListener {
	private ListView list;
	private Button cancelButton;
	private Button addNewProgram;
	private ArrayAdapter<Workout> workoutAdapter;
	private String programId;
	private EditText progressNameEdit;
	private DatabaseManager databaseManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_program_layout);
		list = (ListView)findViewById(R.id.workoutList);
		programId = getIntent().getStringExtra(Constants.PROGRAM_ID_KEY);
		progressNameEdit = (EditText)findViewById(R.id.newProgramNameEdit);
		workoutAdapter = new ArrayAdapter<Workout>(this, android.R.layout.simple_list_item_1);
		databaseManager = DatabaseManager.getInstance(this);
		list.setAdapter(workoutAdapter);
		cancelButton = (Button)findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(this);
		addNewProgram = (Button)findViewById(R.id.startNewProgram);
		addNewProgram.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		workoutAdapter.clear();
		Program program = databaseManager.getProgram(programId);
		setTitle(program.getName());
		for (Workout workout : program.getWorkouts()) {
			workoutAdapter.add(workout);
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
			String progressNameName = progressNameEdit.getText().toString();
			ProgramProgress progress = new  ProgramProgress(now, progressNameName, program);
			databaseManager.startProgram(progress);
			setResult(Constants.RESULT_STARTED_NEW_PROGRAM);
			finish();
			break;
		}
		}
	}
}