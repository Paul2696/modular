package com.modular;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        System.out.println(format.parse("2019-10-11T20:30:31.129Z"));
    }
}
