package com.vehiclesurvey;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.vehiclesurvey.exception.InvalidHoseDataPointException;
import com.vehiclesurvey.model.Direction;
import com.vehiclesurvey.model.VehicleStats;
import com.vehiclesurvey.report.VehicleStatsReport;
import com.vehiclesurvey.util.FileReaderUtil;
import com.vehiclesurvey.util.VehicleStatsBuilder;

public class VehicleStatsReportTest {

    VehicleStatsBuilder builder;

    @Before
    public void setUp() {
	builder = new VehicleStatsBuilder();

    }

    @Test
    public void testGetDayWiseReportFor2DaysStats() throws IOException, InvalidHoseDataPointException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/VehicleSurveySample2Days");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport reprt = new VehicleStatsReport(list);
	Map<String, ?> resultMap=reprt.getVehicleCountByMorningEveningForDirection(Direction.NORTH, false);
	assertEquals(Long.valueOf(1), ((Map)resultMap.get("PM")).get("Day1"));
	assertEquals(Long.valueOf(2), ((Map)resultMap.get("AM")).get("Day2"));
	
	
    }

    @Test
    public void testOverAllWiseReportFor2DaysStats() throws IOException, InvalidHoseDataPointException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/VehicleSurveySample2Days");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport reprt = new VehicleStatsReport(list);
	Map<String, ?> resultMap=reprt.getVehicleCountByMorningEveningForDirection(Direction.NORTH, true);
	assertEquals(Long.valueOf(1), resultMap.get("AM"));
	assertEquals(Long.valueOf(0), resultMap.get("PM"));

    }
    
    @Test
    public void testGetDayWiseReportForMidStats() throws IOException, InvalidHoseDataPointException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/VehicleReportSampleMidSize.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport reprt = new VehicleStatsReport(list);
	Map<String, ?> resultMap=reprt.getVehicleCountByMorningEveningForDirection(Direction.SOUTH, false);
	assertEquals(Long.valueOf(28), ((Map)resultMap.get("AM")).get("Day1"));

	// fail("Not yet implemented");
    }

    @Test
    public void testOverAllAMPMWiseReportForMidStats() throws IOException, InvalidHoseDataPointException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/VehicleReportSampleMidSize.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport reprt = new VehicleStatsReport(list);
	Map<String, ?> resultMap=reprt.getVehicleCountByMorningEveningForDirection(Direction.SOUTH, true);
	assertEquals(Long.valueOf(28), resultMap.get("AM"));


    }
    
    @Test(expected=InvalidHoseDataPointException.class)
    public void testInvalidHosePoint() throws IOException, InvalidHoseDataPointException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/VehicleReportSampleInvalidHose.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

    }

    
    @Test
    public void testGetHourWiseReportForMidStats() throws IOException, InvalidHoseDataPointException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/VehicleReportSampleMidSize.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport reprt = new VehicleStatsReport(list);
	Map<String, ?> resultMap=reprt.getVehicleCountByHourForDirection(Direction.SOUTH, false);
	
	assertEquals(Long.valueOf(7), ((Map)resultMap.get("Count of Vehicle at the hour 3")).get("Day1"));
	assertEquals(Long.valueOf(6), ((Map)resultMap.get("Count of Vehicle at the hour 2")).get("Day1"));
	assertEquals(Long.valueOf(7), ((Map)resultMap.get("Count of Vehicle at the hour 1")).get("Day1"));
	assertEquals(Long.valueOf(8), ((Map)resultMap.get("Count of Vehicle at the hour 0")).get("Day1"));

	// fail("Not yet implemented");
    }

    @Test
    public void testOverAllHourWiseReportForMidStats() throws IOException, InvalidHoseDataPointException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/VehicleReportSampleMidSize.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport reprt = new VehicleStatsReport(list);
	Map<String, ?> resultMap=reprt.getVehicleCountByHourForDirection(Direction.SOUTH, true);
	assertEquals(Long.valueOf(7), resultMap.get("Count of Vehicle at the hour 3"));
	assertEquals(Long.valueOf(6), resultMap.get("Count of Vehicle at the hour 2"));
	assertEquals(Long.valueOf(7), resultMap.get("Count of Vehicle at the hour 1"));
	assertEquals(Long.valueOf(8), resultMap.get("Count of Vehicle at the hour 0"));


    }

    
    @Test
    public void testGetDayWiseReportForBigStats() throws IOException, InvalidHoseDataPointException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/VehicleSurveySample.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport reprt = new VehicleStatsReport(list);
	Map<String, ?> resultMap=reprt.getVehicleCountByMorningEveningForDirection(Direction.SOUTH, false);
	
	assertEquals(Long.valueOf(1054), ((Map)resultMap.get("AM")).get("Day1"));
	assertEquals(Long.valueOf(1141), ((Map)resultMap.get("AM")).get("Day2"));
	assertEquals(Long.valueOf(1105), ((Map)resultMap.get("AM")).get("Day3"));
	assertEquals(Long.valueOf(1118), ((Map)resultMap.get("AM")).get("Day4"));
	assertEquals(Long.valueOf(1086), ((Map)resultMap.get("AM")).get("Day5"));
	
	assertEquals(Long.valueOf(1170), ((Map)resultMap.get("PM")).get("Day1"));
	assertEquals(Long.valueOf(1192), ((Map)resultMap.get("PM")).get("Day2"));
	assertEquals(Long.valueOf(1115), ((Map)resultMap.get("PM")).get("Day3"));
	assertEquals(Long.valueOf(1123), ((Map)resultMap.get("PM")).get("Day4"));
	assertEquals(Long.valueOf(1172), ((Map)resultMap.get("PM")).get("Day5"));

	// fail("Not yet implemented");
    }

    @Test
    public void testOverAllWiseReportForBigStats() throws IOException, InvalidHoseDataPointException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/VehicleSurveySample.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport reprt = new VehicleStatsReport(list);
	Map<String, ?> resultMap=reprt.getVehicleCountByMorningEveningForDirection(Direction.SOUTH, true);
	assertEquals(Long.valueOf(1100), resultMap.get("AM"));
	assertEquals(Long.valueOf(1154), resultMap.get("PM"));


    }



}
