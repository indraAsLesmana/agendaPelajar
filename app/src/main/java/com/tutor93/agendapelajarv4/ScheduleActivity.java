package com.tutor93.agendapelajarv4;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutor93.agendapelajarv4.models.Schedule;
import com.tutor93.agendapelajarv4.viewholder.ScheduleDetailsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by indra on 06/08/2016.
 */


public class ScheduleActivity extends BaseActivity {

    static final String TAG = "Schedule Activity";

    private DatabaseReference mDatabase;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private ListView lvSchedule;
    private static ScheduleActivity scheduleActivity;
    int position;


    private static ArrayList<Schedule> arrayListSchedule = new ArrayList<>();
    private ScheduleDetailsAdapter scheduleDetailsAdapter;
    private ArrayList<String> keysArray;


    //final String userId = getUid();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        position = getIntent().getIntExtra("Position", -1);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //mDatabase.addChildEventListener(childEventListener);

        mDatabase.addValueEventListener(valueEventListener);

        scheduleActivity = this;

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        lvSchedule = (ListView) findViewById(R.id.PersonList);

        keysArray = new ArrayList<>();

        lvSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ScheduleActivity.this, CreateScheduleActivity.class);
                intent.putExtra("Position", position);
                startActivity(intent);
            }
        });


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScheduleActivity.this, CreateScheduleActivity.class));
            }
        });


        scheduleDetailsAdapter = new ScheduleDetailsAdapter(ScheduleActivity.this, arrayListSchedule);
        lvSchedule.setAdapter(scheduleDetailsAdapter);

        new Wait().execute();


    }


    private class Wait extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            lvSchedule.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                Log.d(TAG, ie.toString());
            }
            return (arrayListSchedule.size() == 0);
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool)
                updateView();
        }
    }

    public static ScheduleActivity getInstance() {
        return scheduleActivity;
    }

    public void deleteSchedule(int position) {
        final String clickedKey = keysArray.get(position);

        ScheduleActivity.getInstance().getmDatabase().child("schedule").child(getUid()).child(clickedKey)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Schedule deleteAlarm = dataSnapshot.getValue(Schedule.class);

                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                                notificationIntent.addCategory("android.intent.category.DEFAULT");
                                PendingIntent broadcast = PendingIntent.getBroadcast(ScheduleActivity.this,
                                        Integer.parseInt(deleteAlarm.getRoom())
                                        , notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                alarmManager.cancel(broadcast);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


        mDatabase.child("schedule").child(getUid()).child(clickedKey).removeValue();


        //Log.d(TAG, String.valueOf(mDatabase.child("schedule").child(getUid()).child(clickedKey).removeValue()));
    }

    public void addSchedule(Schedule model) {
        Schedule sc = new Schedule();
        sc.setUid(model.getUid());
        sc.setScheduleName(model.getScheduleName());
        sc.setTeacher(model.getTeacher());
        sc.setRoom(model.getRoom());
        sc.setStartTime(model.getStartTime());
        sc.setEndTime(model.getEndTime());


        String key = mDatabase.child("schedule").push().getKey();
        Map<String, Object> postValues = sc.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/schedule/" + getUid() + "/" + key, postValues);
        mDatabase.updateChildren(childUpdates);
    }


    public void updateScheduleDetails(final Schedule model, int position) {
        String clickedKey = keysArray.get(position);
        Schedule p = new Schedule();
        p.setUid(model.getUid());
        p.setScheduleName(model.getScheduleName());
        p.setTeacher(model.getTeacher());
        p.setRoom(model.getRoom());
        p.setStartTime(model.getStartTime());
        p.setEndTime(model.getEndTime());
        mDatabase.child("schedule").child(getUid()).child(clickedKey).setValue(p);
    }

    public ArrayList<String> getKeysArray() {
        return keysArray;
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public void updateView() {
        scheduleDetailsAdapter.notifyDataSetChanged();
        lvSchedule.invalidate();
        progressBar.setVisibility(View.GONE);
        lvSchedule.setVisibility(View.VISIBLE);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot postSnapshot : dataSnapshot.child("schedule").child(getUid()).getChildren()) {
                //Getting the data from snapshot

                Log.d(TAG, postSnapshot.getKey());
                Schedule person = postSnapshot.getValue(Schedule.class);
                arrayListSchedule.add(person);
                keysArray.add(postSnapshot.getKey());

            }
            updateView();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        arrayListSchedule.clear();
        mDatabase.removeEventListener(valueEventListener);
    }


}
