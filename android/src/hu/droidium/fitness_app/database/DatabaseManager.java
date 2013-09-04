package hu.droidium.fitness_app.database;

import android.content.Context;

public class DatabaseManager {
	private static DatabaseManager instance;
	
	private DatabaseHelper helper;
	
	private DatabaseManager(Context context) {
		helper = new DatabaseHelper(context);
	}
		
	public static DatabaseManager getInstance(Context context){
		if (instance == null) {
			instance = new DatabaseManager(context);
		}
		return instance;
	}
}
