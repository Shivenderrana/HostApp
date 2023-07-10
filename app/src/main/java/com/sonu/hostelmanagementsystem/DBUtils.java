package com.sonu.hostelmanagementsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.sonu.hostelmanagementsystem.AttendanceSummaryActivity.ATTENDANCE_COLUMN_COUNT;
import static com.sonu.hostelmanagementsystem.SetAttendanceActivity.ABSENT;
import static com.sonu.hostelmanagementsystem.SetAttendanceActivity.PRESENT;
import static com.sonu.hostelmanagementsystem.SqliteExporter.HOSTAPP_DIRECTORY;


final class DBUtils {
    static final String HOSTEL_DATABASE = "hostel.db";
    static final String STUDENTS_TABLE = "students";
    private static final String COL_AADHAAR_NUMBER = "aadhaar_number";
    private static final String COL_FULL_NAME = "full_name";
    private static final String COL_FATHER_NAME = "father_name";
    private static final String COL_MOTHER_NAME = "mother_name";
    private static final String COL_ADDRESS = "address";
    private static final String COL_GENDER = "gender";
    private static final String COL_CURRENT_YEAR = "current_year";
    private static final String COL_DOB = "date_of_birth";
    private static final String COL_CONTACT = "contact";
    private static final String COL_RELATIVE_CONTACT = "relative_contact";
    private static final String COL_BRANCH = "branch";
    private static final String COL_ROOM_NUMBER = "room_number";
    private static final String COL_FATHER_CONTACT = "father_contact";
    private static final String IMPORT_DIRECTORY = "/Import";

    static boolean createStudentsTable(Context context) {
        try {
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
            String sql = "CREATE TABLE IF NOT EXISTS " + STUDENTS_TABLE + " (" +
                    COL_AADHAAR_NUMBER + " varchar(12) PRIMARY KEY NOT NULL," +
                    COL_FULL_NAME + " varchar(50) NOT NULL," +
                    COL_BRANCH + " varchar(50) NOT NULL," +
                    COL_CURRENT_YEAR + " varchar(1) NOT NULL," +
                    COL_ROOM_NUMBER + " varchar(5) NULL," +
                    COL_GENDER + " varchar(10) NOT NULL," +
                    COL_DOB + " varchar(10) NOT NULL," +
                    COL_FATHER_NAME + " varchar(50) NOT NULL," +
                    COL_MOTHER_NAME + " varchar(50) NOT NULL," +
                    COL_ADDRESS + " varchar(100) NOT NULL," +
                    COL_CONTACT + " varchar(15) NOT NULL," +
                    COL_FATHER_CONTACT + " varchar(15) NOT NULL," +
                    COL_RELATIVE_CONTACT + " varchar(15) NOT NULL" +
                    ");";
            sqLiteDatabase.execSQL(sql);
            sqLiteDatabase.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    static boolean insertStudent(Context context, String[] orderedDetails) {

        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_AADHAAR_NUMBER, orderedDetails[0]);
        contentValues.put(COL_FULL_NAME, orderedDetails[1]);
        contentValues.put(COL_BRANCH, orderedDetails[2]);
        contentValues.put(COL_CURRENT_YEAR, orderedDetails[3]);
        contentValues.put(COL_ROOM_NUMBER, orderedDetails[4]);
        contentValues.put(COL_GENDER, orderedDetails[5]);
        contentValues.put(COL_DOB, orderedDetails[6]);
        contentValues.put(COL_FATHER_NAME, orderedDetails[7]);
        contentValues.put(COL_MOTHER_NAME, orderedDetails[8]);
        contentValues.put(COL_ADDRESS, orderedDetails[9]);
        contentValues.put(COL_CONTACT, orderedDetails[10]);
        contentValues.put(COL_FATHER_CONTACT, orderedDetails[11]);
        contentValues.put(COL_RELATIVE_CONTACT, orderedDetails[12]);
        long result = sqLiteDatabase.insert(STUDENTS_TABLE, null, contentValues);
        sqLiteDatabase.close();
        return result != -1;
    }


    static boolean updateStudent(Context context, String[] orderedDetails) {

        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_AADHAAR_NUMBER, orderedDetails[0]);
        contentValues.put(COL_FULL_NAME, orderedDetails[1]);
        contentValues.put(COL_BRANCH, orderedDetails[2]);
        contentValues.put(COL_CURRENT_YEAR, orderedDetails[3]);
        contentValues.put(COL_ROOM_NUMBER, orderedDetails[4]);
        contentValues.put(COL_GENDER, orderedDetails[5]);
        contentValues.put(COL_DOB, orderedDetails[6]);
        contentValues.put(COL_FATHER_NAME, orderedDetails[7]);
        contentValues.put(COL_MOTHER_NAME, orderedDetails[8]);
        contentValues.put(COL_ADDRESS, orderedDetails[9]);
        contentValues.put(COL_CONTACT, orderedDetails[10]);
        contentValues.put(COL_FATHER_CONTACT, orderedDetails[11]);
        contentValues.put(COL_RELATIVE_CONTACT, orderedDetails[12]);

        String where = COL_AADHAAR_NUMBER + "=" + orderedDetails[0];
        int result = sqLiteDatabase.update(STUDENTS_TABLE, contentValues, where, null);
        sqLiteDatabase.close();
        return result > 0;
    }

