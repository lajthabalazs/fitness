package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.AvailableProgramListAdapter;
import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Program;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ProgramListActivity extends Activity implements OnClickListener, OnItemClickListener {
	@SuppressWarnings("unused")
	private static final String TAG = "ProgramListActivity";
	private static final int ADD_DETAILS = 12317;
	private ListView list;
	private Button cancelButton;
	private AvailableProgramListAdapter programAdapter;
	private DatabaseManager databaseManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_programs_layout);
		setTitle(R.string.programListTitle);
		list = (ListView)findViewById(R.id.stringList);
		databaseManager = DatabaseManager.getInstance(this);
		programAdapter = new AvailableProgramListAdapter(getResources(), getLayoutInflater(), databaseManager);
		list.setAdapter(programAdapter);
		list.setOnItemClickListener(this);
		cancelButton = (Button)findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		programAdapter.updatePrograms(databaseManager.getPrograms());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.cancelButton) {
			// Cancels the new program
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constants.RESULT_STARTED_NEW_PROGRAM) {
			// If a program was selected, return immediately
			finish();
		} else {
			// Otherwise let the user browse other programs
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String programId = ((Program)programAdapter.getItem(arg2)).getId();
		Intent intent = new Intent(this,ProgramDetailsActivity.class);
		intent.putExtra(Constants.PROGRAM_ID_KEY, programId);
		startActivityForResult(intent, ADD_DETAILS);
	}
}
