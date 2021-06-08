package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class LocalRepository {
    private final String path;

    public LocalRepository(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public boolean delete() throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            Files.walk(file.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        if (!file.exists()) return true;
        return file.delete();
    }

    public Stream<String> getKtStream() throws IOException {
        return Files.walk(Paths.get(path), FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .filter(f-> f.isFile() && f.getName().endsWith(".kt")).map(File::getAbsolutePath);
    }
}
