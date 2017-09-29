package workoutguide.pentagramsoftware.pl.workoutguide.eventbus;

import io.reactivex.subjects.PublishSubject;
import workoutguide.pentagramsoftware.pl.workoutguide.events.Event;

/**
 * Created by Admin on 2017-09-21.
 */

public class WorkoutEventBus {
    private static WorkoutEventBus instance;

    private PublishSubject<Event> subject = PublishSubject.create();

    public static WorkoutEventBus getInstance() {
        if (instance == null) {
            instance = new WorkoutEventBus();
        }
        return instance;
    }

    public void publishEvent(Event event) {
        subject.onNext(event);
    }

    public PublishSubject<Event> getSubject() {
        return subject;
    }
}
