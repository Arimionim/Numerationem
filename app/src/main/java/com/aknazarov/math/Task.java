package com.aknazarov.math;

import android.util.Log;

import java.util.Random;

import static java.lang.Math.max;

class Task {
    static final int ADD_SUB_POINT = 20;
    private static final int MUL_POINT = 30;
    private static final int MUL_POINT_DIVIDER = 8;
    private static final int RANDOM_MULTIPLIER = 5;
    private int point;
    private int answer;
    private String task;
    private String type;
    final private String[] ALL_TYPES = {"number", "add", "sub", "mul"};
    private Task left;
    private Task right;
    private Random random;

    Task(int point_) {
        point = max(point_, 0);
        type = "null";
        random = new Random();
    }

    void genTask() {
        if (point >= ADD_SUB_POINT) {
            int maxType = 2;
            if (point >= MUL_POINT) {
                maxType += 1;
            }
            int a = random.nextInt(maxType);
            type = ALL_TYPES[a + 1];
        } else {
            type = ALL_TYPES[0];
        }

        switch (type) {
            case "number":
                answer = random.nextInt((point + 1) * RANDOM_MULTIPLIER) + 1;
                task = answer + "";
                return;
            case "add":
                point -= ADD_SUB_POINT;
                task = "+";
                break;
            case "sub":
                point -= ADD_SUB_POINT;
                task = "-";
                break;
            case "mul":
                point -= MUL_POINT;
                point /= MUL_POINT_DIVIDER;
                task = "\u00B7";
                break;
            default:
                return;
        }

        point /= 2;
        left = new Task(point);
        left.genTask();
        right = new Task(point);
        right.genTask();

        if (left.type != "mul" && left.type != "number" && type != "add" && type != "sub"){
            task = "(" + left.getTask() + ")" + task;
        }
        else{
            task = left.getTask() + task;
        }

        if (right.type != "mul" && right.type != "number" && type != "add" && (type != "sub" || right.type == "sub" || right.type == "add")){
            task = task + "(" + right.getTask() + ")";
        }
        else{
            task = task + right.getTask();
        }

        switch (type) {
            case "add":
                answer = left.getAnswer() + right.getAnswer();
                break;
            case "sub":
                answer = left.getAnswer() - right.getAnswer();
                break;
            case "mul":
                answer = left.getAnswer() * right.getAnswer();
                break;
        }
    }

    String getTask() {
        if (type == "null") {
            Log.e("task", "try to get empty task");
            return "";
        } else {
            return task;
        }
    }

    int getAnswer() {
        if (type == "null" || task == "") {
            Log.e("task", "try to get empty answer");
            return 0;
        } else {
            return answer;
        }
    }

    void setPoint(int newPoint) {
        point = newPoint;
    }
}
