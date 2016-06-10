package com.app.spyapp.model;

/**
 * Created by Ankit on 5/14/2016.
 */
public class AudioRecordModel {
    private String filename;
    private String filepath;
    private String recordDate;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }
}
