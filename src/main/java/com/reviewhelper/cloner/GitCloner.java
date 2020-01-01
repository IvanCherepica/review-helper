package com.reviewhelper.cloner;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GitCloner {

    public String[] cloneAsProjects(String[] links, String[] names) throws GitAPIException {
        String[] paths = new String[links.length];

        for (int i = 0; i < links.length; i++) {
            String dirName = "testclone/" + names[i];
            paths[i] = dirName;

            createDir(dirName);
            cloneInToDir(links[i], dirName);
        }

        return paths;
    }

    private void cloneInToDir(String url, String dirName) throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(new File(dirName))
                .call();
    }

    private void createDir(String dirName) {
        Path path = Paths.get(dirName);

        if (Files.exists(path)) {
            //cleaning old files
            deletePathWithContent(path.toFile());
        }
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            //fail to create directory
            e.printStackTrace();
        }
    }

    private void deletePathWithContent(File file) {
        if (file.isDirectory()) {
            for (File fileInDirectory : file.listFiles()) {
                deletePathWithContent(fileInDirectory);
            }
        }
        file.delete();
    }
}
