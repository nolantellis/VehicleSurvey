package com.vehiclesurvey;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.vehiclesurvey.exception.InValidTimeIntervalException;
import com.vehiclesurvey.exception.InvalidHoseDataPointException;
import com.vehiclesurvey.model.Direction;
import com.vehiclesurvey.model.VehicleStats;
import com.vehiclesurvey.report.VehicleStatsReport;
import com.vehiclesurvey.util.FileReaderUtil;
import com.vehiclesurvey.util.VehicleStatsBuilder;

public class VehicleStatReportByTimeTest {

    VehicleStatsBuilder builder;

    @Before
    public void setUp() {
	builder = new VehicleStatsBuilder();

    }

    @Test
    public void testSmallSampleTimeReport()
	    throws InvalidHoseDataPointException, IOException, InValidTimeIntervalException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/VehicleSurveySample2Days");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport reprt = new VehicleStatsReport(list);
	Map<String, ?> resultMap = reprt.getVehicleCountByMinuteDivisionForDirection(Direction.NORTH, 20, false);

	assertEquals(Long.valueOf(1),
		((Map) resultMap.get("Count of Vehicles between time range of 40 and 60 min")).get("Day1"));
	assertEquals(Long.valueOf(1),
		((Map) resultMap.get("Count of Vehicles between time range of 20 and 40 min")).get("Day2"));
	assertEquals(Long.valueOf(1),
		((Map) resultMap.get("Count of Vehicles between time range of 0 and 20 min")).get("Day2"));

    }

    @Test
    public void testSmallSampleTimeReportAverage()
	    throws InvalidHoseDataPointException, IOException, InValidTimeIntervalException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/VehicleSurveySample2Days");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport reprt = new VehicleStatsReport(list);
	Map<String, ?> resultMap = reprt.getVehicleCountByMinuteDivisionForDirection(Direction.NORTH, 20, true);
	assertEquals(Long.valueOf(0), (resultMap.get("Count of Vehicles between time range of 40 and 60 min")));
	assertEquals(Long.valueOf(0), (resultMap.get("Count of Vehicles between time range of 40 and 60 min")));
	assertEquals(Long.valueOf(0), (resultMap.get("Count of Vehicles between time range of 40 and 60 min")));

    }

    @Test(expected = InValidTimeIntervalException.class)
    public void testSmallSampleTimeReportAverageException()
	    throws InvalidHoseDataPointException, IOException, InValidTimeIntervalException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/VehicleSurveySample2Days");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport reprt = new VehicleStatsReport(list);
	Map<String, ?> resultMap = reprt.getVehicleCountByMinuteDivisionForDirection(Direction.NORTH, -20, true);

    }

    @Test
    public void testMidSampleTimeReportAverage()
	    throws InvalidHoseDataPointException, IOException, InValidTimeIntervalException {

	Path p = Paths.get(System.getProperty("user.dir"),
		"/src/test/resource/reportSample/VehicleReportSampleMidSize.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport reprt = new VehicleStatsReport(list);

	Map<String, ?> resultMap = reprt.getVehicleCountByMinuteDivisionForDirection(Direction.NORTH, 30, false);
	assertEquals(Long.valueOf(5),
		((Map) resultMap.get("Count of Vehicles between time range of 0 and 30 min")).get("Day1"));
	assertEquals(Long.valueOf(8),
		((Map) resultMap.get("Count of Vehicles between time range of 30 and 60 min")).get("Day1"));

    }

    @Test
    public void testPeakVolume() throws InvalidHoseDataPointException, IOException, InValidTimeIntervalException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/VehicleSurveySample.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport report = new VehicleStatsReport(list);

	List<Map.Entry<String, Long>> data = report.getPeakVehicleTimeByHoursOverAverageDays();

	assertEquals(Long.valueOf(647), data.get(0).getValue());

    }

    @Test
    public void testPeakVolume2() throws InvalidHoseDataPointException, IOException, InValidTimeIntervalException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/VehicleSurveySample2Days");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport report = new VehicleStatsReport(list);

	List<Map.Entry<String, Long>> data = report.getPeakVehicleTimeByHoursOverAverageDays();

	assertEquals(Long.valueOf(2), data.get(0).getValue());

    }

    @Test
    public void testSpeedDistributionBigData()
	    throws InvalidHoseDataPointException, IOException, InValidTimeIntervalException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/VehicleSurveySample.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport report = new VehicleStatsReport(list);

	Map<String, Long> data = report.getAveragedSpeedDistribution();
	assertEquals(Long.valueOf(117), data.get("Count of Vehicle at 73 Km/Hr"));

    }

    @Test
    public void testSpeedDistribution2()
	    throws InvalidHoseDataPointException, IOException, InValidTimeIntervalException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/VehicleReportSampleMidSize.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport report = new VehicleStatsReport(list);

	Map<String, Long> data = report.getAveragedSpeedDistribution();
	
	assertEquals(Long.valueOf(2), data.get("Count of Vehicle at 73 Km/Hr"));
	assertEquals(Long.valueOf(1), data.get("Count of Vehicle at 69 Km/Hr"));
	assertEquals(Long.valueOf(1), data.get("Count of Vehicle at 55 Km/Hr"));
	

    }
    
    @Test
    public void testRandomDistanceTest()
	    throws InvalidHoseDataPointException, IOException, InValidTimeIntervalException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/RoughDistanceSample.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport report = new VehicleStatsReport(list);

	String data = report.getRoughDistanceBetweenVehicleBetweenPeriod(Direction.NORTH,LocalTime.MIN, LocalTime.MAX);
	
	assertEquals("294.5 meters",data);

    }

    @Test
    public void testRandomDistanceTestSameTime()
	    throws InvalidHoseDataPointException, IOException, InValidTimeIntervalException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/reportSample/RoughDistanceSample.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport report = new VehicleStatsReport(list);

	String data = report.getRoughDistanceBetweenVehicleBetweenPeriod(Direction.NORTH,LocalTime.MIDNIGHT, LocalTime.MIDNIGHT);
	
	assertEquals("0 meters",data);

    }

    
    @Test
    public void testRandomDistanceTestBigData()
	    throws InvalidHoseDataPointException, IOException, InValidTimeIntervalException {

	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/VehicleSurveySample.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());

	List<VehicleStats> list = builder.getVehicleStatsFromDataPoints(dataPoints);

	VehicleStatsReport report = new VehicleStatsReport(list);

	String data = report.getRoughDistanceBetweenVehicleBetweenPeriod(Direction.NORTH,LocalTime.of(8,0), LocalTime.of(8,30));
	
	assertEquals("3486.7056451612902 meters",data); // nonauthentic test result

    }

}
