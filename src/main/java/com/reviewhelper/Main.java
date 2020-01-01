package com.reviewhelper;

import com.reviewhelper.cloner.GitCloner;
import com.reviewhelper.launcher.IntellijLauncher;

public class Main {
    public static void main(String[] args) {

        System.out.println("start");
        String[] studentNames = new String[args.length];
        String[] gitLinks = new String[args.length];

        for (int i = 0; i < args.length; i++) {
            String[] parts = args[i].split("@");
            studentNames[i] = parts[0];
            gitLinks[i] = parts[1];

            System.out.println(parts[0]);
            System.out.println(parts[1]);
        }

        System.out.println("parse successful");

        GitCloner cloner = new GitCloner();
        String[] projects = null;
        try {
            projects = cloner.cloneAsProjects(gitLinks, studentNames);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("clone successful");

        IntellijLauncher launcher = new IntellijLauncher();

        if (projects != null) {
            launcher.launchIntellij(projects);
        }

        System.out.println("launch successful");
    }
}
