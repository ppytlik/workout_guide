package workoutguide.pentagramsoftware.pl.workoutguide.events;

import workoutguide.pentagramsoftware.pl.workoutguide.enums.ExcerciseTypesEnum;
import workoutguide.pentagramsoftware.pl.workoutguide.enums.IntensitiesEnum;

/**
 * Created by Admin on 2017-09-22.
 */

public class ExcerciseEvent extends Event {

    private int id;

    private ExcerciseTypesEnum type;

    private int duration;

    private IntensitiesEnum intensity;

    public ExcerciseEvent() {

    }

    public ExcerciseEvent(int id, ExcerciseTypesEnum type, int duration, IntensitiesEnum intensity) {
        this.id = id;
        this.type = type;
        this.duration = duration;
        this.intensity = intensity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ExcerciseTypesEnum getType() {
        return type;
    }

    public void setType(ExcerciseTypesEnum type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public IntensitiesEnum getIntensity() {
        return intensity;
    }

    public void setIntensity(IntensitiesEnum intensity) {
        this.intensity = intensity;
    }

    public static class Builder {

        private int nestedId;

        private int nestedDuration;

        private ExcerciseTypesEnum nestedType;

        private IntensitiesEnum nestedIntensity;

        public Builder setId(int id) {
            nestedId = id;
            return this;
        }

        public Builder duration(int duration) {
            nestedDuration = duration;
            return this;
        }

        public Builder type(ExcerciseTypesEnum type) {
            nestedType = type;
            return this;
        }

        public Builder intensity(IntensitiesEnum intensity) {
            nestedIntensity = intensity;
            return this;
        }

        public ExcerciseEvent build() {
            return new ExcerciseEvent(nestedId, nestedType, nestedDuration, nestedIntensity);
        }
    }
}
