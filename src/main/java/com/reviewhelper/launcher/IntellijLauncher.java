package com.reviewhelper.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IntellijLauncher {

    private static final String OS_NAME = System.getProperty("os.name");

    private String commandUtil;
    private String launchCommand;

    public IntellijLauncher() {
        if (OS_NAME.equals("Linux")) {
            commandUtil = "bash";
            launchCommand = "idea";
        } else if (OS_NAME.toLowerCase().contains("windows")) {
            if (System.getProperty("os.arch").contains("64")) {
                launchCommand = "idea64";
            } else {
                launchCommand = "idea";
            }
            commandUtil = "cmd";
        }
    }

    public void launchIntellij(String[] filesString) {
        File[] files = new File[filesString.length];
        for (int i = 0; i < filesString.length; i++) {
            files[i] = new File(filesString[i]);
        }
        List<String> pomFilesPathList = searchPomFiles(files);
        if (OS_NAME.equals("Linux")) {
            for (String pomFile : pomFilesPathList) {
                System.out.println("Opening : " + pomFile);
                runLinuxCommand(launchCommand + " " + pomFile);
            }
        } else if (OS_NAME.toLowerCase().contains("windows")) {
            for (String pomFile : pomFilesPathList) {
                System.out.println("Opening : " + pomFile);
                runWindowsCommand(launchCommand + " " + pomFile);
            }
        }
    }

    private List<String> searchPomFiles(File[] files) {
        List<String> pomFilesPathList = new ArrayList<>();
        boolean foundPom = false;
        for (File file : files) {
            if (!file.isDirectory()) {
                if (file.getName().equals("pom.xml")) {
                    foundPom = true;
                    pomFilesPathList.add(file.getAbsolutePath());
                }
            }
        }
        if (!foundPom) {
            for (File file : files) {
                if (file.isDirectory()) {
                    pomFilesPathList.addAll(searchPomFiles(file.listFiles()));
                }
            }
        }
        return pomFilesPathList;
    }

    private void runLinuxCommand(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command(commandUtil, "-c", command);

        try {

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runWindowsCommand(String command) {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
