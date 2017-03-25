package com.vehiclesurvey;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.vehiclesurvey.exception.InvalidHoseDataPointException;
import com.vehiclesurvey.model.Direction;
import com.vehiclesurvey.model.HoseDataPoint;
import com.vehiclesurvey.model.VehicleStats;

public class VehicleStatTest {

    @Test
    public void testVehicleStatGeneration2() throws InvalidHoseDataPointException {

	List<HoseDataPoint> dpList = new ArrayList<>();
	dpList.add(new HoseDataPoint("A499718"));
	dpList.add(new HoseDataPoint("A499886"));
	VehicleStats s = new VehicleStats("Day1", Direction.SOUTH, dpList);
	assertEquals(0, s.getAverageTime().getHour());
	assertEquals(8, s.getAverageTime().getMinute());
	assertEquals(2, s.getHoseDataPoints().size());
	assertEquals(19, s.getAverageTime().getSecond());
	assertEquals(s.getDirection(), Direction.SOUTH);
	assertEquals(53, s.getAverageSpeed());
	assertEquals("AM", s.getAMPMInstant());

    }

    @Test
    public void testVehicleStatGeneration7() throws InvalidHoseDataPointException {

	List<HoseDataPoint> dpList = new ArrayList<>();
	dpList.add(new HoseDataPoint("A86335139"));
	dpList.add(new HoseDataPoint("A86335248"));
	VehicleStats s = new VehicleStats("Day1", Direction.SOUTH, dpList);
	assertEquals(23, s.getAverageTime().getHour());
	assertEquals(58, s.getAverageTime().getMinute());
	assertEquals(2, s.getHoseDataPoints().size());
	assertEquals(55, s.getAverageTime().getSecond());
	assertEquals(s.getDirection(), Direction.SOUTH);
	assertEquals(82, s.getAverageSpeed());
	assertEquals("PM", s.getAMPMInstant());

    }

    @Test
    public void testVehicleStatGeneration3() throws InvalidHoseDataPointException {

	List<HoseDataPoint> dpList = new ArrayList<>();
	dpList.add(new HoseDataPoint("A86381097"));
	dpList.add(new HoseDataPoint("B86381100"));
	dpList.add(new HoseDataPoint("A86381258"));
	dpList.add(new HoseDataPoint("B86381261"));
	VehicleStats s = new VehicleStats("Day1", Direction.NORTH, dpList);
	assertEquals(4, s.getHoseDataPoints().size());
	assertEquals(59, s.getAverageTime().getMinute());
	assertEquals(41, s.getAverageTime().getSecond());
	assertEquals(s.getDirection(), Direction.NORTH);
	assertEquals(55, s.getAverageSpeed());
	assertEquals("PM", s.getAMPMInstant());
    }

    @Test
    public void testVehicleStatGeneration4() throws InvalidHoseDataPointException {

	List<HoseDataPoint> dpList = new ArrayList<>();
	dpList.add(new HoseDataPoint("A2422884"));
	dpList.add(new HoseDataPoint("B2422887"));
	dpList.add(new HoseDataPoint("A2423013"));
	dpList.add(new HoseDataPoint("B2423016"));
	VehicleStats s = new VehicleStats("Day1", Direction.NORTH, dpList);
	assertEquals(0, s.getAverageTime().getHour());
	assertEquals(40, s.getAverageTime().getMinute());
	assertEquals(22, s.getAverageTime().getSecond());
	assertEquals(s.getDirection(), Direction.NORTH);
	assertEquals(69, s.getAverageSpeed());
	assertEquals("AM", s.getAMPMInstant());

    }

    @Test
    public void testVehicleStatGeneration5() throws InvalidHoseDataPointException {

	List<HoseDataPoint> dpList = new ArrayList<>();
	dpList.add(new HoseDataPoint("A7522142"));
	dpList.add(new HoseDataPoint("B7522145"));
	dpList.add(new HoseDataPoint("A7522265"));
	dpList.add(new HoseDataPoint("B7522267"));
	VehicleStats s = new VehicleStats("Day1", Direction.NORTH, dpList);
	assertEquals(2, s.getAverageTime().getHour());
	assertEquals(5, s.getAverageTime().getMinute());
	assertEquals(22, s.getAverageTime().getSecond());
	assertEquals(s.getDirection(), Direction.NORTH);
	assertEquals(73, s.getAverageSpeed());
	assertEquals("AM", s.getAMPMInstant());

    }

    @Test
    public void testVehicleStatGeneration6() throws InvalidHoseDataPointException {

	List<HoseDataPoint> dpList = new ArrayList<>();
	dpList.add(new HoseDataPoint("A14547248"));
	dpList.add(new HoseDataPoint("B14547250"));
	dpList.add(new HoseDataPoint("A14547377"));
	dpList.add(new HoseDataPoint("B14547380"));
	VehicleStats s = new VehicleStats("Day1", Direction.NORTH, dpList);
	assertEquals(4, s.getAverageTime().getHour());
	assertEquals(2, s.getAverageTime().getMinute());
	assertEquals(27, s.getAverageTime().getSecond());
	assertEquals(s.getDirection(), Direction.NORTH);
	assertEquals(69, s.getAverageSpeed());
	assertEquals("AM", s.getAMPMInstant());

    }

    @Test
    public void testVehicleStatGeneration() throws InvalidHoseDataPointException {

	List<HoseDataPoint> dpList = new ArrayList<>();
	dpList.add(new HoseDataPoint("A98186"));
	dpList.add(new HoseDataPoint("A98333"));
	VehicleStats s = new VehicleStats("Day1", Direction.NORTH, dpList);
	assertEquals(2, s.getHoseDataPoints().size());
	assertEquals(01, s.getAverageTime().getMinute());
	assertEquals(38, s.getAverageTime().getSecond());
	assertEquals(s.getDirection(), Direction.NORTH);
	assertEquals(61, s.getAverageSpeed());
	assertEquals("AM", s.getAMPMInstant());

    }

}
