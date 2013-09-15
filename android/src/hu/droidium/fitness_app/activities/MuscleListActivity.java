package hu.droidium.fitness_app.activities;

import java.util.List;

import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.ORMMuscle;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MuscleListActivity extends Activity {
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_list_layout);
		list = (ListView)findViewById(R.id.stringList);
		ArrayAdapter<ORMMuscle> muscleAdapter = new ArrayAdapter<ORMMuscle>(this, android.R.layout.simple_list_item_1);
		DatabaseManager databaseManager = DatabaseManager.getInstance(this);
		List<ORMMuscle> muscles = databaseManager.getMuscles();
		for (ORMMuscle muscle : muscles) {
			muscleAdapter.add(muscle);
		}
		list.setAdapter(muscleAdapter);
	}
}
