package com.kechinvv.ghsearch.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class LocalRepository {
    private final Path path;

    public LocalRepository(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public void delete() throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .forEach(path1 -> {
                    try {
                        Files.delete(path1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        Files.delete(path);
    }

    public Stream<String> getKtStream() throws IOException {
        return Files.walk(path, FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .filter(f -> f.isFile() && f.getName().endsWith(".kt")).map(File::getAbsolutePath);
    }
}
