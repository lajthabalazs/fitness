package hu.droidium.fitness_app.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.Log;

public class DataLoader {
	private static final String TAG = "ProgramLoader";
	
	private static final String META_FOLDER = "meta";
	private static final String EXERCISES_FOLDER = "exercises";
	private static final String PROGRAM_FOLDER = "programs";
	private static final String TRANSLATION_FOLDER = "translations";

	private static final String PROGRAMS_KEY = "programs";
	private static final String PROGRAM_ID_KEY = "programId";
	private static final String PROGRAM_NAME_KEY = "programName";
	private static final String PROGRAM_DESCRIPTION_KEY = "programDescription";
	private static final String PROGRAM_COLOR_KEY = "programColor";
	
	private static final String WORKOUTS_KEY = "workouts";
	private static final String WORKOUT_NAME_KEY = "workoutName";
	private static final String WORKOUT_DAY_KEY = "workoutDay";
	private static final String WORKOUT_DESCRIPTION_KEY = "workoutDescription";
	
	private static final String BLOCKS_KEY = "blocks";
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
	private static final String K_UNIT_KEY = "kUnit";
	private static final String UNIT_WEIGHT_KEY = "repWeight";
	private static final String UNIT_TIME_KEY = "repTime";

	private static final String TRANSLATION_ORIGINAL_KEY = "original";
	private static final String TRANSLATION_LANGUAGE_KEY = "language";
	private static final String TRANSLATION_VALUE_KEY = "value";

