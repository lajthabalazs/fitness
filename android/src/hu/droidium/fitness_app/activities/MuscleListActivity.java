package hu.droidium.fitness_app.activities;

import java.util.List;

import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.Muscle;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MuscleListActivity extends FitnessBaseActivity {
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_list_layout);
		list = (ListView)findViewById(R.id.stringList);
		ArrayAdapter<Muscle> muscleAdapter = new ArrayAdapter<Muscle>(this, android.R.layout.simple_list_item_1);
		DatabaseManager databaseManager = DatabaseManager.getInstance(this);
		List<Muscle> muscles = databaseManager.getMuscles();
		for (Muscle muscle : muscles) {
			muscleAdapter.add(muscle);
		}
		list.setAdapter(muscleAdapter);
	}
}
