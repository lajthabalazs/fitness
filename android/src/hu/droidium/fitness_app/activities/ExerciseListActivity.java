package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.ORMExerciseType;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExerciseListActivity extends Activity {
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_list_layout);
		list = (ListView)findViewById(R.id.stringList);
		ArrayAdapter<ORMExerciseType> exerciseTypeAdapter = new ArrayAdapter<ORMExerciseType>(this, android.R.layout.simple_list_item_1);
		DatabaseManager databaseManager = DatabaseManager.getInstance(this);
		List<ORMExerciseType> types = databaseManager.getExerciseTypes();
		for (ORMExerciseType type : types) {
			exerciseTypeAdapter.add(type);
		}
		list.setAdapter(exerciseTypeAdapter);
	}
}
