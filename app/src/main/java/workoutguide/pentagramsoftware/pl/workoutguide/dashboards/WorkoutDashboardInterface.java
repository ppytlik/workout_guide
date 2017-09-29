package workoutguide.pentagramsoftware.pl.workoutguide.dashboards;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Admin on 2017-09-26.
 */

public interface WorkoutDashboardInterface {

    Context aquireContext();

    Activity aquireActivity();

    FragmentManager aquireFragmentManager();

    Resources aquireResources();
}
