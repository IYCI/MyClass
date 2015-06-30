package com.example.jason.myclass.Courses;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Lincoln on 2015-06-03.
 */
public class Schedule {
    protected CourseInfo courses[];
    protected boolean valid;
    protected int NumofCourse;

    public CourseInfo getCourse(int position){
        return courses[position];
    }
    public int getNumofCourse(){return NumofCourse;}
    public boolean getValidity(){return valid;}
    public Schedule(String input) {
        valid = true;
        String[] input_array = input.split("\\s+");

        List<String> input_list = Arrays.asList(input_array);
       /* for(int i = 0; i < 100; i += 1){
            Log.d("print this" + i, input_list.get(i));
        }
        */
        int start = input_list.indexOf("Waterloo");
        int length = input_list.size();
        if (start != -1) {
            start += 1;
        } else {
            /** cant find the target string */
            valid = false;
            return;

        }
        String course = input_list.get(start) + " " + input_list.get(start + 1);
        courses = new CourseInfo[10];
        int i = 0;
        while (length > 1) {
            if (i != 0) {
                course = input_list.get(0) + " " + input_list.get(1);
            }
            courses[i] = new CourseInfo(course);
            int index = input_list.indexOf("ComponentLEC");
            if (index == -1) {
                index = input_list.indexOf("ComponentSTU");
                if (index == -1) {
                    index = input_list.indexOf("ComponentSEM");
                    if (index == -1) {
                        index = input_list.indexOf("ComponentPRJ");
                        if (index == -1) {
                            index = input_list.indexOf("ComponentRDG");
                            if (index == -1) {
                                index = input_list.indexOf("ComponentFLT");
                                if (index == -1) {
                                    index = input_list.indexOf("ComponentPRA");
                                    //if (index == -1) {
                                    //    index = input_list.indexOf("ComponentLAB");
                                    //}
                                }

                            }
                        }
                    }
                }
            }
            if(index == -1){
                index = input_list.indexOf("Information");
                int index2 = input_list.indexOf("Materials");
                if(index2 < index){
                    index = index2;
                }
                input_list = input_list.subList(index + 1, length);
                length = input_list.size();
                continue;
            }else{
                /*int index2 = input_list.indexOf("Information");
                if (index2 < index){
                    index = index2;
                    input_list = input_list.subList(index + 1, length);
                    length = input_list.size();
                    continue;
                }*/

            }
            String num = input_list.get(index - 2);
            if(num.length() > 3){
                num = num.substring(3,num.length());
            }
            courses[i].setNum(num);
            // Log.d("we want this is nbr", input_list.get(index + 2));
            String sec = input_list.get(index - 1);
            if(sec.length() > 7) {
                sec = sec.substring(7, sec.length());
            }else {
                sec = "";
            }
            courses[i].setSec(sec);
            if (courses[i].isOnline) {
                index = input_list.indexOf("RoomTBA");
                String prof = input_list.get(index + 1);
                int stringlen = prof.length();
                if(stringlen > 10) {
                    prof = prof.substring(10, stringlen);
                    if (prof.equals("Staff")) {
                    } else {
                        prof = prof + " " + input_list.get(index + 2);
                        int getprofname = 3;
                        while (!(input_list.get(index + getprofname).equals("Start/End"))){
                            prof = prof + " " + input_list.get(index + getprofname);
                            getprofname += 1;
                        }
                    }
                }
                //Log.d("pppppppppppppppppppppppppppppppp is ",prof);
                courses[i].setProf(prof);
                index = input_list.indexOf("Materials");
                input_list = input_list.subList(index + 1, length);
                length = input_list.size();
                //Log.d("sub string length is", Integer.toString(length));
            } else {
                //index = input_list.indexOf("Days");
                String time = input_list.get(index + 3);
                int timelen = time.length();
                if(timelen >5) {
                    time = time.substring(5, timelen);
                    time = time + " " + input_list.get(index + 4) + input_list.get(index + 5) + input_list.get(index + 6);
                }else {
                    time = "";
                }
                courses[i].setTime(time);
                String room = input_list.get(index + 7);
                if (room.equals("RoomTBA")) {
                    courses[i].setLoc(room);
                    String prof = input_list.get(index + 9);
                    int stringlen = prof.length();
                    if(stringlen > 10) {
                        prof = prof.substring(10, stringlen);
                        if (prof.equals("Staff")) {
                        } else {
                            prof = prof + " " + input_list.get(index + 10);
                            int getprofname = 11;
                            while (!(input_list.get(index + getprofname).equals("Start/End"))){
                                prof = prof + " " + input_list.get(index + getprofname);
                                getprofname += 1;
                            }
                        }
                    }
                    courses[i].setProf(prof);
                } else {
                    int roomlen = room.length();
                    if(roomlen > 4) {
                        room = room.substring(4, roomlen);
                        room = room + input_list.get(index + 8);
                    }else{
                        room = "";
                    }
                    courses[i].setLoc(room);
                    String prof = input_list.get(index + 9);
                    int stringlen = prof.length();
                    if(stringlen > 10) {
                        prof = prof.substring(10, stringlen);
                        if (prof.equals("Staff")) {
                        } else {
                            prof = prof + " " + input_list.get(index + 10);
                            int getprofname = 11;
                            while (!(input_list.get(index + getprofname).equals("Start/End"))){
                                prof = prof + " " + input_list.get(index + getprofname);
                                getprofname += 1;
                            }
                        }
                    }
                    courses[i].setProf(prof);
                }

                index = input_list.indexOf("ComponentTUT");
                if (index == -1) {
                } else {
                    courses[i].setTutname();
                    String tutnum = input_list.get(index - 2);
                    if(tutnum.length() > 3){
                        tutnum = tutnum.substring(3,tutnum.length());
                    }
                    courses[i].setTutnum(tutnum);
                    String tutsec = input_list.get(index - 1);
                    if(tutsec.length() > 7) {
                        tutsec = tutsec.substring(7, tutsec.length());
                    }else {
                        tutsec = "";
                    }
                    courses[i].setTutsec(tutsec);
                    time = input_list.get(index + 3);
                    timelen = time.length();
                    if(timelen > 5) {
                        time = time.substring(5, timelen);
                        time = time + " " + input_list.get(index + 4) + input_list.get(index + 5) + input_list.get(index + 6);
                    }else {
                        time = "";
                    }
                    courses[i].setTutTime(time);
                    room = input_list.get(index + 7);
                    int roomlen = room.length();
                    if(roomlen > 4) {
                        room = room.substring(4, roomlen);
                        room = room + input_list.get(index + 8);
                    }else{
                        room = "";
                    }
                    courses[i].setTutLoc(room);
                }
                index = input_list.indexOf("ComponentLAB");

                if(index == -1){
                }else{
                    int index2 = input_list.indexOf("Information");
                    if(index > index2){
                    }else{
                        courses[i].setlabName();
                        String labnum = input_list.get(index - 2);
                        if(labnum.length() > 3){
                            labnum = labnum.substring(3,labnum.length());
                        }
                        courses[i].setlabNum(labnum);
                        String labsec = input_list.get(index - 1);
                        if(labsec.length() > 7) {
                            labsec = labsec.substring(7, labsec.length());
                        }else {
                            labsec = "";
                        }
                        courses[i].setlabSec(labsec);
                        time = input_list.get(index + 3);
                        timelen = time.length();
                        if(timelen > 5) {
                            time = time.substring(5, timelen);
                            time = time + " " + input_list.get(index + 4) + input_list.get(index + 5) + input_list.get(index + 6);
                        }else {
                            time = "";
                        }
                        courses[i].setlabTime(time);
                        room = input_list.get(index + 7);
                        int roomlen = room.length();
                        if(roomlen > 4) {
                            room = room.substring(4, roomlen);
                            room = room + input_list.get(index + 8);
                        }else{
                            room = "";
                        }
                        courses[i].setlabLoc(room);
                    }
                }
                index = input_list.indexOf("Information");
                input_list = input_list.subList(index + 1, length);
                length = input_list.size();
            }
            i += 1;
        }
        NumofCourse = i;

    }

}