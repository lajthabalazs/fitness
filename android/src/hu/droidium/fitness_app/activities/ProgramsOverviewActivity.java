package hu.droidium.fitness_app.activities;

import java.util.List;

import com.flurry.android.FlurryAgent;

import hu.droidium.fitness_app.ActiveProgramListAdapter;
import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.ProgramProgress;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class ProgramsOverviewActivity extends FitnessBaseActivity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	private static final String TAG = "ProgramsOverviewActivity";
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
			programAdapter.updatePrograms(programs);
		} else {
			Toast.makeText(this, "No programs added yet.", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constants.RESULT_STARTED_NEW_PROGRAM) {
			long progressId = data.getLongExtra(Constants.PROGRAM_PROGRESS_ID, -1);
			if (progressId != -1) {
				Intent intent = new Intent(this, ProgramProgressDetailsActivity.class);
				intent.putExtra(Constants.PROGRAM_PROGRESS_ID, progressId);
				startActivity(intent);
			}
		} else {
			// Otherwise let the user browse other programs
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, ProgramListActivity.class);
		startActivityForResult(intent, ProgramListActivity.ADD_DETAILS);
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
