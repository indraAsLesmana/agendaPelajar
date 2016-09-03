package com.tutor93.agendapelajarv4.models;

/**
 * Created by indra on 05/08/2016.
 */

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Schedule {

    public String uid;
    public String scheduleName;
    public String teacher;
    public String room;
    public String startTime;
    public String endTime;
    public String day;

    public Schedule() {
    }

    public Schedule(String uid, String scheduleName, String teacher, String room, String startTime, String endTime, String day){

        this.uid = uid;
        this.scheduleName = scheduleName;
        this.teacher = teacher;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("scheduleName", scheduleName);
        result.put("teacher", teacher);
        result.put("room", room);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("day", day);


        return result;
    }
    // [END post_to_map]

}
