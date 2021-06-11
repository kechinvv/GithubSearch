package com.kechinvv.ghsearch.repository;

import java.io.IOException;
import java.nio.file.Path;

public class RemoteRepository {
    private final String url;
    private final String name;

    public RemoteRepository(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LocalRepository cloneTo(Path path) throws InterruptedException, IOException {
        Process proc = new ProcessBuilder("git", "clone", "--depth=1", "--recurse-submodules", url, path.toString()).start();
        proc.waitFor();
        proc.destroy();
        return new LocalRepository(path);
    }

}
