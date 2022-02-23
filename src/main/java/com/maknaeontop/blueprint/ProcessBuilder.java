package com.maknaeontop.blueprint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * ProcessBuilder is a class for creating sub-processes in the server
 * and executing .py files related to image processing.
 */
public class ProcessBuilder {
    /**
     * Method to generate and execute a sub-process with arg.
     *
     * @param args      the arg list which contains command
     * @return          the return value of sub-process
     * @throws IOException
     * @throws InterruptedException
     */
    public String buildProcess(List<String> args) throws IOException, InterruptedException {
        java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder(args);

        Process process = builder.start();
        int exitVal = process.waitFor();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        if(exitVal != 0) {
            System.out.println("process terminated abnormally: " );
            return null;
        }
        return sb.toString();
    }
}
