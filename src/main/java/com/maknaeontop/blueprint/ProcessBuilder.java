package com.maknaeontop.blueprint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessBuilder {
    private final String PYTHON_MODULE_PATH = "/home/ubuntu/Server/src/main/resources/python/";

    public String executeConvertImageToMapModule(String arg) throws IOException, InterruptedException {
        return buildProcess( PYTHON_MODULE_PATH + "convertImageToMap.py", arg);
    }

    private String buildProcess(String command, String arg) throws IOException, InterruptedException {
        java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder(command, arg);
        Process process = builder.start();
        int exitVal = process.waitFor();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }if(exitVal != 0) {
            System.out.println("process terminated abnormally");
        }

        return sb.toString();
    }
}
