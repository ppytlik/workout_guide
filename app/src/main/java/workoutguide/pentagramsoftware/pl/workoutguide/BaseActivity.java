package workoutguide.pentagramsoftware.pl.workoutguide;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Admin on 2016-10-15.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTimber();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initButterknife();
    }

    public void initTimber() {
        Timber.plant(new Timber.DebugTree());
    }

    public void initButterknife() {
        ButterKnife.bind(this);
    }

}
