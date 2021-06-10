package com.kechinvv.ghsearch.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.setAttribute(file, "dos:readonly", false);
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                    throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public Stream<String> getKtStream() throws IOException {
        return Files.walk(path, FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .filter(f -> f.isFile() && f.getName().endsWith(".kt")).map(File::getAbsolutePath);
    }
}
