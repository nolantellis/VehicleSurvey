package com.vehiclesurvey;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.vehiclesurvey.exception.InvalidHoseDataPointException;
import com.vehiclesurvey.model.Direction;
import com.vehiclesurvey.model.HoseDataPoint;
import com.vehiclesurvey.model.VehicleStats;
import com.vehiclesurvey.util.FileReaderUtil;
import com.vehiclesurvey.util.VehicleStatsBuilder;

public class VehicleStatBuilderTest {

    VehicleStatsBuilder builder;

    @Before
    public void setUp() {
	builder = new VehicleStatsBuilder();
    }

    @Test
    public void testSimpleVehicleStatBuilder() throws InvalidHoseDataPointException {

	List<HoseDataPoint> hosePoints = new ArrayList<>();
	hosePoints.add(new HoseDataPoint("A98186"));
	hosePoints.add(new HoseDataPoint("A98333"));
	hosePoints.add(new HoseDataPoint("A499718"));
	hosePoints.add(new HoseDataPoint("A499886"));
	hosePoints.forEach(builder::accept);
	assertEquals(2, builder.getVehicleStats().size());
	assertEquals(0, builder.getHoseDataPoints().size());

    }

    @Test
    public void testVehicleStatBuilderMoreVehicles() throws InvalidHoseDataPointException {

	List<HoseDataPoint> hosePoints = new ArrayList<>();
	hosePoints.add(new HoseDataPoint("A7522142"));
	hosePoints.add(new HoseDataPoint("B7522145"));
	hosePoints.add(new HoseDataPoint("A7522265"));
	hosePoints.add(new HoseDataPoint("B7522267"));
	hosePoints.add(new HoseDataPoint("A8368330"));
	hosePoints.add(new HoseDataPoint("A8368477"));
	hosePoints.add(new HoseDataPoint("A8906253"));
	hosePoints.add(new HoseDataPoint("B8906256"));
	hosePoints.add(new HoseDataPoint("A8906397"));
	hosePoints.add(new HoseDataPoint("B8906400"));

	hosePoints.forEach(builder::accept);

	assertEquals(3, builder.getVehicleStats().size());

    }

    @Test
    public void testSingleVehicleAverageSpeedAndTime() throws InvalidHoseDataPointException {

	List<HoseDataPoint> hosePoints = new ArrayList<>();
	hosePoints.add(new HoseDataPoint("A98186"));
	hosePoints.add(new HoseDataPoint("A98333"));
	hosePoints.forEach(builder::accept);
	assertEquals(1, builder.getVehicleStats().size());
	VehicleStats s = builder.getVehicleStats().get(0);
	assertEquals(1, s.getAverageTime().getMinute());
	assertEquals(38, s.getAverageTime().getSecond());
	assertEquals(s.getDirection(), Direction.NORTH);
	assertEquals(61, s.getAverageSpeed());

    }

    @Test
    public void testSingleVehicleAverageSpeed() throws InvalidHoseDataPointException {

	List<HoseDataPoint> hosePoints = new ArrayList<>();
	hosePoints.add(new HoseDataPoint("A156007"));
	hosePoints.add(new HoseDataPoint("B156011"));
	hosePoints.add(new HoseDataPoint("A156220"));
	hosePoints.add(new HoseDataPoint("B156224"));
	hosePoints.forEach(builder::accept);
	assertEquals(1, builder.getVehicleStats().size());
	VehicleStats s = builder.getVehicleStats().get(0);
	assertEquals(2, s.getAverageTime().getMinute());
	assertEquals(36, s.getAverageTime().getSecond());
	assertEquals(s.getDirection(), Direction.SOUTH);
	assertEquals(42, s.getAverageSpeed());

    }

