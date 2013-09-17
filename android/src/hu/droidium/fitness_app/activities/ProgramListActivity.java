package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Program;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProgramListActivity extends Activity {
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_list_layout);
		list = (ListView)findViewById(R.id.stringList);
		ArrayAdapter<Program> exerciseTypeAdapter = new ArrayAdapter<Program>(this, android.R.layout.simple_list_item_1);
		DatabaseManager databaseManager = DatabaseManager.getInstance(this);
		List<Program> programs = databaseManager.getPrograms();
		for (Program program : programs) {
			exerciseTypeAdapter.add(program);
		}
		list.setAdapter(exerciseTypeAdapter);
	}
}
