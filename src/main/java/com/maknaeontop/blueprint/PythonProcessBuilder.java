package com.maknaeontop.blueprint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonProcessBuilder {
    private final String PYTHON_MODULE_PATH = "/home/ubuntu/Server/";

    public String executeMapBuilder(String arg) throws IOException, InterruptedException {
        return executeProcess(PYTHON_MODULE_PATH+"mapBuilder.exe", arg);
    }

    private String executeProcess(String command, String arg) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command, arg);
        Process process = builder.start();
        int exitVal = process.waitFor();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }if(exitVal != 0) {
            System.out.println("서브 프로세스가 비정상 종료되었다.");
        }

        return sb.toString();
    }
}
