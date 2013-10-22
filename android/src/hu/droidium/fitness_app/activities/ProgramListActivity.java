package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.AvailableProgramListAdapter;
import hu.droidium.fitness_app.Constants;
import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Program;
import hu.droidium.fitness_app.model.comparators.ProgramComparator;
import hu.droidium.fitness_app.model.comparators.ProgramLengthComparator;
import hu.droidium.fitness_app.model.comparators.ProgramLengthDescComparator;
import hu.droidium.fitness_app.model.comparators.ProgramNameComparator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class ProgramListActivity extends FitnessBaseActivity implements OnClickListener, OnItemClickListener, OnItemSelectedListener {
	private static final String TAG = "ProgramListActivity";
	static final int ADD_DETAILS = 12317;
	private ListView list;
	private Button cancelButton;
	private AvailableProgramListAdapter programAdapter;
	private DatabaseManager databaseManager;
	private Spinner programListSorter;
	private ArrayAdapter<ProgramComparator> programListSorterAdapter;
	private ProgramComparator selectedComparator = new ProgramNameComparator("UNKNOWN");
	private ProgramComparator[] comparators;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.program_list_layout);
		setTitle(R.string.programListTitle);
		list = (ListView)findViewById(R.id.stringList);
		databaseManager = DatabaseManager.getInstance(this);
		programAdapter = new AvailableProgramListAdapter(getResources(), getLayoutInflater(), databaseManager);
		list.setAdapter(programAdapter);
		list.setOnItemClickListener(this);
		cancelButton = (Button)findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(this);
		
		programListSorter = (Spinner) findViewById(R.id.programListSorter);
		comparators = new ProgramComparator[3];
		comparators[0] = new ProgramNameComparator(getString(R.string.sortByName));
		comparators[1] = new ProgramLengthComparator(getString(R.string.sortByLength), databaseManager);
		comparators[2] = new ProgramLengthDescComparator(getString(R.string.sortByLengthDesc), databaseManager);
		// Create an ArrayAdapter using the string array and a default spinner layout
		programListSorterAdapter = new ArrayAdapter<ProgramComparator>(this, R.layout.sorter_chooser, R.id.sorterLabel, comparators );
		// Specify the layout to use when the list of choices appears
		programListSorterAdapter.setDropDownViewResource(R.layout.sorter_chooser_dropdown);
		// Apply the adapter to the spinner
		programListSorter.setAdapter(programListSorterAdapter);
		programListSorter.setOnItemSelectedListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refreshUI();
	}
	
	private void refreshUI() {
		programAdapter.updatePrograms(databaseManager.getPrograms(), selectedComparator);		
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
			Intent output = new Intent();			
			long progressId = data.getLongExtra(Constants.PROGRAM_PROGRESS_ID, -1);
			Log.e(TAG, "Progress id " + progressId);
			output.putExtra(Constants.PROGRAM_PROGRESS_ID, progressId);
			setResult(Constants.RESULT_STARTED_NEW_PROGRAM, output);
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

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		selectedComparator = comparators[arg2];
		refreshUI();
	}

	@Override public void onNothingSelected(AdapterView<?> arg0) {}
}