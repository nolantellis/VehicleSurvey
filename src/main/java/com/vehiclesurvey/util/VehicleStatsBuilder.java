package com.vehiclesurvey.util;

import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.vehiclesurvey.exception.InvalidHoseDataPointException;
import com.vehiclesurvey.model.Direction;
import com.vehiclesurvey.model.HoseDataPoint;
import com.vehiclesurvey.model.HoseTag;
import com.vehiclesurvey.model.VehicleStats;

/**
 * This Class is used to build the list of {@link VehicleStats} used for analysis.
 * @author Nolan.Tellis
 *
 */
public class VehicleStatsBuilder {

    private enum STATE {

	A, B

    };

    private  STATE state = STATE.A;
    private  int dayCounter = 0;
    private  String day = "Day";
    private static LocalTime lastLocalTime = LocalTime.NOON;

    private List<VehicleStats> vehicleStats = new ArrayList<>();
    private List<HoseDataPoint> hoseDataPoints = new ArrayList<>();
    private  List<String> dayList = new ArrayList<>();

    public List<VehicleStats> getVehicleStats() {
	return vehicleStats;
    }

    /**
     * Temp storage for list of hosepoints.
     * This can be used to skip or continue to next hosepoints if any data is corrupt in the file.
     * @return List of {@link HoseDataPoint} Upto the parsed hosepoints for a vehicle.
     * Max value can be 4.
     */
    public List<HoseDataPoint> getHoseDataPoints() {
	return hoseDataPoints;
    }

    /** 
     * 
     * @return Returns list of all days uptill the {@link HoseDataPoint} used to build the {@link VehicleStats}
     */
    public  List<String> getDayList() {
	return dayList;
    }

    /**
     * This method is used to accept hospoints and build vehicle stats.
     * @param hoseDataPoint HostDatapoints Example A1234
     * 
     */
    public void accept(HoseDataPoint hoseDataPoint) {
	if (dayCounter == 0 || isNextDayHoseDataPoint(hoseDataPoint)) {
	    day = getNewDay();
	    dayList.add(day);
	}
	lastLocalTime = hoseDataPoint.getInstantTime();
	hoseDataPoints.add(hoseDataPoint);
	switch (state) {
	case A:
	    if (hoseDataPoint.getHoseTag() == HoseTag.A && hoseDataPoints.size() == 2) {
		VehicleStats vehicleStat = new VehicleStats(day, Direction.NORTH,
			new ArrayList<HoseDataPoint>(hoseDataPoints));
		vehicleStat.setBuilderRef(this);
		vehicleStats.add(vehicleStat);
		hoseDataPoints.clear();
		;
		state = STATE.A;
	    } else if (hoseDataPoint.getHoseTag() == HoseTag.B) {
		state = STATE.B;
	    }

	    break;

	case B:
	    if (hoseDataPoint.getHoseTag() == HoseTag.B && hoseDataPoints.size() == 4) {
		VehicleStats vehicleStat = new VehicleStats(day, Direction.SOUTH,
			new ArrayList<HoseDataPoint>(hoseDataPoints));
		vehicleStat.setBuilderRef(this);
		vehicleStats.add(vehicleStat);
		hoseDataPoints.clear();
		state = STATE.A;
	    }
	    break;
	}
    }

    private boolean isNextDayHoseDataPoint(HoseDataPoint hoseDataPoint) {
	return hoseDataPoints.isEmpty() && hoseDataPoint.getInstantTime().isBefore(lastLocalTime);
    }

    private String getNewDay() {
	return "Day" + (++dayCounter);
    }

    public  List<VehicleStats> getVehicleStatsFromDataPoints(List<String> dataPoints)
	    throws InvalidHoseDataPointException {
	ListIterator<String> dataPointIterator = dataPoints.listIterator();
	while (dataPointIterator.hasNext()) {
	    String data = dataPointIterator.next();
	    accept(new HoseDataPoint(data));
	}
	
	return getVehicleStats();

    }

}
