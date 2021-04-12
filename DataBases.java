package com.example.menu;

import static android.provider.BaseColumns._ID;

public class DataBases {
    public static final String _TABLENAME1 = "information";
    public static final String UserID = "userid";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String AGE = "age";
    public static final String SEX = "sex";

    public static final String _CREATE =
            "create table if not exists " + _TABLENAME1 + "("
                    +UserID+ " text primary key, "
                    + HEIGHT + " integer, "
                    + WEIGHT + " integer, "
                    + AGE + " integer, "
                    + SEX + " text);";

    static final int DATABASE_VERSION = 2;

    public static final String _TABLENAME2 = "stepsRecord";
    public static final String DATE = "date";
    public static final String STEPS = "steps";



    public static final String  _CREATE2 = "create table if not exists " + _TABLENAME2 + "("
            + UserID + " text, "
            + DATE + " text, "
            + STEPS + " integer,"

            + "constraint id_fk foreign key("+UserID+")"+" references "+_TABLENAME1+ "("+UserID+")"
            + "constraint id_pk primary key(userid, date));";
}
