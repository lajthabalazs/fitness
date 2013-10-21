package hu.droidium.fitness_app.activities;

import hu.droidium.fitness_app.R;
import hu.droidium.fitness_app.database.DataLoader;

import java.io.IOException;

import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TestActivity extends Activity implements OnClickListener {

	private Button showExercises;
	private Button showMuscles;
	private Button showPrograms;
	private Button toRealApp;
	private Button createDb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		showMuscles = (Button)findViewById(R.id.showMusclesButton);
		showMuscles.setOnClickListener(this);
		showExercises = (Button)findViewById(R.id.showExercisesButton);
		showExercises.setOnClickListener(this);
		showPrograms = (Button)findViewById(R.id.showProgramsButton);
		showPrograms.setOnClickListener(this);
		toRealApp = (Button)findViewById(R.id.toRealApp);
		toRealApp.setOnClickListener(this);
		createDb = (Button)findViewById(R.id.createDatabase);
		createDb.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
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

	protected void loadFromAssets() {
		try {
			DataLoader.loadDataFromAssets(this);
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.showMusclesButton: {
			Intent intent = new Intent(this,MuscleListActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.showExercisesButton: {
			Intent intent = new Intent(this,ExerciseListActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.showProgramsButton: {
			Intent intent = new Intent(this,ProgramListActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.toRealApp: {
			Intent intent = new Intent(this,ProgramsOverviewActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.createDatabase: {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					loadFromAssets();
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(TestActivity.this, "Database created", Toast.LENGTH_LONG).show();
						}
					});
				}
			}).start();
			break;
		}
		}
	}

}
