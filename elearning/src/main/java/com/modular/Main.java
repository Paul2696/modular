package com.modular;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) throws ParseException {
        System.out.println(FileUtils.sizeOfDirectory(new File("/home/carlo/dev/workspace/jcs-common")));
    }
}
