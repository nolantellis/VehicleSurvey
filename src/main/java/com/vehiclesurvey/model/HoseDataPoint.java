package com.vehiclesurvey.model;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import com.vehiclesurvey.exception.InvalidHoseDataPointException;

/**
 * This Class is a Pojo that holds data points for a vehicle which has passed a
 * hosepoint on the road.
 * 
 * @author Nolan.Tellis
 *
 */
public class HoseDataPoint {
    private static String hosePointExpression = "[AB]\\d+";
    private HoseTag hoseTag;
    private LocalTime instantTime = LocalTime.MIDNIGHT;

    /**
     * This Constructor is used to generate a hosepoint from a String
     * 
     * Example :
     * <ul>
     * <li>A123455</li>
     * </ul>
     * 
     * 
     * @param hosePoint A String hosepoint. Example : B1233444
     * @throws InvalidHoseDataPointException Exception is thrown if it is unable to parse the hosepoint using expression [AB]\d*
     */
    public HoseDataPoint(String hosePoint) throws InvalidHoseDataPointException {
	if (Objects.isNull(hosePoint) || !hosePoint.matches(hosePointExpression)) {
	    throw new InvalidHoseDataPointException("InValid HosePoint");
	}
	hoseTag = HoseTag.valueOf(hosePoint.substring(0, 1));
	this.instantTime = this.instantTime.plus(Long.valueOf(hosePoint.substring(1)), ChronoUnit.MILLIS);
    }

    /**
     * 
     * @return {@link HoseTag} Example :  In Data point A1234 the value 'A' is a HoseTag. It is used to detect the direction of a vehicle
     */
    public HoseTag getHoseTag() {
	return hoseTag;
    }

    /**
     * 
     * @return {@link LocalTime} Its the time at which a vehicle passed over a given {@link HoseDataPoint}
     */
    public LocalTime getInstantTime() {
	return instantTime;
    }

}
