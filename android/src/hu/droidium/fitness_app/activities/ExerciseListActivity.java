package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DatabaseManager;
import hu.droidium.fitness_app.database.ExerciseType;

import java.util.List;

import com.flurry.android.FlurryAgent;

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
		ArrayAdapter<ExerciseType> exerciseTypeAdapter = new ArrayAdapter<ExerciseType>(this, android.R.layout.simple_list_item_1);
		DatabaseManager databaseManager = DatabaseManager.getInstance(this);
		List<ExerciseType> types = databaseManager.getExerciseTypes();
		for (ExerciseType type : types) {
			exerciseTypeAdapter.add(type);
		}
		list.setAdapter(exerciseTypeAdapter);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, getString(R.string.flurryKey));
	}
	 
	@Override
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}

}
