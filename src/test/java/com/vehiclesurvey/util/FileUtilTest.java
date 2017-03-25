package com.vehiclesurvey.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class FileUtilTest {

    @Test
    public void testReadFileExists() {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/vehicleHoseDataPoints.txt");
	Assert.assertTrue(FileReaderUtil.exists(p.toString()));
    }

    @Test
    public void testReadFileSampleDate() throws IOException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/vehicleHoseDataPoints.txt");
	List<String> dataPoint = FileReaderUtil.readFile(p.toString());
	assertEquals(8, dataPoint.size());

    }

    @Test(expected = IOException.class)
    public void testReadFileNotExistsSampleDate() throws IOException {

	List<String> dataPoint = FileReaderUtil.readFile("/src/test/resource/vehicleHoseDataPoints.txt");

    }

}
