package workoutguide.pentagramsoftware.pl.workoutguide.events;

/**
 * Created by Admin on 2017-09-26.
 */

public class TimerEvent extends Event {

    private int id;

    private long time;

    public TimerEvent(int id, long time) {
        this.setId(id);
        this.setTime(time);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static class TimerEventBuilder {

        private int nestedId;

        private long nestedTime;

        public TimerEventBuilder setId(int id) {
            nestedId = id;
            return this;
        }

        public TimerEventBuilder setTime(long time) {
            nestedTime = time;
            return this;
        }

        public TimerEvent build() {
            return new TimerEvent(nestedId, nestedTime);
        }
    }
}
