package hu.droidium.fitness_app.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class DataLoader {
	private static final String TAG = "ProgramLoader";
	
	private static final String META_FOLDER = "meta";
	private static final String EXERCISES_FOLDER = "exercises";
	private static final String PROGRAM_FOLDER = "programs";

	private static final String PROGRAMS_KEY = "programs";
	private static final String PROGRAM_ID_KEY = "programId";
	private static final String PROGRAM_NAME_KEY = "programName";
	private static final String PROGRAM_DESCRIPTION_KEY = "programDescription";
	
	private static final String WORKOUTS_KEY = "workouts";
	private static final String WORKOUT_NAME_KEY = "workoutName";
	private static final String WORKOUT_DAY_KEY = "workoutDay";
	private static final String WORKOUT_DESCRIPTION_KEY = "workoutDescription";
	
	private static final String BLOCKS_KEY = "blokcks";
	private static final String BLOCK_NAME_KEY = "blockName";

	private static final String EXERCISES_KEY = "exercises";
	private static final String EXERCISE_ID_KEY = "exerciseId";
	private static final String EXERCISE_NAME_KEY = "exerciseName";
	private static final String EXERCISE_TYPE_KEY = "type";
	private static final String EXERCISE_DESCRIPTION_KEY = "exerciseDescription";
	private static final String EXERCISE_INSTRUCTIONS_KEY = "exerciseInstructions";

	private static final String STAMINA_KEY = "stamina";
	private static final String STRENGTH_KEY = "strength";
	private static final String SPEED_KEY = "speed";
	private static final String FLEXIBILITY_KEY = "flexibility";
	private static final String BALANCE_KEY = "balance";
	
	private static final String TARGET_SECS_KEY = "targetSecs";
	private static final String REPS_KEY = "reps";
	private static final String BREAK_KEY = "break";
	
	private static final String MUSCLE_FILE = "muscles.json";
	private static final String MUSCLES_KEY = "muscles";
	private static final String MUSCLE_ID_KEY = "muscleId";
	private static final String MUSCLE_NAME_KEY = "muscleName";
	private static final String MUSCLE_DESCRIPTION_KEY = "muscleDescription";

	private static final String UNIT_KEY = "unit";

	public static void loadDataFromAssets(Context context) throws IOException{
		AssetManager assetManager =  context.getAssets();
		// Load meta data
		loadMuscleDataFromAsset(META_FOLDER + File.separator + MUSCLE_FILE,context);
		DatabaseManager databaseManager = DatabaseManager.getInstance(context);
		List<ORMMuscle> muscles = databaseManager.getMuscles();
		Log.e(TAG, "Count " + muscles.size());
		for (ORMMuscle muscle : muscles) {
			Log.e(TAG, "Muscle " + muscle.getId() + " " + muscle.getName());
		}
		// Load exercises
		/*
		String[] exercisesFiles = assetManager.list(EXERCISES_FOLDER);
		for(String exercisesFile : exercisesFiles){
			loadExerciseMetaDataFromFile(EXERCISES_FOLDER + File.separator + exercisesFile, context);
		}
		// Load programs
		String[] programFiles = assetManager.list(PROGRAM_FOLDER);
		for(String programFile : programFiles){
			loadProgramsFromFile(PROGRAM_FOLDER + File.separator + programFile, context);
		}
		*/
	}
	
	private static void loadMuscleDataFromAsset(String assetPath, Context context) {
		Log.e(TAG, "Loading muscle data from asset " + assetPath);
		
		DatabaseManager databaseManager = DatabaseManager.getInstance(context);
		JSONArray muscles = null;
		try {
			muscles = getRootFromAsset(assetPath, context).getJSONArray(MUSCLES_KEY);
		} catch (JSONException e) {
			Log.e(TAG, "Couldn't parse muscles. " + e.getLocalizedMessage());
			e.printStackTrace();
			return;
		} catch (IOException e) {
			Log.e(TAG, "Couldn't parse muscles. " + e.getLocalizedMessage());
			e.printStackTrace();
			return;
		}
		for (int i = 0; i < muscles.length(); i++) {
			try {
				JSONObject muscle = muscles.getJSONObject(i);
				String muscleId = muscle.getString(MUSCLE_ID_KEY);
				String muscleName = null;
				try {
					muscleName = muscle.getString(MUSCLE_NAME_KEY);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String muscleDescription = null;
				try {
					muscleDescription = muscle.getString(MUSCLE_DESCRIPTION_KEY);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ORMMuscle ormMuscle = new ORMMuscle(muscleId, muscleName, muscleDescription);
				databaseManager.addMuscle(ormMuscle);
				Log.e(TAG, "Added muscle " + ormMuscle.getId());
			} catch (Exception e) {
				Log.e(TAG, "Couldn't parse muscle: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}
	
	private static void loadExerciseMetaDataFromFile(String assetPath, Context context) {
		Log.e(TAG, "Loading exercise meta data from file " + assetPath);
		DatabaseManager databaseManager = DatabaseManager.getInstance(context);
		// Get muscles to link them to exercises
		List<ORMMuscle> muscleList = databaseManager.getMuscles();
		HashMap<String, ORMMuscle> muscles = new HashMap<String, ORMMuscle>();
		for (ORMMuscle muscle : muscleList) {
			muscles.put(muscle.getId(), muscle);
		}
		JSONArray exercises = null;
		try {
			exercises = getRootFromAsset(assetPath, context).getJSONArray(EXERCISES_KEY);
		} catch (JSONException e) {
			Log.e(TAG, "Couldn't parse exercise. " + e.getLocalizedMessage());
			e.printStackTrace();
			return;
		} catch (IOException e) {
			Log.e(TAG, "Couldn't parse exercise. " + e.getLocalizedMessage());
			e.printStackTrace();
			return;
		}
		for (int i = 0; i < exercises.length(); i++) {
			try {
				JSONObject exercise = exercises.getJSONObject(i);
				String exerciseId = exercise.getString(EXERCISE_ID_KEY);
				String exerciseName = null;
				try {
					exerciseName = exercise.getString(EXERCISE_NAME_KEY);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String unit = null;
				try {
					unit = exercise.getString(UNIT_KEY);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String exerciseDescription = null;
				try {
					exerciseDescription = exercise.getString(EXERCISE_DESCRIPTION_KEY);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String exerciseInstructions = null;
				try {
					exerciseInstructions = exercise.getString(EXERCISE_INSTRUCTIONS_KEY);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				int stamina = exercise.getInt(STAMINA_KEY);
				int strength = exercise.getInt(STRENGTH_KEY);
				int speed = exercise.getInt(SPEED_KEY);
				int flexibility = exercise.getInt(FLEXIBILITY_KEY);
				int balance = exercise.getInt(BALANCE_KEY);
				JSONArray musclesArray = exercise.getJSONArray(MUSCLES_KEY);
				List<ORMMuscle> ormMuscles = new ArrayList<ORMMuscle>();
				for (int j = 0; j < musclesArray.length(); j++) {
					ormMuscles.add(muscles.get(musclesArray.getLong(j)));
				}
				ORMExerciseType ormExercise = new ORMExerciseType(exerciseId,
						exerciseName, exerciseDescription, exerciseInstructions, unit,
						stamina, strength, speed, flexibility, balance);
				databaseManager.addExerciseType(ormExercise);
				//ormExercise = databaseManager.getExerciseType(exerciseId);
				ormExercise.updateMuscles(ormMuscles, databaseManager.getHelper().getExerciseTypeMuscleDao());
				databaseManager.addExerciseType(ormExercise);
			} catch (Exception e) {
				Log.e(TAG, "Couldn't parse exercise meta data: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	@SuppressLint("UseSparseArrays")
	private static void loadProgramsFromFile(String assetPath, Context context) throws IOException {
		Log.e(TAG, "Loading programs from file " + assetPath);
		// Get database manager
		DatabaseManager databaseManager = DatabaseManager.getInstance(context);
		List<ORMExerciseType> typeList = databaseManager.getTypes();
		HashMap<String, ORMExerciseType> types = new HashMap<String, ORMExerciseType>();
		for (ORMExerciseType type : typeList) {
			types.put(type.getId(), type);
		}
		JSONArray programs = null;
		try {
			programs = getRootFromAsset(assetPath, context).getJSONArray(PROGRAMS_KEY);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IOException(e.getLocalizedMessage());
		}
		for (int programIndex = 0; programIndex < programs.length(); programIndex++) {
			try {
				JSONObject program = programs.getJSONObject(programIndex);
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
				List<ORMWorkout> ormWorkouts = new ArrayList<ORMWorkout>();
				JSONArray workouts = program.getJSONArray(WORKOUTS_KEY);
				for (int workoutIndex = 0; workoutIndex < workouts.length(); workoutIndex++) {
					String workoutId = programId + " " + workoutIndex;
					JSONObject workout = workouts.getJSONObject(workoutIndex);
					int day = workout.getInt(WORKOUT_DAY_KEY);
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
					List<ORMBlock> ormBlocks = new ArrayList<ORMBlock>();
					JSONArray blocks = workout.getJSONArray(BLOCKS_KEY);
					for (int blockIndex = 0; blockIndex < blocks.length(); blockIndex++) {
						String blockId = workoutId + " " + blockIndex;
						JSONObject block = blocks.getJSONObject(blockIndex);
						String blockName = null;
						try {
							blockName = block.getString(BLOCK_NAME_KEY);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						JSONArray exercises = block.getJSONArray(EXERCISES_KEY);
						List<ORMExercise> ormExercises = new ArrayList<ORMExercise>();
						for (int exerciseIndex = 0; exerciseIndex < exercises.length(); exerciseIndex++) {
							String exerciseId = blockId + " " + exerciseIndex;
							try {
								JSONObject exercise = exercises.getJSONObject(exerciseIndex);
								int exerciseType = exercise.getInt(EXERCISE_TYPE_KEY);
								ORMExerciseType ormExerciseType = types.get(exerciseType);
								if (ormExerciseType == null) {
									throw new JSONException("No type data available.");
								}
								int reps = exercise.getInt(REPS_KEY);
								int targetSecs = 0;
								try {
									targetSecs = exercise.getInt(TARGET_SECS_KEY);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								int breakSecs = 0;
								try {
									breakSecs = exercise.getInt(BREAK_KEY);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								ORMExercise ormExercise = new ORMExercise(exerciseId, exerciseIndex, ormExerciseType, reps, targetSecs, breakSecs);
								databaseManager.addExercise(ormExercise);
								ormExercise = databaseManager.getExercise(exerciseId);
								if (ormExercise != null){
									ormExercises.add(ormExercise);
								} else {
									Log.e(TAG, "ORM exercise couldn't be loaded.");
								}
							} catch (JSONException e) {
								Log.e(TAG, "Couldn't parse exercise from " + exercises.toString(3));
								e.printStackTrace();
								throw new JSONException(e.getLocalizedMessage());
							}
						}
						ORMBlock ormBlock = new ORMBlock(blockId, blockName);
						databaseManager.addBlock(ormBlock);
						ormBlock = databaseManager.getBlock(blockId);
						ormBlock.updateExercises(ormExercises);
						databaseManager.addBlock(ormBlock);
						ormBlocks.add(ormBlock);
					}
					ORMWorkout ormWorkout = new ORMWorkout(workoutId, day, workoutName, workoutDescription);
					databaseManager.addWorkout(ormWorkout);
					ormWorkout = databaseManager.getWorkout(workoutId);
					ormWorkout.updateBlocks(ormBlocks);
					databaseManager.addWorkout(ormWorkout);
					ormWorkouts.add(ormWorkout);
				}
				ORMProgram ormProgram = new ORMProgram(programId, programName, programDescription);
				databaseManager.addProgram(ormProgram);
				ormProgram = databaseManager.getProgram(programId);
				ormProgram.updateWorkouts(ormWorkouts);
				databaseManager.addProgram(ormProgram);
				
				
			} catch (JSONException e) {
				Log.e(TAG, "Couldn't parse program: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	private static JSONObject getRootFromAsset(String assetPath, Context context) throws IOException {
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
		return root;
	}
}