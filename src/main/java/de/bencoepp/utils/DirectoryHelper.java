package de.bencoepp.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DirectoryHelper {

    public static List<File> getFilesFromDirectory(String dir) throws IOException {
        List<File> list = Files.walk(Paths.get(dir))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        for (ListIterator<File> iter = list.listIterator(); iter.hasNext(); ) {
            File element = iter.next();
            if(element.getCanonicalPath().contains(".git")){
                iter.remove();
                continue;
            }
            if(element.getCanonicalPath().contains(".idea")){
                iter.remove();
                continue;
            }
        }
        return list;
    }
}
