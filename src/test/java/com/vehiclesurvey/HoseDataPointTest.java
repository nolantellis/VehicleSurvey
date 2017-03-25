package com.vehiclesurvey;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

import com.vehiclesurvey.exception.InvalidHoseDataPointException;
import com.vehiclesurvey.model.HoseDataPoint;
import com.vehiclesurvey.model.HoseTag;

public class HoseDataPointTest {

    @Test
    public void testValidHoseDataPointA() throws InvalidHoseDataPointException {
	HoseDataPoint hoseDataPoint = new HoseDataPoint("A268981");

	assertEquals(HoseTag.A, hoseDataPoint.getHoseTag());
	assertEquals(LocalTime.of(00, 04, 28).getMinute(), hoseDataPoint.getInstantTime().getMinute());
	assertEquals(LocalTime.of(00, 04, 28).getSecond(), hoseDataPoint.getInstantTime().getSecond());
    }

    @Test
    public void testValidHoseDataPointB() throws InvalidHoseDataPointException {
	HoseDataPoint hoseDataPoint = new HoseDataPoint("B268981");

	assertEquals(HoseTag.B, hoseDataPoint.getHoseTag());
	assertEquals(LocalTime.of(00, 04, 28).getMinute(), hoseDataPoint.getInstantTime().getMinute());
	assertEquals(LocalTime.of(00, 04, 28).getSecond(), hoseDataPoint.getInstantTime().getSecond());
    }

    @Test
    public void testValidHoseDataPointTime() throws InvalidHoseDataPointException {
	HoseDataPoint hoseDataPoint = new HoseDataPoint("A1089807");

	assertEquals(HoseTag.A, hoseDataPoint.getHoseTag());
	assertEquals(LocalTime.of(00, 18, 9).getMinute(), hoseDataPoint.getInstantTime().getMinute());
	assertEquals(LocalTime.of(00, 18, 9).getSecond(), hoseDataPoint.getInstantTime().getSecond());
    }

    @Test(expected = InvalidHoseDataPointException.class)
    public void testInValidHoseDataPoint() throws InvalidHoseDataPointException {
	HoseDataPoint hoseDataPoint = new HoseDataPoint("AA268981");

    }

    @Test(expected = InvalidHoseDataPointException.class)
    public void testNullHoseDataPoint() throws InvalidHoseDataPointException {
	HoseDataPoint hoseDataPoint = new HoseDataPoint(null);
    }

}
