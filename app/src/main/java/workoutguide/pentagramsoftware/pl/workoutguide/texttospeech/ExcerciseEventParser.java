package workoutguide.pentagramsoftware.pl.workoutguide.texttospeech;

import android.content.Context;

import timber.log.Timber;
import workoutguide.pentagramsoftware.pl.workoutguide.R;
import workoutguide.pentagramsoftware.pl.workoutguide.enums.ExcerciseTypesEnum;
import workoutguide.pentagramsoftware.pl.workoutguide.enums.IntensitiesEnum;
import workoutguide.pentagramsoftware.pl.workoutguide.events.ExcerciseEvent;
import workoutguide.pentagramsoftware.pl.workoutguide.utils.StringUtils;

/**
 * Created by Admin on 2017-09-22.
 * <p>
 * Class represents an component that transforms ExcerciseEvent objects into a human readable form.
 * Therefore the {@link TextToSpeechManager} object is able to read it to the user.
 */

public class ExcerciseEventParser {

    private static ExcerciseEventParser instance;

    public static ExcerciseEventParser getInstance() {
        if (instance == null) {
            instance = new ExcerciseEventParser();
        }
        return instance;
    }

    public String ParseEvent(Context context, ExcerciseEvent event) {
        if (event.getType() == ExcerciseTypesEnum.PREPARATION) {
            return extractType(context, event.getType()) + " "
                    + context.getString(R.string.parser_prefix_workout_begins) + " "
                    + event.getDuration() + " "
                    + context.getString(R.string.duration_title_seconds);
        } else {
            return context.getString(R.string.parser_prefix_prepare) + " "
                    + event.getDuration() + " "
                    + context.getString(R.string.duration_title_seconds) + " "
                    + context.getString(R.string.parser_bindings_of) + " "
                    + extractIntensity(context, event.getIntensity()) + " "
                    + context.getString(R.string.parser_bindings_intensity) + " "
                    + extractType(context, event.getType());
        }

    }

    private String extractType(Context context, ExcerciseTypesEnum type) {
        switch (type) {
            case COOLDOWN:
                return context.getResources().getString(R.string.enum_excercise_types_cooldown);
            case PREPARATION:
                return context.getResources().getString(R.string.enum_excercise_types_preparation);
            case STRENGTH:
                return context.getResources().getString(R.string.enum_excercise_types_strength);
            case STEADY_CARDIO:
                return context.getResources().getString(R.string.enum_excercise_types_steady_cardio);
            case HIIT_CARDIO:
                return context.getResources().getString(R.string.enum_excercise_types_interval_cardio);
            case REST:
                return context.getResources().getString(R.string.enum_excercise_types_rest);
            default:
                Timber.e("Attempting to extract unknown type: %s", type);
                return StringUtils.EMPTY_STRING;
        }
    }

    private String extractIntensity(Context context, IntensitiesEnum intensity) {
        switch (intensity) {
            case LOW:
                return context.getResources().getString(R.string.enum_intensities_low);
            case MEDIUM:
                return context.getResources().getString(R.string.enum_intensities_medium);
            case HIGH:
                return context.getResources().getString(R.string.enum_intensities_high);
            default:
                Timber.e("Attempting to extract unknown intensity: %s", intensity);
                return StringUtils.EMPTY_STRING;
        }
    }

    /**
     * Prepares duration to be read by {@link TextToSpeechManager}
     *
     * @param context
     * @param duration
     * @return
     */
    private String extractDuration(Context context, int duration) {
        int minutes = duration / 60;
        if (minutes > 0) {
            int hours = minutes / 60;
        }
        return "";
    }
}
