package workoutguide.pentagramsoftware.pl.workoutguide.enums;

/**
 * Created by Admin on 2017-09-22.
 */

public enum IntensitiesEnum {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH");

    IntensitiesEnum(String asda) {
    }

    public static String[] toArray() {
        return new String[]{LOW.toString(), MEDIUM.toString(), HIGH.toString()};
    }
}
