package hu.droidium.fitness_app.activities;

import java.util.ArrayList;
import java.util.List;

import hu.droidium.fitness_app.ActiveProgramListAdapter;
import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.FlurryLogConstants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.ProgramProgress;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class ProgramsOverviewActivity extends FitnessBaseActivity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	private Button startNewProgram;
	private ListView programList;
	
	private ActiveProgramListAdapter programAdapter;
	private DatabaseManager databaseManager;
	private View noActiveProgram;
	private SharedPreferences prefs;
	private ImageView settingsImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.programs_overview_activity);
		prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
		databaseManager = DatabaseManager.getInstance(this);
		programList = (ListView)findViewById(R.id.programList);
		programList.setOnItemClickListener(this);
		programList.setOnItemLongClickListener(this);
		programAdapter = new ActiveProgramListAdapter(this, databaseManager);
		noActiveProgram = findViewById(R.id.noActiveProgram);
		noActiveProgram.setOnClickListener(this);
		programList.setAdapter(programAdapter);
		startNewProgram = (Button)findViewById(R.id.startNewProgram);
		startNewProgram.setOnClickListener(this);
		settingsImage = (ImageView)findViewById(R.id.settingsIcon);
		settingsImage.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refreshUI();
	}
	
	private void refreshUI() {
		List<ProgramProgress> programs = databaseManager.getProgressList(prefs.getBoolean(Constants.SETTINGS_SHOW_DONE_PROGRAMS, true));
		if (programs != null && programs.size() > 0) {
			programAdapter.updatePrograms(programs);
			noActiveProgram.setVisibility(View.GONE);
		} else {
			programAdapter.updatePrograms(new ArrayList<ProgramProgress>());
			noActiveProgram.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constants.RESULT_STARTED_NEW_PROGRAM) {
			log(FlurryLogConstants.ADDED_PROGRAM);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_settings) {
			// TODO go to settings
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.settingsIcon: {
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				break;
			}
			case R.id.startNewProgram : {
				Intent intent = new Intent(this, ProgramListActivity.class);
				startActivityForResult(intent, ProgramListActivity.ADD_DETAILS);
				break;
			}
		}
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
		final ProgramProgress progress = (ProgramProgress)programAdapter.getItem(arg2);
		Builder builder = new Builder(this);
		builder.setTitle(R.string.deleteProgramProgressTitle);
		builder.setMessage(R.string.deleteProgramProgressMessage);
		builder.setPositiveButton(R.string.deleteProgramProgressDelete, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				databaseManager.removeProgramProgress(progress);
				refreshUI();
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
		return true;
	}
}
