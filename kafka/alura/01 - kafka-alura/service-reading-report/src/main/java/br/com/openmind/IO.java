package br.com.openmind;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public class IO {


    public static void copyTo(Path source, File target) throws IOException {
        target.getParentFile().mkdirs();
        Files.copy(source, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void append(File target, String context) throws IOException {
        Files.write(target.toPath(), context.getBytes(), StandardOpenOption.APPEND);
    }
}