	public static void loadDataFromAssets(Context context) throws IOException{
		DatabaseManager databaseManager = DatabaseManager.getInstance(context);
		List<ExerciseType> exerciseTypes = databaseManager.getExerciseTypes();
		Log.e(TAG, "Type count " + exerciseTypes.size());
		for (ExerciseType type : exerciseTypes) {
			Log.e(TAG, "Type " + type.getId() + " " + type.getName());
		}
		AssetManager assetManager =  context.getAssets();
		// Load meta data
		
		loadMuscleDataFromAsset(META_FOLDER + File.separator + MUSCLE_FILE,context);
		List<Muscle> muscles = databaseManager.getMuscles();
		Log.e(TAG, "Count " + muscles.size());
		for (Muscle muscle : muscles) {
			Log.e(TAG, "Muscle " + muscle.getId() + " " + muscle.getName());
		}

		// Load exercises
		String[] exercisesFiles = assetManager.list(EXERCISES_FOLDER);
		for(String exercisesFile : exercisesFiles){
			loadExerciseMetaDataFromFile(EXERCISES_FOLDER + File.separator + exercisesFile, context);
		}

		// Load programs
		String[] programFiles = assetManager.list(PROGRAM_FOLDER);
		for(String programFile : programFiles){
			loadProgramsFromFile(PROGRAM_FOLDER + File.separator + programFile, context);
		}

		// Load translations
		String[] translationFiles = assetManager.list(TRANSLATION_FOLDER);
		for(String translationFile : translationFiles){
			loadTranslationsFromFile(TRANSLATION_FOLDER + File.separator + translationFile, context);
		}
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
					Log.w(TAG, "No muscle name " + e.getMessage());
				}
				String muscleDescription = null;
				try {
					muscleDescription = muscle.getString(MUSCLE_DESCRIPTION_KEY);
				} catch (JSONException e) {
					Log.w(TAG, "No muscle description " + e.getMessage());
				}
				Muscle ormMuscle = new Muscle(muscleId, muscleName, muscleDescription);
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
		List<Muscle> muscleList = databaseManager.getMuscles();
		HashMap<String, Muscle> muscles = new HashMap<String, Muscle>();
		for (Muscle muscle : muscleList) {
			muscles.put(muscle.getId(), muscle);
		}
		JSONArray exercises = null;
		try {
			exercises = getRootFromAsset(assetPath, context).getJSONArray(EXERCISES_KEY);
		} catch (JSONException e) {
			Log.e(TAG, "Couldn't parse exercises. " + e.getLocalizedMessage());
			e.printStackTrace();
			return;
		} catch (IOException e) {
			Log.e(TAG, "Couldn't parse exercises. " + e.getLocalizedMessage());
			e.printStackTrace();
			return;
		}
		for (int i = 0; i < exercises.length(); i++) {
			try {
				JSONObject exercise = exercises.getJSONObject(i);
				String exerciseId = exercise.getString(EXERCISE_ID_KEY);
				String exerciseName = null;
				float unitWeight = 0f;
				float unitTime = 0f;
				try {
					exerciseName = exercise.getString(EXERCISE_NAME_KEY);
				} catch (JSONException e) {
					Log.w(TAG, "No exercise name " + e.getMessage());
				}
				String unit = null;
				try {
					unit = exercise.getString(UNIT_KEY);
				} catch (JSONException e) {
					Log.w(TAG, "No exercise unit " + e.getMessage());
				}
				String kUnit = null;
				try {
					kUnit = exercise.getString(K_UNIT_KEY);
				} catch (JSONException e) {
					Log.w(TAG, "No exercise kUnit " + e.getMessage());
				}
				String exerciseDescription = null;
				try {
					exerciseDescription = exercise.getString(EXERCISE_DESCRIPTION_KEY);
				} catch (JSONException e) {
					Log.w(TAG, "No exercise description " + e.getMessage());
				}
				String exerciseInstructions = null;
				try {
					exerciseInstructions = exercise.getString(EXERCISE_INSTRUCTIONS_KEY);
				} catch (JSONException e) {
					Log.w(TAG, "No exercise instructions " + e.getMessage());
				}
				try {
					unitWeight = (float)exercise.getDouble(UNIT_WEIGHT_KEY);
				} catch (JSONException e) {
					unitWeight = 1;
					Log.w(TAG, "No exercise unit " + e.getMessage());
				}
				try {
					unitTime = (float)exercise.getDouble(UNIT_TIME_KEY);
				} catch (JSONException e) {
					unitTime = 1;
					Log.w(TAG, "No exercise time " + e.getMessage());
				}
				int stamina = exercise.getInt(STAMINA_KEY);
				int strength = exercise.getInt(STRENGTH_KEY);
				int speed = exercise.getInt(SPEED_KEY);
				int flexibility = exercise.getInt(FLEXIBILITY_KEY);
				int balance = exercise.getInt(BALANCE_KEY);
				ExerciseType ormExercise = new ExerciseType(exerciseId,
						exerciseName, exerciseDescription, exerciseInstructions, unit, kUnit, unitWeight, unitTime,
						stamina, strength, speed, flexibility, balance);
				databaseManager.addExerciseType(ormExercise);
				JSONArray musclesArray = exercise.getJSONArray(MUSCLES_KEY);
				for (int j = 0; j < musclesArray.length(); j++) {
					databaseManager.associateMuscleWithExercise(ormExercise, muscles.get(musclesArray.getString(j)));
				}
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
		List<ExerciseType> typeList = databaseManager.getTypes();
		HashMap<String, ExerciseType> types = new HashMap<String, ExerciseType>();
		for (ExerciseType type : typeList) {
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
					Log.w(TAG, "No program name " + e.getMessage());
				}
				String programDescription = null;
				try {
					programDescription = program.getString(PROGRAM_DESCRIPTION_KEY);
				} catch (JSONException e) {
					Log.w(TAG, "No program description " + e.getMessage());
				}
				int programColor = 0;
				try {
					String colorString = program.getString(PROGRAM_COLOR_KEY);
					// Brighten color if needed
					int rgb = Color.parseColor(colorString);
					int red = (rgb >> 16) & 0xFF;
					int green = (rgb >> 8) & 0xFF;
					int blue = rgb & 0xFF;
					if (red < 128) {
						red += 128;
					}
					if (blue < 128){
						blue += 128;
					}
					if (green < 128) {
						blue += 128;
					}
					programColor = Color.rgb(red, green, blue);
				} catch (JSONException e) {
					Log.w(TAG, "No program color " + e.getMessage());
					int red = (int)(Math.random() * 128 + 128);
					int green = (int)(Math.random() * 128 + 128);
					int blue = (int)(Math.random() * 128 + 128);
					programColor = Color.rgb(red, green, blue);				}
				Program ormProgram = new Program(programId, programName, programDescription, programColor);
				databaseManager.addProgram(ormProgram);
				JSONArray workouts = program.getJSONArray(WORKOUTS_KEY);
				for (int workoutIndex = 0; workoutIndex < workouts.length(); workoutIndex++) {
					String workoutId = programId + " " + workoutIndex;
					JSONObject workout = workouts.getJSONObject(workoutIndex);
					int day = workout.getInt(WORKOUT_DAY_KEY);
					String workoutName = null;
					try {
						workoutName = workout.getString(WORKOUT_NAME_KEY);
					} catch (JSONException e) {
						Log.w(TAG, "No workout name " + e.getMessage());
					}
					String workoutDescription = null;
					try {
						workoutDescription = workout.getString(WORKOUT_DESCRIPTION_KEY);
					} catch (JSONException e) {
						Log.w(TAG, "No workout description " + e.getMessage());
					}
					Workout ormWorkout = new Workout(workoutId, ormProgram, day, workoutName, workoutDescription);
					databaseManager.addWorkout(ormWorkout);
					JSONArray blocks = workout.getJSONArray(BLOCKS_KEY);
					for (int blockIndex = 0; blockIndex < blocks.length(); blockIndex++) {
						String blockId = workoutId + " " + blockIndex;
						JSONObject block = blocks.getJSONObject(blockIndex);
						String blockName = null;
						try {
							blockName = block.getString(BLOCK_NAME_KEY);
						} catch (JSONException e) {
							Log.w(TAG, "No block name " + e.getMessage());
						}
						Block ormBlock = new Block(blockId, ormWorkout, blockName, blockIndex);
						databaseManager.addBlock(ormBlock);
						JSONArray exercises = block.getJSONArray(EXERCISES_KEY);
						for (int exerciseIndex = 0; exerciseIndex < exercises.length(); exerciseIndex++) {
							String exerciseId = blockId + " " + exerciseIndex;
							try {
								JSONObject exercise = exercises.getJSONObject(exerciseIndex);
								String exerciseType = exercise.getString(EXERCISE_TYPE_KEY);
								ExerciseType ormExerciseType = types.get(exerciseType);
								if (ormExerciseType == null) {
									throw new JSONException("No type data available.");
								}
								int reps = exercise.getInt(REPS_KEY);
								int targetSecs = 0;
								try {
									targetSecs = exercise.getInt(TARGET_SECS_KEY);
								} catch (JSONException e) {
									Log.w(TAG, "No target secs " + e.getMessage());
								}
								int breakSecs = 0;
								try {
									breakSecs = exercise.getInt(BREAK_KEY);
								} catch (JSONException e) {
									Log.w(TAG, "No break secs " + e.getMessage());
								}
								Log.e(TAG, "Exercise parsed successfully. " + exerciseId);
								Exercise ormExercise = new Exercise(exerciseId, ormBlock, exerciseIndex, ormExerciseType, reps, targetSecs, breakSecs);
								databaseManager.addExercise(ormExercise);
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

	private static void loadTranslationsFromFile(String assetPath, Context context) {
		Log.e(TAG, "Loading translaitons from asset " + assetPath);
		
		DatabaseManager databaseManager = DatabaseManager.getInstance(context);
		JSONArray translations = null;
		try {
			translations = getRootArrayFromAsset(assetPath, context);
		} catch (IOException e) {
			Log.e(TAG, "Couldn't parse translations. " + e.getLocalizedMessage());
			e.printStackTrace();
			return;
		}
		for (int i = 0; i < translations.length(); i++) {
			try {
				JSONObject translation = translations.getJSONObject(i);
				String original = translation.getString(TRANSLATION_ORIGINAL_KEY);
				String language = translation.getString(TRANSLATION_LANGUAGE_KEY);
				String value = translation.getString(TRANSLATION_VALUE_KEY);
				Translation ormTranslation = new Translation(original, language, value);
				databaseManager.addTranslation(ormTranslation);
				Log.e(TAG, "Added translation " + ormTranslation);
			} catch (Exception e) {
				Log.e(TAG, "Couldn't parse muscle: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	} 
	private static StringBuilder loadStringFromAsset(String assetPath, Context context) throws IOException {
        InputStream in = context.getAssets().open(assetPath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
		in.close();
		bufferedReader.close();
		return stringBuilder;
	}

	private static JSONObject getRootFromAsset(String assetPath, Context context) throws IOException {
		StringBuilder stringBuilder = loadStringFromAsset(assetPath, context);
		JSONObject root = null;
		try {
			root = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IOException(e.getLocalizedMessage());
		}
		return root;
	}

	private static JSONArray getRootArrayFromAsset(String assetPath, Context context) throws IOException {
		StringBuilder stringBuilder = loadStringFromAsset(assetPath, context);
		JSONArray root = null;
		try {
			root = new JSONArray(stringBuilder.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			throw new IOException(e.getLocalizedMessage());
		}
		return root;
	}
}