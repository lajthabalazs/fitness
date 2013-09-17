package hu.droidium.fitness_app.activities;

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

public class ProgramsOverviewActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	private Button startNewProgram;
	private ListView programList;
	private ActiveProgramListAdapter programAdapter;
	private DatabaseManager databaseManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.programs_overview);
		programList = (ListView)findViewById(R.id.programList);
		programList.setOnItemClickListener(this);
		programList.setOnItemLongClickListener(this);
		databaseManager = DatabaseManager.getInstance(this);
		programAdapter = new ActiveProgramListAdapter(this);
		programList.setAdapter(programAdapter);
		startNewProgram = (Button)findViewById(R.id.startNewProgram);
		startNewProgram.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		programAdapter.updatePrograms(databaseManager.getProgressList());
	}

	@Override
	public void onClick(View v) {
		// TODO start a new program
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
