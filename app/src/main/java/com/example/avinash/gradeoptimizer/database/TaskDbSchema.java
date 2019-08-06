package com.example.avinash.gradeoptimizer.database;

public class TaskDbSchema {
    public static final class TaskTable {
        public static final String NAME = "tasks";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String STATUS = "completed";
            public static final String SESSIONS="sessions";
        }
    }
}
