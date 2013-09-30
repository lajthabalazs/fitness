package hu.droidium.fitness_app.activities;

import java.util.List;

import hu.droidium.fitness_app.ActiveProgramListAdapter;
import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.ProgramProgress;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ProgramsOverviewActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	private Button startNewProgram;
	private ListView programList;
	
	private ActiveProgramListAdapter programAdapter;
	private DatabaseManager databaseManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.programs_overview_activity);
		databaseManager = DatabaseManager.getInstance(this);
		programList = (ListView)findViewById(R.id.programList);
		programList.setOnItemClickListener(this);
		programList.setOnItemLongClickListener(this);
		programAdapter = new ActiveProgramListAdapter(this, databaseManager);
		programList.setAdapter(programAdapter);
		startNewProgram = (Button)findViewById(R.id.startNewProgram);
		startNewProgram.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		List<ProgramProgress> programs = databaseManager.getProgressList();
		if (programs != null && programs.size() > 0) {
			for (ProgramProgress progress : programs) {
				progress.setProgram(databaseManager.getProgram(progress.getProgram().getId()));
			}
			programAdapter.updatePrograms(programs);
		} else {
			Toast.makeText(this, "No programs added yet.", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, ProgramListActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// Program selected
		Intent intent = new Intent(this, ProgramProgressDetailsActivity.class);
		ProgramProgress progress = (ProgramProgress)programAdapter.getItem(arg2);
		intent.putExtra(Constants.PROGRAM_PROGRESS_ID, progress.getProgressId());
		startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		return false;
	}
}
