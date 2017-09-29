package workoutguide.pentagramsoftware.pl.workoutguide.dashboards;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import workoutguide.pentagramsoftware.pl.workoutguide.BaseActivity;
import workoutguide.pentagramsoftware.pl.workoutguide.R;
import workoutguide.pentagramsoftware.pl.workoutguide.enums.ExcerciseTypesEnum;
import workoutguide.pentagramsoftware.pl.workoutguide.enums.IntensitiesEnum;
import workoutguide.pentagramsoftware.pl.workoutguide.eventbus.WorkoutEventBus;
import workoutguide.pentagramsoftware.pl.workoutguide.events.Event;
import workoutguide.pentagramsoftware.pl.workoutguide.events.ExcerciseEvent;
import workoutguide.pentagramsoftware.pl.workoutguide.events.TimerEvent;
import workoutguide.pentagramsoftware.pl.workoutguide.texttospeech.ExcerciseEventParser;
import workoutguide.pentagramsoftware.pl.workoutguide.texttospeech.TextToSpeechManager;

import static workoutguide.pentagramsoftware.pl.workoutguide.enums.ExcerciseTypesEnum.HIIT_CARDIO;
import static workoutguide.pentagramsoftware.pl.workoutguide.enums.ExcerciseTypesEnum.STEADY_CARDIO;
import static workoutguide.pentagramsoftware.pl.workoutguide.enums.ExcerciseTypesEnum.STRENGTH;

public class WorkoutDashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, WorkoutDashboardInterface {

    private final WorkoutEventBus workoutEventBus = WorkoutEventBus.getInstance();
    @BindView(R.id.main_activity_unsubscribe_event_bus)
    Button unsubscribeEvenBus;

    @BindView(R.id.main_activity_round_intensity_spinner)
    Spinner intensitySpinner;

    @BindView(R.id.main_activity_start_workout)
    Button startWorkout;

    @BindView(R.id.main_activity_number_of_rounds_edit_text)
    EditText numberOfRounds;

    @BindView(R.id.main_activity_round_duration_edit_text)
    EditText roundDuration;

    @BindView(R.id.main_activity_break_duration_edit_text)
    EditText breakDuration;

    @BindView(R.id.main_activity_current_round)
    TextView currentRound;

    @BindView(R.id.main_activity_total_rounds)
    TextView totalRounds;

    @BindView(R.id.main_activity_current_excercise)
    TextView currentExercise;

    @BindView(R.id.main_activity_workout_time)
    TextView workoutTime;
    private TextToSpeechManager textToSpeechManager;
    // todo switch from consumer to subscriber in observer!!!
    private Subscription eventBusSubscription;

    private CompositeDisposable disposable;

    // todo investigate if not better and better event objects aquisition for future and performance
//    private Queue<Pair<Long, ExerciseEvent>> eventQueue;

    private HashMap<Long, ExcerciseEvent> eventQueue;

