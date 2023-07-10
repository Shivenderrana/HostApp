package com.sonu.hostelmanagementsystem;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Locale;

final class SqliteExporter {
    static final String HOSTAPP_DIRECTORY = "/HostApp";

    static boolean export(SQLiteDatabase db, String tableName, String folder) throws IOException{
        if( !FileUtils.isExternalStorageWritable() ){
            throw new IOException("Cannot write to external storage");
        }
        File backupDir = FileUtils.createDirIfNotExist(FileUtils.getExternalDir() + HOSTAPP_DIRECTORY + File.separator + folder);
        String fileName = createBackupFileName(tableName);
        File backupFile = new File(backupDir, fileName);
        boolean success = backupFile.createNewFile();
        if(!success){
            throw new IOException("Failed to create the backup file");
        }
        //List<String> tables = getTablesOnDataBase(db);
        boolean isCsvWritten = writeCsv(backupFile, db, tableName);
        if(!isCsvWritten) backupFile.delete();
        return isCsvWritten;
    }

    private static String createBackupFileName(String tableName){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmm", Locale.getDefault());
        return tableName + "_" + sdf.format(new Date()) + ".csv";
    }

    /**
     * Get all the table names we have in db
     *
     * @param db
     * @return
     */
    /*public static List<String> getTablesOnDataBase(SQLiteDatabase db){
        Cursor c = null;
        List<String> tables = new ArrayList<>();
        try{
            c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    tables.add(c.getString(0));
                    c.moveToNext();
                }
            }
        }
        catch(Exception throwable){
            Log.e(TAG, "Could not get the table names from db", throwable);
        }
        finally{
            if(c!=null)
                c.close();
        }
        return tables;
    }*/

    private static boolean writeCsv(File backupFile, SQLiteDatabase db, String tableName){
        CSVWriter csvWrite = null;
        Cursor curCSV = null;
        try {
            csvWrite = new CSVWriter(new FileWriter(backupFile));
                curCSV = db.rawQuery("SELECT * FROM " + tableName,null);
                csvWrite.writeNext(curCSV.getColumnNames());
                while(curCSV.moveToNext()) {
                    int columns = curCSV.getColumnCount();
                    String[] columnArr = new String[columns];
                    for( int i = 0; i < columns; i++){
                        columnArr[i] = curCSV.getString(i);
                    }
                    csvWrite.writeNext(columnArr);
                }
            return true;
        }
        catch(Exception sqlEx) {
        }finally {
            if(csvWrite != null){
                try {
                    csvWrite.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if( curCSV != null ){
                curCSV.close();
            }
        }
        return false;
    }

    /*private static void writeSingleValue(CSVWriter writer, String value){
        writer.writeNext(new String[]{value});
    }*/

}