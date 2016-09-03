package com.tutor93.agendapelajarv4;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tutor93.agendapelajarv4.models.Schedule;

public class AddOrUpdateScheduleActivity extends BaseActivity {

    private Button bOK, bCancel;
    private Schedule schedule;
    private int position;
    private EditText pScheduleName, pTeacherName, pRoom;
    private TextView tDay, tStartTIme, tEndTIme;
    private CoordinatorLayout cl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newschedule);

        position = getIntent().getIntExtra("Position", -1);

        cl = (CoordinatorLayout) findViewById(R.id.cdlayout);

        pScheduleName = (EditText) findViewById(R.id.schdulename_et);
        pTeacherName = (EditText) findViewById(R.id.teacher_et);
        pRoom = (EditText) findViewById(R.id.room_et);

        tDay = (TextView) findViewById(R.id.day_et);
        tStartTIme = (TextView) findViewById(R.id.startime_et);
        tEndTIme = (TextView) findViewById(R.id.endtime_et);

        bOK = (Button) findViewById(R.id.bOk);
        bCancel = (Button) findViewById(R.id.bCancel);


        if (position != -1) {
            getSupportActionBar().setTitle("Edit Entry");
            searchSchedule(position);
            schedule = new Schedule();
        } else {
            getSupportActionBar().setTitle("Add Entry");
            schedule = null;
        }

        bOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pScheduleName.getText().toString().trim().equals("") ||
                        pTeacherName.getText().toString().trim().equals("") ||
                        pRoom.getText().toString().trim().equals("") ||
                        tDay.getText().toString().trim().equals("")||
                        tStartTIme.getText().toString().trim().equals("")||
                        tEndTIme.getText().toString().trim().equals("")) {
                    final Snackbar snackBar = Snackbar.make(cl, "Please enter all the fields.", Snackbar.LENGTH_LONG);
                    snackBar.setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackBar.dismiss();
                        }
                    });
                    snackBar.show();
                } else {
                    Schedule p = new Schedule();
                    p.setScheduleName(pScheduleName.getText().toString());
                    p.setTeacher(pTeacherName.getText().toString());
                    p.setRoom(pRoom.getText().toString());
                    p.setDay(tDay.getText().toString());
                    p.setStartTime(tStartTIme.getText().toString());
                    p.setEndTime(tEndTIme.getText().toString());

                    if (schedule == null)
                        ScheduleActivity.getInstance().addSchedule(p);
                    else
                        ScheduleActivity.getInstance().updateScheduleDetails(p, position);
                    finish();
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public void searchSchedule(int position) {
        String clickedKey = ScheduleActivity.getInstance().getKeysArray().get(position);
        ScheduleActivity.getInstance().getmDatabase().child("schedule").child(getUid()).child(clickedKey)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Schedule person = dataSnapshot.getValue(Schedule.class);
                                pScheduleName.setText(person.getScheduleName());
                                pTeacherName.setText(person.getTeacher());
                                pRoom.setText(person.getRoom());
                                tDay.setText(person.getDay());
                                tStartTIme.setText(person.getStartTime());
                                tEndTIme.setText(person.getEndTime());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
    }
}
