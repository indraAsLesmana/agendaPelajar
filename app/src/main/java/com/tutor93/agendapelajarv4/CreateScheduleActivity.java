package com.tutor93.agendapelajarv4;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutor93.agendapelajarv4.models.Schedule;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by indra on 05/08/2016.
 */
public class CreateScheduleActivity extends BaseActivity {


    //cuman buat ngelogcat
    private static final String TAG = "PostDetailActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private EditText mScheduleName;
    private EditText mTeacherName;
    private EditText mRoom;
    private TextView mStartime, mEndtime, mDay;

    int getHours, getMenite;


    final String[] dayArray = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    // arraylist to keep the selected items
    // Boolean array for initial selected items
    final boolean[] chakedDay = new boolean[]{
            false, // Sunday
            false, // Monday
            false, // Tuesday
            false, // Wednesday
            false, // Thursday
            false, // Friday
            false // Saturday

    };

    // Convert the color array to list
    final List<String> daylist = Arrays.asList(dayArray);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newschedule);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]


        mScheduleName = (EditText) findViewById(R.id.schdulename_et);
        mTeacherName = (EditText) findViewById(R.id.teacher_et);
        mRoom = (EditText) findViewById(R.id.room_et);
        mStartime = (TextView) findViewById(R.id.startime_et);
        mEndtime = (TextView) findViewById(R.id.endtime_et);
        mDay = (TextView) findViewById(R.id.day_et);


        findViewById(R.id.bOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSchedule();
            }
        });


        findViewById(R.id.startime_et).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepicker(mStartime);
            }
        });
        findViewById(R.id.endtime_et).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepicker(mEndtime);
            }
        });
        findViewById(R.id.day_et).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDay();
            }
        });

    }

    private void pickDay() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select One Days")
                .setMultiChoiceItems(dayArray, chakedDay, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                        // Update the current focused item's checked status
                        chakedDay[which] = isChecked;

                        // Get the current focused item
                        String currentItem = daylist.get(which);

                        // Notify the current action
                        Toast.makeText(getApplicationContext(),
                                currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < chakedDay.length; i++) {
                            boolean checked = chakedDay[i];
                            if (checked) {
                                //mDay.setText(null);
                                mDay.setText(daylist.get(i));
                            }
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CreateScheduleActivity.this, "Cancel",
                                Toast.LENGTH_SHORT).show();
                    }
                }).create();
        dialog.show();

    }


    private int getDayInt() {
        if (mDay.getText() == "Sunday" || mDay.getText() == "Minggu") {
            return 1;
        } else if (mDay.getText() == "Monday" || mDay.getText() == "Senin") {
            return 2;
        } else if (mDay.getText() == "Tuesday" || mDay.getText() == "Selasa") {
            return 3;
        } else if (mDay.getText() == "Wednesday" || mDay.getText() == "Rabu") {
            return 4;
        } else if (mDay.getText() == "Thursday" || mDay.getText() == "Kamis") {
            return 5;
        } else if (mDay.getText() == "Friday" || mDay.getText() == "Jumat") {
            return 6;
        } else if (mDay.getText() == "Saturday" || mDay.getText() == "Sabtu") {
            return 7;
        } else {
            return 0;
        }

    }

    ;


    private void addAlarm(int alarmId) {


        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.DAY_OF_WEEK, getDayInt());
        calendar.set(calendar.HOUR_OF_DAY, getHours);
        calendar.set(calendar.MINUTE, getMenite);
        //calendar.set(calendar.SECOND, 0);
        //calendar.set(calendar.MILLISECOND, 0);
        long sdl = calendar.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, alarmId
                , notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, sdl, alarmManager.INTERVAL_DAY * 7, broadcast);

    }


    private void timepicker(final TextView clickView) {


        Calendar mcurrentTime = Calendar.getInstance();
        int hoursPic = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minutePic = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CreateScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                clickView.setText(selectedHour + ":" + selectedMinute);

                getHours = selectedHour;
                getMenite = selectedMinute;
            }
        }, hoursPic, minutePic, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }


    private void addSchedule() {
        final String scheduleName = mScheduleName.getText().toString();
        final String teacher = mTeacherName.getText().toString();
        final String room = mRoom.getText().toString();
        final String startTime = mStartime.getText().toString();
        final String endTime = mEndtime.getText().toString();
        final String day = mDay.getText().toString();

        if (TextUtils.isEmpty(scheduleName)) {
            mScheduleName.setError(REQUIRED);
        }

        if (TextUtils.isEmpty(teacher)) {
            mTeacherName.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(room)) {
            mRoom.setError(REQUIRED);
            return;
        }


        // [START single_value_read]
        final String userId = getUid();
        mDatabase.child("user").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                writeNewPost(userId, scheduleName, teacher, room, startTime, endTime, day);

                // Finish this Activity, back to the stream
                finish();
                // [END_EXCLUDE]

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());

            }
        });
    }

    // [START write_fan_out]

    private void writeNewPost(String userId, String scheduleName, String teacher,
                              String room, String startTime, String endTime, String day) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("schedule").push().getKey();
        Schedule schedule = new Schedule(userId, scheduleName, teacher, room, startTime, endTime, day);
        Map<String, Object> postValues = schedule.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/schedule/" + userId + "/" + key, postValues);
        mDatabase.updateChildren(childUpdates);

        //add alarm
        addAlarm(Integer.parseInt(room));
    }
    // [END write_fan_out]


}