    private WorkoutDashboardPresenter workoutDashboardPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disposable = new CompositeDisposable();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initSTTManager();
//        eventQueue = mockExcerciseQueue();
        initIntensitySpinner();
        initPresenter();

    }

    private void initPresenter() {
        if (workoutDashboardPresenter == null) {
            workoutDashboardPresenter = new WorkoutDashboardPresenter(this);
        }
    }

    private void initIntensitySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, IntensitiesEnum.toArray());
        intensitySpinner.setAdapter(adapter);
    }

    private HashMap<Long, ExcerciseEvent> generateWorkoutQueue(int rounds, IntensitiesEnum intensity, int roundDuration, int breakDuration) {
        @SuppressLint("UseSparseArrays") HashMap<Long, ExcerciseEvent> queue = new HashMap<>();
        int preparationTime = 20;
        queue.put((long) 0, new ExcerciseEvent.Builder()
                .setId(0)
                .duration(preparationTime)
                .type(ExcerciseTypesEnum.PREPARATION)
                .intensity(IntensitiesEnum.LOW)
                .build());
        for (int i = 0, size = rounds; i < size; i++) {
            queue.put((long) preparationTime + i * roundDuration + i * breakDuration, new ExcerciseEvent.Builder()
                    .setId(i + 1)
                    .duration(roundDuration)
                    .type(STRENGTH)
                    .intensity(IntensitiesEnum.HIGH)
                    .build());
            queue.put((long) preparationTime + (i + 1) * roundDuration + i * breakDuration, new ExcerciseEvent.Builder()
                    .setId(i * 10 + 1)
                    .duration(breakDuration)
                    .type(ExcerciseTypesEnum.REST)
                    .intensity(IntensitiesEnum.LOW)
                    .build());
        }
        return queue;
    }

    private HashMap<Long, ExcerciseEvent> mockExcerciseQueue() {
        @SuppressLint("UseSparseArrays") HashMap<Long, ExcerciseEvent> queue = new HashMap<>();
        queue.put((long) 10, new ExcerciseEvent.Builder()
                .setId(0)
                .duration(10)
                .type(STRENGTH)
                .intensity(IntensitiesEnum.HIGH)
                .build());
        queue.put((long) 20, new ExcerciseEvent.Builder()
                .setId(0)
                .duration(10)
                .type(ExcerciseTypesEnum.REST)
                .intensity(IntensitiesEnum.LOW)
                .build());
        queue.put((long) 30, new ExcerciseEvent.Builder()
                .setId(0)
                .duration(10)
                .type(STRENGTH)
                .intensity(IntensitiesEnum.HIGH)
                .build());
        queue.put((long) 40, new ExcerciseEvent.Builder()
                .setId(0)
                .duration(10)
                .type(ExcerciseTypesEnum.REST)
                .intensity(IntensitiesEnum.LOW)
                .build());
        queue.put((long) 50, new ExcerciseEvent.Builder()
                .setId(0)
                .duration(10)
                .type(STRENGTH)
                .intensity(IntensitiesEnum.HIGH)
                .build());
        return queue;
    }

    private Disposable observeEventBus() {
        final ExcerciseEventParser parser = ExcerciseEventParser.getInstance();
        return workoutEventBus.getSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Event>() {
                    @Override
                    public void accept(Event event) throws Exception {
                        if (event instanceof ExcerciseEvent) {
                            if (event != null) {
                                ExcerciseEvent excerciseEvent = (ExcerciseEvent) event;
                                if (isExcercise(excerciseEvent.getType())) {
                                    currentRound.setText(String.format("%d", excerciseEvent.getId()));
                                }
                                currentExercise.setText(excerciseEvent.getType().toString());
                                textToSpeechManager.speakAndDiscardCurentSpeech(parser.ParseEvent(WorkoutDashboardActivity.this, excerciseEvent));
                            } else {
                                Timber.e("ExcerciseEvent object is null!!");
                            }

                        }
                    }
                });
    }

    private Disposable observeTime() {
        return workoutEventBus.getSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Event>() {
                    @Override
                    public void accept(Event event) throws Exception {
                        if (event instanceof TimerEvent) {
                            TimerEvent timerEvent = (TimerEvent) event;
                            workoutTime.setText(String.format("Time %d", timerEvent.getTime()));

                        }
                    }
                });
    }

    private boolean isExcercise(ExcerciseTypesEnum type) {
        return type == STRENGTH || type == STEADY_CARDIO || type == HIIT_CARDIO;
    }

    private void initEmitter() {
        disposable.add(Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        WorkoutEventBus.getInstance().publishEvent(new TimerEvent
                                .TimerEventBuilder()
                                .setTime(aLong)
                                .build());
                        ExcerciseEvent currentEvent;
                        if (eventQueue.containsKey(aLong)) {
                            currentEvent = eventQueue.remove(aLong);
                            WorkoutEventBus.getInstance().publishEvent(currentEvent);
                        }
                    }
                }));
    }

    @OnClick(R.id.main_activity_start_workout)
    void startWorkout() {
        eventQueue = generateWorkoutQueue(Integer.parseInt(numberOfRounds.getText().toString()),
                IntensitiesEnum.HIGH, Integer.parseInt(roundDuration.getText().toString()),
                Integer.parseInt(breakDuration.getText().toString()));

        disposable.add(observeEventBus());
        disposable.add(observeTime());
        initEmitter();
        totalRounds.setText(String.format("%d", (eventQueue.size() - 1) / 2));

    }

    @OnClick(R.id.main_activity_unsubscribe_event_bus)
    void unsubscribeEventBus() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            unsubscribeEvenBus.setText("SUBSCRIBE");
        } else {
            disposable.add(observeEventBus());
            unsubscribeEvenBus.setText("UNSUBSCRIBE");
        }
    }

    private void initSTTManager() {
        textToSpeechManager = TextToSpeechManager.getInstance();
        textToSpeechManager.initEngine(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
        textToSpeechManager.terminate();
        super.onDestroy();
    }

    @Override
    public Context aquireContext() {
        return this;
    }

    @Override
    public Activity aquireActivity() {
        return this;
    }

    @Override
    public FragmentManager aquireFragmentManager() {
        return getFragmentManager();
    }

    @Override
    public Resources aquireResources() {
        return getResources();
    }
}