    static boolean deleteStudent(Context context, String aadhaar) {
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
        int result = sqLiteDatabase.delete(STUDENTS_TABLE, COL_AADHAAR_NUMBER + "=" + aadhaar, null);
        sqLiteDatabase.close();
        return result > 0;
    }


    static boolean insertStudentAttendance(Context context, String date, String aadhaar, String fullname, String currentYear, String presentState) {

        String day;
        String month;
        String year;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
            try {
                Date d = simpleDateFormat.parse(date);
                d.getTime();
                SimpleDateFormat sdfDay = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat sdfMonth = new SimpleDateFormat("mm", Locale.getDefault());
                SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy", Locale.getDefault());
                day = sdfDay.format(d);
                month = sdfMonth.format(d);
                year = sdfYear.format(d);
            } catch (ParseException e) {
                return false;
            }

            String NameDay = "d" + day;
            String NameMonth = getStringMonth(month);
            String NameYear = year;
            final String DatabaseName = NameMonth + "_" + NameYear;


            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
            String checkSql = "SELECT " + COL_FULL_NAME + " FROM " + DatabaseName + " WHERE " + COL_AADHAAR_NUMBER + "=" + aadhaar;
            Cursor cursor = sqLiteDatabase.rawQuery(checkSql, null);
            if (cursor.moveToFirst()) {
                //There is a record
                //String sql = "UPDATE TABLE " + DatabaseName + " SET " + NameDay + "=" + presentState + " WHERE " + COL_AADHAAR_NUMBER + "=" + aadhaar;
                //sqLiteDatabase.execSQL(sql);
                //return true;
                ContentValues c = new ContentValues();
                c.put(NameDay, presentState);
                int rowsAffected = sqLiteDatabase.update(DatabaseName, c, COL_AADHAAR_NUMBER + "=" + aadhaar, null);
                sqLiteDatabase.close();
                return rowsAffected > 0;
            } else {
                //New Student
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_AADHAAR_NUMBER, aadhaar);
                contentValues.put(COL_FULL_NAME, fullname);
                contentValues.put(COL_CURRENT_YEAR, currentYear);
                contentValues.put(NameDay, presentState);
                long result = sqLiteDatabase.insert(DatabaseName, null, contentValues);
                sqLiteDatabase.close();
                return result != -1;
            }

        } catch (Exception e) {
            return false;
        }
    }


    static boolean exportDatabase(Context context, String folder) {
        File mainDatabaseFile = new File(Environment.getDataDirectory().getAbsolutePath() + "//data/" + context.getPackageName() + File.separator + "databases" +
                File.separator + HOSTEL_DATABASE);
        File backupDir = FileUtils.createDirIfNotExist(FileUtils.getExternalDir() + HOSTAPP_DIRECTORY + File.separator + folder);
        File backupFile = new File(backupDir, HOSTEL_DATABASE);
        try {
            if(backupFile.exists()) backupFile.delete();
            boolean success = backupFile.createNewFile();
            if (!success) {
                return false;
            }
            if (mainDatabaseFile.exists()) {

                if (!backupFile.exists()) {
                    boolean isBackupFileCreated = backupFile.createNewFile();
                    if (!isBackupFileCreated) return false;
                } else {
                    backupFile.delete();
                    boolean isBackupFileCreated = backupFile.createNewFile();
                    if (!isBackupFileCreated) return false;
                }
                FileChannel src = new FileInputStream(mainDatabaseFile).getChannel();
                FileChannel dst = new FileOutputStream(backupFile).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                return true;

            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    static boolean importDatabase(Context context) {
        File mainDBFile = new File(Environment.getDataDirectory().getAbsolutePath() + "//data/" + context.getPackageName() + File.separator + "databases" +
                File.separator + HOSTEL_DATABASE);
        File externalDBImportDir = FileUtils.createDirIfNotExist(FileUtils.getExternalDir() + HOSTAPP_DIRECTORY + IMPORT_DIRECTORY);
        File externalDBFile = new File(externalDBImportDir, HOSTEL_DATABASE);
        try {

            if (!externalDBFile.exists())
                return false;

            if (mainDBFile.exists()) {
                mainDBFile.delete();
                boolean success = mainDBFile.createNewFile();
                if (!success) {
                    return false;
                }
            }

            FileChannel src = new FileInputStream(externalDBFile).getChannel();
            FileChannel dst = new FileOutputStream(mainDBFile).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    static boolean deleteDatabase(Context context){
        File mainDatabaseFile = new File(Environment.getDataDirectory().getAbsolutePath() + "//data/" + context.getPackageName() + File.separator + "databases" +
                File.separator + HOSTEL_DATABASE);
        if(mainDatabaseFile.exists()){
            boolean isDeleted = mainDatabaseFile.delete();
            if(isDeleted) return true;
            else return false;
        }
        else {
            return true;
        }
    }

    static void createImportDirectory(Context context) {
        File externalDBImportDir = FileUtils.createDirIfNotExist(FileUtils.getExternalDir() + HOSTAPP_DIRECTORY + IMPORT_DIRECTORY);
        if (!externalDBImportDir.exists())
            externalDBImportDir.mkdirs();
    }


    static boolean deleteStudentsTable(Context context) {
        try {
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
            String sql = "DROP TABLE " + STUDENTS_TABLE;
            sqLiteDatabase.execSQL(sql);
            sqLiteDatabase.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    static boolean deleteStudentsAttendanceTable(Context context, String name) {
        try {
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
            String sql = "DROP TABLE " + name;
            sqLiteDatabase.execSQL(sql);
            sqLiteDatabase.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    static List<Student> loadStudentsName(Context context, int year) {
        try {
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
            String sql = "SELECT " + COL_AADHAAR_NUMBER + "," + COL_FULL_NAME + "," + COL_CURRENT_YEAR + " FROM " + STUDENTS_TABLE + " WHERE " + COL_CURRENT_YEAR + "=" + year;
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

            List<Student> students = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    Student student = new Student();
                    student.setAadhaar(cursor.getString(0));
                    student.setFullName(cursor.getString(1));
                    student.setCurrentYear(cursor.getString(2));
                    students.add(student);
                } while (cursor.moveToNext());
                cursor.close();
                sqLiteDatabase.close();
                return students;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    static Student getStudentDetails(Context context, String aadhaar) {
        try {
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
            String sql = "SELECT * FROM " + STUDENTS_TABLE + " WHERE " + COL_AADHAAR_NUMBER + "=" + aadhaar;
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                Student student = new Student();
                student.setAadhaar(cursor.getString(0));
                student.setFullName(cursor.getString(1));
                student.setBranch(cursor.getString(2));
                student.setCurrentYear(cursor.getString(3));
                student.setRoom(cursor.getString(4));
                student.setGender(cursor.getString(5));
                student.setDob(cursor.getString(6));
                student.setFatherName(cursor.getString(7));
                student.setMotherName(cursor.getString(8));
                student.setAddress(cursor.getString(9));
                student.setContact(cursor.getString(10));
                student.setFatherContact(cursor.getString(11));
                student.setRelativeContact(cursor.getString(12));
                cursor.close();
                sqLiteDatabase.close();
                return student;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    static String[][] getStudentAttendance(Context context, String month, String year) {
        try {
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
            String TableName = month + "_" + year;
            int total_records = 0;
            Cursor countCursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + TableName,null);
            if(countCursor.moveToFirst()) total_records = countCursor.getInt(0);
            countCursor.close();
            String sql = "SELECT * FROM " + TableName;
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);


            String[][] details = new String[total_records][ATTENDANCE_COLUMN_COUNT];
/*            if (cursor.moveToNext()) {
                for (int i = 0; i < 34; i++) {
                    details[cursor.getPosition()][i] = cursor.getString(i);
                }
            }
*/
            while(cursor.moveToNext()){
                for (int i = 0; i < ATTENDANCE_COLUMN_COUNT; i++) {
                    if(i==0) continue;
                    details[cursor.getPosition()][i-1] = cursor.getString(i);
                }
            }
            sqLiteDatabase.close();
            cursor.close();
            return details;
        } catch (Exception e) {
            return null;
        }
    }

    static StudentSummary getStudentSummary(Context context, String month, String year, String aadhaar) {
        try {
            String TableName = month + "_" + year;
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
            String sql = "SELECT * FROM " + TableName + " WHERE " + COL_AADHAAR_NUMBER + "=" + aadhaar;
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                StudentSummary studentSummary = new StudentSummary();
                studentSummary.setStudentName(cursor.getString(1));
                studentSummary.setStudentAcademicYear(cursor.getString(2));
                int totalPresent = 0, totalDays = 0;
                for (int i = 3; i <= 33; i++) {
                    if (cursor.getString(i) == null) continue;
                    if (cursor.getString(i).equals(PRESENT)) {
                        totalPresent++;
                        totalDays++;
                    } else if (cursor.getString(i).equals(ABSENT)) {
                        totalDays++;
                    }
                }
                studentSummary.setTotal_days_present(String.valueOf(totalPresent));
                studentSummary.setTotal_days(String.valueOf(totalDays));
                float attendancePercentage = (float) totalPresent / totalDays * 100;
                studentSummary.setStudentAttendancePercentage(String.valueOf(attendancePercentage));
                cursor.close();
                sqLiteDatabase.close();
                return studentSummary;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static boolean createStudentAttendanceTable(Context context, String date) {
        String day;
        String month;
        String year;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
            try {
                Date d = simpleDateFormat.parse(date);
                //d.getTime();
                SimpleDateFormat sdfDay = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat sdfMonth = new SimpleDateFormat("mm", Locale.getDefault());
                SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy", Locale.getDefault());
                day = sdfDay.format(d);
                month = sdfMonth.format(d);
                year = sdfYear.format(d);
            } catch (ParseException e) {
                return false;
            }

            String NameMonth = getStringMonth(month);
            String NameYear = year;
            final String DatabaseName = NameMonth + "_" + NameYear;

            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(HOSTEL_DATABASE, MODE_PRIVATE, null);
            String sql = "CREATE TABLE IF NOT EXISTS " + DatabaseName + " (" +
                    COL_AADHAAR_NUMBER + " varchar(12) NOT NULL PRIMARY KEY," +
                    COL_FULL_NAME + " varchar(50) NOT NULL," +
                    COL_CURRENT_YEAR + " varchar(1) NOT NULL," +
                    "d01 varchar(1)," +
                    "d02 varchar(1)," +
                    "d03 varchar(1)," +
                    "d04 varchar(1)," +
                    "d05 varchar(1)," +
                    "d06 varchar(1)," +
                    "d07 varchar(1)," +
                    "d08 varchar(1)," +
                    "d09 varchar(1)," +
                    "d10 varchar(1)," +
                    "d11 varchar(1)," +
                    "d12 varchar(1)," +
                    "d13 varchar(1)," +
                    "d14 varchar(1)," +
                    "d15 varchar(1)," +
                    "d16 varchar(1)," +
                    "d17 varchar(1)," +
                    "d18 varchar(1)," +
                    "d19 varchar(1)," +
                    "d20 varchar(1)," +
                    "d21 varchar(1)," +
                    "d22 varchar(1)," +
                    "d23 varchar(1)," +
                    "d24 varchar(1)," +
                    "d25 varchar(1)," +
                    "d26 varchar(1)," +
                    "d27 varchar(1)," +
                    "d28 varchar(1)," +
                    "d29 varchar(1)," +
                    "d30 varchar(1)," +
                    "d31 varchar(1)" +
                    ");";
            sqLiteDatabase.execSQL(sql);
            sqLiteDatabase.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private static String getStringMonth(String month) {
        switch (month) {
            case "01":
                return "january";
            case "02":
                return "february";
            case "03":
                return "march";
            case "04":
                return "april";
            case "05":
                return "may";
            case "06":
                return "june";
            case "07":
                return "july";
            case "08":
                return "august";
            case "09":
                return "september";
            case "10":
                return "october";
            case "11":
                return "november";
            case "12":
                return "december";
        }
        return null;
    }

}
