package hu.droidium.fitness_app;

import hu.droidium.fitness_app.database.ORMProgramProgressManager;
import hu.droidium.fitness_app.model.ProgramProgress;
import hu.droidium.fitness_app.model.ProgramProgressManager;
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
	private ProgramProgressManager programProgressManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.programs_overview);
		programList = (ListView)findViewById(R.id.programList);
		programList.setOnItemClickListener(this);
		programList.setOnItemLongClickListener(this);
		programProgressManager = new ORMProgramProgressManager();
		programAdapter = new ActiveProgramListAdapter(this, programProgressManager);
		programList.setAdapter(programAdapter);
		startNewProgram = (Button)findViewById(R.id.startNewProgram);
		startNewProgram.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO add a new workout
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
