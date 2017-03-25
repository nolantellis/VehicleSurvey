package com.vehiclesurvey.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.vehiclesurvey.util.VehicleStatsBuilder;

/**
 * This is the main class which is built based on Hosepoints.
 * An Instance of this class represents a Vehicle moving in a perticular direction in a perticular time
 * and contains the average speed and average time period.
 * @author Nolan.Tellis
 *
 */
public class VehicleStats {

    private static final int FRONT_AXLE_B = 1;
    private static final int BACK_AXLE_B = 3;
    private static final int FRONT_AXLE_A = 0;
    private static final int BACK_AXLE_A = 1;
    private static int totalNumberOfDays = 0;
    private VehicleStatsBuilder builderRef;
    static float averageAxleLength = 2.5f;
    static int kmPerHourFactor = 3600;
    private String day;
    private Direction direction;
    private int averageSpeed;
    private LocalTime averageTime;
    private List<HoseDataPoint> hoseDataPoints;
/**
 * 
 * @param day String that represents the day when the vehicle passed over the Strip. Example : Day1,Day2 etc
 * @param direction {@link Direction}
 * @param hoseDataPoints {@link HoseDataPoint}
 */
    public VehicleStats(String day, Direction direction, List<HoseDataPoint> hoseDataPoints) {
	this.day = day;
	this.direction = direction;
	this.hoseDataPoints = hoseDataPoints;
	this.averageTime = getCalculatedAverageTime();
	this.averageSpeed = getCalculatedAverageSpeed();

    }

    /**
     * 
     * @param builderRef A reference to the builder tat was used to build this VehicleStat Object. It contains meta information
     * Example
     * <ul>
     * <li>Total Number of Days</li>
     * <li>Total Number of Vehicles</li>
     * <li>List of VehicleStats that was generated</li>
     * </ul>
     */
    public void setBuilderRef(VehicleStatsBuilder builderRef) {
	this.builderRef = builderRef;
    }
/**
 * 
 * @return day when the vehicle passed on the road.
 */
    public String getDay() {
	return day;
    }
/**
 * 
 * @return {@link Direction} The Direction of Vehicle on the Road
 */
    public Direction getDirection() {
	return direction;
    }

    /**
     * 
     * @return average Speed of the Vehicle
     */
    public int getAverageSpeed() {
	return averageSpeed;
    }

    /**
     * 
     * @return averae time of the vehicle when it passed the HosePoints
     */
    public LocalTime getAverageTime() {
	return averageTime;
    }

    public List<HoseDataPoint> getHoseDataPoints() {
	return hoseDataPoints;
    }

    private LocalTime getCalculatedAverageTime() {

	Iterator<HoseDataPoint> timeIterator = hoseDataPoints.iterator();
	Duration d = Duration.ZERO;
	while (timeIterator.hasNext()) {
	    LocalTime t1 = timeIterator.next().getInstantTime();
	    LocalTime t2 = timeIterator.next().getInstantTime();
	    d = d.plus(Duration.between(t1, t2));
	}
	d = d.dividedBy(hoseDataPoints.size());
	return hoseDataPoints.get(0).getInstantTime().plus(d);
    }

    private int getCalculatedAverageSpeed() {

	long timeDifference = Long.MIN_VALUE;
	Duration duration = Duration.ZERO;
	if (hoseDataPoints.size() == 2) {
	    // Assuming average time by taking second axle
	    duration = Duration.between(hoseDataPoints.get(FRONT_AXLE_A).getInstantTime(),
		    hoseDataPoints.get(BACK_AXLE_A).getInstantTime());

	} else {
	    // Assuming average time by taking second axle for strip B
	    duration = Duration.between(hoseDataPoints.get(FRONT_AXLE_B).getInstantTime(),
		    hoseDataPoints.get(BACK_AXLE_B).getInstantTime());

	}
	timeDifference = duration.getNano() / 1000000;
	return (int) ((averageAxleLength / timeDifference) * kmPerHourFactor);
    }

    /**
     * 
     * @return AM/PM for time which the vehicle passed over the road.
     */
    public String getAMPMInstant() {
	return this.averageTime.format(DateTimeFormatter.ofPattern("a"));
    }

    public int getTotalNumberOfDays() {
	return builderRef.getDayList().size();
    }

}
