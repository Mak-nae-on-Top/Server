package com.maknaeontop.blueprint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessBuilder {
    private final String PYTHON_MODULE_PATH = "/home/ubuntu/Server/src/main/resources/python/";
    //private final String PYTHON_MODULE_PATH = "C:/Users/namu/Documents/gitWorkspace/Server/src/main/resources/python/";

    /**
     * Method to execute 'convertImageToMap.py' to create map using base64 image.
     *
     * @param arg   the first arg which is file path
     * @return      true if executing file is successful
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean executeConvertImageToMapModule(String arg) throws IOException, InterruptedException {
        String response = buildProcess( PYTHON_MODULE_PATH + "convertImageToMap.py", arg);
        return response == null ?  false : true;
    }

    /**
     * Method to execute 'Astar.py' to find the optimized route.
     *
     * @param arg1  the first arg which is users location list
     * @param arg2  the second arg which is destinations location list
     * @param arg3  the third arg which is image file path
     * @return      the optimized route
     * @throws IOException
     * @throws InterruptedException
     */
    public String executeFindRouteModule(String arg1, String arg2, String arg3) throws IOException, InterruptedException {
        return buildProcess( PYTHON_MODULE_PATH + "Astar.exe", arg1 + " " + arg2 + " " + arg3);
    }

    /**
     * Method to generate and execute a sub-process with arg.
     *
     * @param command   the command to execute
     * @param arg       the arg which is delivered with the command
     * @return          the return value of sub-process
     * @throws IOException
     * @throws InterruptedException
     */
    private String buildProcess(String command, String arg) throws IOException, InterruptedException {
        java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder(command, arg);
        Process process = builder.start();
        int exitVal = process.waitFor();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        if(exitVal != 0) {
            System.out.println("process terminated abnormally");
            return null;
        }
        return sb.toString();
    }
}
