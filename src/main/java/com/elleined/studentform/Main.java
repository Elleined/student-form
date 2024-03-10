package com.elleined.studentform;

import com.elleined.studentform.login.LoginForm;

class Main {
    public static void main(String[] args) {
        SQLiteConnection.createRegisterTable();
        SQLiteConnection.studentTable();
        LoginForm.getInstance();
    }
}