    @Test
    public void testSingleVehicleAverageSpeedAndTime1() throws InvalidHoseDataPointException {

	List<HoseDataPoint> hosePoints = new ArrayList<>();
	hosePoints.add(new HoseDataPoint("A7522142"));
	hosePoints.add(new HoseDataPoint("B7522145"));
	hosePoints.add(new HoseDataPoint("A7522265"));
	hosePoints.add(new HoseDataPoint("B7522267"));
	hosePoints.forEach(builder::accept);
	assertEquals(1, builder.getVehicleStats().size());
	VehicleStats s = builder.getVehicleStats().get(0);
	assertEquals(5, s.getAverageTime().getMinute());
	assertEquals(22, s.getAverageTime().getSecond());
	assertEquals(s.getDirection(), Direction.SOUTH);
	assertEquals(73, s.getAverageSpeed());

    }

    @Test
    public void test2DaysVehicleStats() throws InvalidHoseDataPointException {

	List<HoseDataPoint> hosePoints = new ArrayList<>();
	hosePoints.add(new HoseDataPoint("A86335139"));
	hosePoints.add(new HoseDataPoint("A86335248"));
	hosePoints.add(new HoseDataPoint("A86351522"));
	hosePoints.add(new HoseDataPoint("B86351525"));
	hosePoints.add(new HoseDataPoint("A86351669"));
	hosePoints.add(new HoseDataPoint("B86351672"));
	hosePoints.add(new HoseDataPoint("A156007"));
	hosePoints.add(new HoseDataPoint("B156011"));
	hosePoints.add(new HoseDataPoint("A156220"));
	hosePoints.add(new HoseDataPoint("B156224"));
	hosePoints.add(new HoseDataPoint("A457868"));
	hosePoints.add(new HoseDataPoint("A457996"));
	for(HoseDataPoint p: hosePoints)
	{
	builder.accept(p);
	}
	assertEquals(4, builder.getVehicleStats().size());
	assertEquals(2, builder.getDayList().size());

	VehicleStats s = builder.getVehicleStats().get(0);

	assertEquals(s.getDirection(), Direction.NORTH);
	assertEquals("Day1", s.getDay());

	assertEquals(builder.getVehicleStats().get(2).getDirection(), Direction.SOUTH);
	assertEquals("Day2", builder.getVehicleStats().get(2).getDay());

	assertEquals(82, s.getAverageSpeed());
	assertEquals(42, builder.getVehicleStats().get(2).getAverageSpeed());

    }
    
    @Test
    public void testVehicleStatsFromFile() throws IOException, InvalidHoseDataPointException
    {
	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/vehicleHoseDataPoints.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());
	assertEquals(8, dataPoints.size());
	List<VehicleStats> vehicleStats=builder.getVehicleStatsFromDataPoints(dataPoints);
	assertEquals(3, vehicleStats.size());
	assertEquals(1, vehicleStats.get(0).getTotalNumberOfDays());

    }
    
    @Test
    public void testVehicleStatsFromFile1() throws IOException, InvalidHoseDataPointException
    {
	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/VehicleSurveyLastSample.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());
	assertEquals(16, dataPoints.size());
	List<VehicleStats> vehicleStats=builder.getVehicleStatsFromDataPoints(dataPoints);
	assertEquals(5, vehicleStats.size());
	assertEquals(1, vehicleStats.get(0).getTotalNumberOfDays());
	

    }
    
    @Test
    public void testVehicleStatsFromFile2Days() throws IOException, InvalidHoseDataPointException
    {
	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/VehicleSurveySample2Days");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());
	assertEquals(26, dataPoints.size());
	List<VehicleStats> vehicleStats=builder.getVehicleStatsFromDataPoints(dataPoints);
	assertEquals(8, vehicleStats.size());
	assertEquals(2, vehicleStats.get(0).getTotalNumberOfDays());
    }
    
    @Test
    public void testVehicleStatsFromBigFile() throws IOException, InvalidHoseDataPointException
    {
	Path p = Paths.get(System.getProperty("user.dir"), "/src/test/resource/VehicleSurveySample.txt");
	List<String> dataPoints = FileReaderUtil.readFile(p.toString());
	List<VehicleStats> vehicleStats=builder.getVehicleStatsFromDataPoints(dataPoints);	
	assertEquals(5, vehicleStats.get(0).getTotalNumberOfDays());
	

    }
}
