package workoutguide.pentagramsoftware.pl.workoutguide.dashboards;

import io.reactivex.disposables.Disposable;

/**
 * Created by Admin on 2017-09-26.
 */

public class WorkoutDashboardPresenter implements Disposable {

    WorkoutDashboardActivity activity;

    WorkoutDashboardPresenter(WorkoutDashboardActivity view) {
        this.activity = activity;
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isDisposed() {
        return false;
    }
}
