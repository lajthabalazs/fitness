package hu.droidium.fitness_app.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class ProgramLoader {
	private static final String TAG = "ProgramLoader";
	
	private static final String PROGRAM_FOLDER = "programs";
	private static final String PROGRAMS_KEY = "programs";
	private static final String PROGRAM_ID_KEY = "programId";
	private static final String PROGRAM_NAME_KEY = "programName";
	private static final String PROGRAM_DESCRIPTION_KEY = "programDescription";
	private static final String WORKOUTS_KEY = "workouts";
	private static final String WORKOUT_NAME_KEY = "workoutName";
	private static final String WORKOUT_DAY_KEY = "workoutDay";
	private static final String BLOCKS_KEY = "blokcks";
	private static final String EXERCISES_KEY = "exercises";
	private static final String EXERCISE_TYPE_KEY = "type";
	private static final String REPS_KEY = "reps";
	private static final String BREAK_KEY = "break";
	private static final String WORKOUT_DESCRIPTION_KEY = "workoutDescription";
	private static final String BLOCK_NAME_KEY = "blockName";
	

	public static void loadProgramFromAssets(Context context) throws IOException{
		AssetManager assetManager =  context.getAssets();
		String[] programFiles = assetManager.list(PROGRAM_FOLDER);
		for(String programFile : programFiles){
			loadFromFile(PROGRAM_FOLDER + File.separator + programFile, context);
		}
	}

	private static void loadFromFile(String assetPath, Context context) throws IOException {
        InputStream in = context.getAssets().open(assetPath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
		in.close();
		bufferedReader.close();
		JSONObject root = null;
		try {
			root = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IOException(e.getLocalizedMessage());
		}
		JSONArray programs = null;
		try {
			programs = root.getJSONArray(PROGRAMS_KEY);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IOException(e.getLocalizedMessage());
		}
		for (int i = 0; i < programs.length(); i++) {
			try {
				JSONObject program = programs.getJSONObject(i);
				String programId = program.getString(PROGRAM_ID_KEY);
				String programName = programId;
				try {
					programName = program.getString(PROGRAM_NAME_KEY);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String programDescription = null;
				try {
					programDescription = program.getString(PROGRAM_DESCRIPTION_KEY);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				JSONArray workouts = program.getJSONArray(WORKOUTS_KEY);
				for (int j = 0; j < workouts.length(); j++) {
					JSONObject workout = workouts.getJSONObject(j);
					String workoutName = null;
					try {
						workoutName = workout.getString(WORKOUT_NAME_KEY);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					String workoutDescription = null;
					try {
						workoutDescription = workout.getString(WORKOUT_DESCRIPTION_KEY);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					int day = workout.getInt(WORKOUT_DAY_KEY);
					JSONArray blocks = workout.getJSONArray(BLOCKS_KEY);
					for (int k = 0; k < blocks.length(); k++) {
						JSONObject block = blocks.getJSONObject(k);
						String blockName = null;
						try {
							blockName = block.getString(BLOCK_NAME_KEY);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						JSONArray exercises = block.getJSONArray(EXERCISES_KEY);
						for (int l = 0; l < exercises.length(); l++) {
							try {
								JSONObject exercise = exercises.getJSONObject(l);
								long exerciseType = exercise.getLong(EXERCISE_TYPE_KEY);
								int reps = exercise.getInt(REPS_KEY);
								int breakSecs = 0;
								try {
									breakSecs = exercise.getInt(BREAK_KEY);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							} catch (JSONException e) {
								Log.e(TAG, "Couldn't parse exercise from " + exercises.toString(3));
								e.printStackTrace();
								throw new JSONException(e.getLocalizedMessage());
							}
						}
					}
				}
			} catch (JSONException e) {
				Log.e(TAG, "Couldn't parse program: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}
}