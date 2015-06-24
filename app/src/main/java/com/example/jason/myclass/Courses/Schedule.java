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
    public boolean geValidity(){return valid;}
    public Schedule(String input){
        valid = true;
        String[] input_array = input.split("\\s+");

        List<String> input_list = Arrays.asList(input_array);
       /* for(int i = 0; i < 100; i += 1){
            Log.d("print this" + i, input_list.get(i));
        }
        */
        int start = input_list.indexOf("Waterloo");
        int length = input_list.size();
        if (start != -1){
            start += 1;
        }else{
            /** cant find the target string */
            valid = false;
            return;

        }
        String course = input_list.get(start) + " " + input_list.get(start+1);
        courses = new CourseInfo[10];
        int i = 0;
        while(length > 1){
            if(i != 0){
                course = input_list.get(0) + " " + input_list.get(1);
            }
            courses[i] = new CourseInfo(course);
            int index = input_list.indexOf("Grade");
            courses[i].setNum(input_list.get(index + 2));
           // Log.d("we want this is nbr", input_list.get(index + 2));
            courses[i].setSec(input_list.get(index + 3));
            if(courses[i].isOnline){
                index = input_list.indexOf("RoomTBA");
                String prof = input_list.get(index + 1);
                int stringlen = prof.length();
                prof = prof.substring(10, stringlen);
                prof = prof + " " + input_list.get(index + 2);
                //Log.d("pppppppppppppppppppppppppppppppp is ",prof);
                courses[i].setProf(prof);
                index = input_list.indexOf("Materials");
                input_list = input_list.subList(index + 1, length);
                length = input_list.size();
                //Log.d("sub string length is", Integer.toString(length));
            }else{
                index = input_list.indexOf("Days");
                String time = input_list.get(index + 2);
                time = time.substring(5,time.length());
                time = time + " " + input_list.get(index + 3) + input_list.get(index + 4) + input_list.get(index + 5);
                courses[i].setTime(time);
                String room = input_list.get(index + 6);
                room = room.substring(4,room.length());
                room = room + input_list.get(index + 7);
                courses[i].setLoc(room);
                String prof = input_list.get(index + 8);
                //Log.d("kkkkkkkkkkkkkkkkkkkkkk  is ", prof);
                int stringlen = prof.length();
                prof = prof.substring(10, stringlen);
                prof = prof + " " + input_list.get(index + 9);
                courses[i].setProf(prof);
                index = input_list.indexOf("ComponentTUT");
                if(index == -1){
                    index = input_list.indexOf("Information");
                    input_list = input_list.subList(index + 1, length);
                    length = input_list.size();
                }else{
                    courses[i].setTutname();
                    courses[i].setTutnum(input_list.get(index - 2));
                    courses[i].setTutsec(input_list.get(index - 1));
                    time = input_list.get(index + 3);
                    time = time.substring(5, time.length());
                    time = time + " " + input_list.get(index + 4) + input_list.get(index + 5) + input_list.get(index + 6);
                    courses[i].setTutTime(time);
                    room = input_list.get(index + 7);
                    room = room.substring(4, room.length());
                    room = room + input_list.get(index + 8);
                    courses[i].setTutLoc(room);
                    index = input_list.indexOf("Information");
                    input_list = input_list.subList(index + 1, length);
                    length = input_list.size();
                }
            }

            i += 1;
        }
        NumofCourse = i;

    }

}
