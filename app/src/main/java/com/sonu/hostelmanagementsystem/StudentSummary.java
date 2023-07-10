package com.sonu.hostelmanagementsystem;

class StudentSummary {
    private String studentName;
    private String studentAcademicYear;
    private String studentAttendancePercentage;
    private String total_days;
    private String total_days_present;

    String getTotal_days() {
        return total_days;
    }

    void setTotal_days(String total_days) {
        this.total_days = total_days;
    }

    String getTotal_days_present() {
        return total_days_present;
    }

    void setTotal_days_present(String total_days_present) {
        this.total_days_present = total_days_present;
    }

    String getStudentName() {
        return studentName;
    }

    void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    String getStudentAcademicYear() {
        return studentAcademicYear;
    }

    void setStudentAcademicYear(String studentAcademicYear) {
        this.studentAcademicYear = studentAcademicYear;
    }

    String getStudentAttendancePercentage() {
        return studentAttendancePercentage;
    }

    void setStudentAttendancePercentage(String studentAttendancePercentage) {
        this.studentAttendancePercentage = studentAttendancePercentage;
    }
}
