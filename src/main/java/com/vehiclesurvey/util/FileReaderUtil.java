package com.vehiclesurvey.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
/**
 * This is a utility class which deals with file related operations.
 * @author Nolan.Tellis
 *
 */
public class FileReaderUtil {

    /**
     * Checks if a file exists or not.
     * @param file A String file name.
     * @return <code>true</code> if file exists else <code>false</code>
     */
    public static boolean exists(String file) {
	Path path = Paths.get(file);

	return Files.exists(path);
    }

    /**
     * This method reads a file and returns a list of lines read from a file
     * @param file File name with path
     * @return List of Strings
     * @throws IOException Throws exception if file does not exists
     */
    public static List<String> readFile(String file) throws IOException {
	Path path = Paths.get(file);
	if (!exists(file)) {
	    // Using console output as problem statement says not to use third
	    // party Library
	    System.out.println("File Does not Exist");
	    throw new IOException("File Not Found");
	}

	List<String> dataPointList = new ArrayList<String>();
	try (Stream<String> lines = Files.lines(path)) {
	    // Read All data in list
	    lines.forEach(dataPointList::add);
	}

	return dataPointList;

    }
}
