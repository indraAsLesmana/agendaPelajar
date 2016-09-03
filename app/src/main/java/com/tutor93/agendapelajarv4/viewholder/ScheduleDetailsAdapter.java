package com.tutor93.agendapelajarv4.viewholder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tutor93.agendapelajarv4.AddOrUpdateScheduleActivity;
import com.tutor93.agendapelajarv4.R;
import com.tutor93.agendapelajarv4.ScheduleActivity;
import com.tutor93.agendapelajarv4.models.Schedule;

import java.util.ArrayList;

public class ScheduleDetailsAdapter extends BaseAdapter {
    private ArrayList<Schedule> arrayListSchedule;
    private Context context;
    private LayoutInflater inflater;

    public ScheduleDetailsAdapter(Context context, ArrayList<Schedule> arrayListSchedule) {
        this.context = context;
        this.arrayListSchedule = arrayListSchedule;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayListSchedule.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListSchedule.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;
        if (v == null) {
            v = inflater.inflate(R.layout.listview_schedule, null);
            holder = new Holder();
            holder.ClassName = (TextView) v.findViewById(R.id.schedule_classname);
            holder.TeacherName = (TextView) v.findViewById(R.id.schedule_teacher);
            holder.RoomNumber = (TextView) v.findViewById(R.id.schedule_room);
            holder.StartTime = (TextView) v.findViewById(R.id.schedule_starttime);
            holder.EndTime = (TextView) v.findViewById(R.id.schedule_endtime);

            holder.EditSchedule = (ImageView) v.findViewById(R.id.schedule_edit);
            holder.DeleteSchedule = (ImageView) v.findViewById(R.id.schedule_delete);
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }
        arrayListSchedule.get(position).getUid();
        holder.ClassName.setText(String.valueOf(arrayListSchedule.get(position).getScheduleName()));
        holder.TeacherName.setText(String.valueOf(arrayListSchedule.get(position).getTeacher()));
        holder.RoomNumber.setText(String.valueOf(arrayListSchedule.get(position).getRoom()));
        holder.StartTime.setText(String.valueOf(arrayListSchedule.get(position).getStartTime()));
        holder.EndTime.setText(String.valueOf(arrayListSchedule.get(position).getEndTime()));
        holder.EditSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddOrUpdateScheduleActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Position", position);
                context.getApplicationContext().startActivity(intent);
            }
        });
        holder.DeleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowConfirmDialog(context, position);
            }
        });
        return v;
    }

    class Holder {
        TextView ClassName, TeacherName, RoomNumber, StartTime, EndTime;
        ImageView DeleteSchedule, EditSchedule;
    }

    public static void ShowConfirmDialog(Context context, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage("Are you sure you want to delete this entry?")
                .setCancelable(true)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ScheduleActivity.getInstance().deleteSchedule(position);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}