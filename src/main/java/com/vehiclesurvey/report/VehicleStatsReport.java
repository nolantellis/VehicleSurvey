package com.vehiclesurvey.report;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalDouble;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vehiclesurvey.exception.InValidTimeIntervalException;
import com.vehiclesurvey.model.Direction;
import com.vehiclesurvey.model.VehicleStats;

/**
 * This is the Class which is used to Generate the report. 
 * The Return types are general Maps or List which a User can use to control display of outputs.
 * @author Nolan.Tellis
 *
 */
public class VehicleStatsReport {

    private static final int SECONDS_OF_DAY = 24 * 3600;
    private static final int METER_FACTOR = 1000;
    private List<VehicleStats> vehicleStats;

    /**
     * 
     * @param vehicleStats This is a list of {@link VehicleStats} which is used for reporting.
     */
    public VehicleStatsReport(List<VehicleStats> vehicleStats) {

	this.vehicleStats = vehicleStats;
    }

    /**
     * This method is used to get Vehicle Stats based on Morning or evening time.
     * @param direction The Direction of Vehicles
     * @param isAverageByDays Is the Report Averaged by total number of Days. If false Then The report Displays Statistics for each Day.
     * @return Map&lt;String,Map&lt;String,Long&gt;&gt; or Map&lt;String,Long&gt; depending upon isAverageByDays flag 
     */
    public Map<String, ?> getVehicleCountByMorningEveningForDirection(Direction direction, boolean isAverageByDays) {
	Predicate<VehicleStats> predicate = (vehicleStat) -> vehicleStat.getDirection() == direction;

	Function<VehicleStats, String> topGroupByFunction = VehicleStats::getAMPMInstant;

	Function<VehicleStats, String> subGroupByFunction = VehicleStats::getDay;

	return getVehicleCountByPredicateAndGroupingFunction(predicate, topGroupByFunction, subGroupByFunction,
		isAverageByDays);

    }

    /**
     * This method returns vehicle statistics based on time intervals by minutes.
     * @param direction The Direction of Vehicles
     * @param timeInterval A valid time interval. Example 20,30 or 40 minutes required to group by interval per hour. 
     * @param isAverageByDays Is the Report Averaged by total number of Days. If false Then The report Displays Statistics for each Day.
     * @return Map&lt;String,Map&lt;String,Long&gt;&gt; or Map&lt;String,Long&gt; depending upon isAverageByDays flag
     * @throws InValidTimeIntervalException This exception is thrown for invalid intervals. Example intervals &lt;=0
     */
    public Map<String, ?> getVehicleCountByMinuteDivisionForDirection(Direction direction, int timeInterval,
	    boolean isAverageByDays) throws InValidTimeIntervalException {

	if (timeInterval <= 0) {
	    throw new InValidTimeIntervalException("Invalid Time Interval" + timeInterval);
	}

	Predicate<VehicleStats> predicate = (vehicleStat) -> vehicleStat.getDirection() == direction;

	Function<VehicleStats, String> topGroupByFunction = (VehicleStats e) -> {
	    int startTimeInterval = (e.getAverageTime().getMinute() / timeInterval) * timeInterval;
	    return "Count of Vehicles between time range of " + startTimeInterval + " and "
		    + (startTimeInterval + timeInterval) + " min";

	};

	Function<VehicleStats, String> subGroupByFunction = VehicleStats::getDay;

	return getVehicleCountByPredicateAndGroupingFunction(predicate, topGroupByFunction, subGroupByFunction,
		isAverageByDays);
    }

    /**
     * This method returns vehicle count per hour.
     * @param direction The Direction of Vehicles
     * @param isAverageByDays Is the Report Averaged by total number of Days. If false Then The report Displays Statistics for each Day.
     * @return Map&lt;String,Map&lt;String,Long&gt;&gt; or Map&lt;String,Long&gt; depending upon isAverageByDays flag 
     */
    public Map<String, ?> getVehicleCountByHourForDirection(Direction direction, boolean isAverageByDays) {

	Predicate<VehicleStats> predicate = (vehicleStat) -> vehicleStat.getDirection() == direction;

	Function<VehicleStats, String> topGroupByFunction = (VehicleStats e) -> {
	    return "Count of Vehicle at the hour " + e.getAverageTime().getHour();
	};

	Function<VehicleStats, String> subGroupByFunction = VehicleStats::getDay;

	return getVehicleCountByPredicateAndGroupingFunction(predicate, topGroupByFunction, subGroupByFunction,
		isAverageByDays);
    }

    /**
     * This method returns the Peak time averaged over all days for a perticular hour.
     * @return Peak Vehicle count for a perticular hour averaged over total days
     */
    public List<Entry<String, Long>> getPeakVehicleTimeByHoursOverAverageDays() {

	Function<VehicleStats, Integer> hourFunction = (vehicleStats) -> {
	    return vehicleStats.getAverageTime().getHour();
	};

	Map<Integer, Long> dataMap = this.vehicleStats.stream()
		.collect(Collectors.groupingBy(hourFunction, Collectors.counting()));

	Map<String, Long> averageMap = new LinkedHashMap<>();
	dataMap.entrySet().forEach((e) -> {
	    averageMap.put("Peak Average Traffic at the " + e.getKey() + " Hour",
		    e.getValue() / vehicleStats.get(0).getTotalNumberOfDays());
	});

	Long max = averageMap.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getValue();

	List<Map.Entry<String, Long>> listOfMax = averageMap.entrySet().stream()
		.filter(entry -> entry.getValue() == max).collect(Collectors.toList());

	return listOfMax;

    }

    /**
     * 
     * @return Map of data showing speed distribution averaged over total number of days.
     */
    public Map<String, Long> getAveragedSpeedDistribution() {

	Function<VehicleStats, Integer> speedFunction = (vehicleStats) -> {
	    return vehicleStats.getAverageSpeed();
	};

	Map<Integer, Long> dataMap = this.vehicleStats.stream()
		.collect(Collectors.groupingBy(speedFunction, Collectors.counting()));

	Map<String, Long> averageMap = new LinkedHashMap<>();
	dataMap.entrySet().forEach((e) -> {
	    averageMap.put("Count of Vehicle at " + e.getKey() + " Km/Hr",
		    e.getValue() / vehicleStats.get(0).getTotalNumberOfDays());
	});

	return averageMap;

    }

    /**
     * 
     * @param direction The Direction of Vehicles
     * @param startperiod The start range time
     * @param endPeriod The end range time.
     * @return rough distance between vehicles
     */
    public String getRoughDistanceBetweenVehicleBetweenPeriod(Direction direction, LocalTime startperiod,
	    LocalTime endPeriod) {

	Predicate<VehicleStats> predicate = (VehicleStats v) -> {

	    return v.getDirection() == direction && v.getAverageTime().isAfter(startperiod)
		    && v.getAverageTime().isBefore(endPeriod);
	};

	VehicleStats previousVehicleStat = null;

	List<VehicleStats> filteredList = vehicleStats.stream().filter(predicate).collect(Collectors.toList());

	List<VehiclePair> vehiclePair = IntStream.range(1, filteredList.size())
		.mapToObj(i -> new VehiclePair(filteredList.get(i - 1), filteredList.get(i)))
		.collect(Collectors.toList());

	List<Integer> distance = new ArrayList<>();
	for (VehiclePair p : vehiclePair) {

	    int firstVehicleDistance = (int) (p.v1.getAverageSpeed() * (float) p.v1.getAverageTime().toSecondOfDay()
		    / SECONDS_OF_DAY * METER_FACTOR);
	    int secondVehicleDistance = (int) (p.v2.getAverageSpeed() * (float) p.v2.getAverageTime().toSecondOfDay()
		    / SECONDS_OF_DAY * METER_FACTOR);

	    distance.add(Math.abs(firstVehicleDistance - secondVehicleDistance));

	}

	OptionalDouble doubleValue = distance.stream().mapToInt((i) -> i).average();
	return doubleValue.isPresent() ? doubleValue.getAsDouble() + " meters" : 0 + " meters";

    }

    private Map<String, ?> getVehicleCountByPredicateAndGroupingFunction(Predicate<VehicleStats> direction,
	    Function<VehicleStats, String> topGroupBy, Function<VehicleStats, String> subGroupBy,
	    boolean isAverageByDays) {

	if (!isAverageByDays) {
	    return this.vehicleStats.stream().filter(direction).collect(Collectors.groupingBy(topGroupBy,
		    LinkedHashMap::new, Collectors.groupingBy(subGroupBy, LinkedHashMap::new, Collectors.counting())));
	} else {

	    Map<String, Long> data = this.vehicleStats.stream().filter(direction)
		    .collect(Collectors.groupingBy(topGroupBy, LinkedHashMap::new, Collectors.counting()));

	    Map<String, Long> resultData = new LinkedHashMap<>();
	    data.entrySet().stream().forEach((Entry<String, Long> e) -> {
		resultData.put(e.getKey(), (e.getValue() / vehicleStats.get(0).getTotalNumberOfDays()));

	    });
	    return resultData;
	}

    }

    class VehiclePair {

	public VehiclePair(VehicleStats v1, VehicleStats v2) {

	    this.v1 = v1;
	    this.v2 = v2;
	}

	VehicleStats v1;
	VehicleStats v2;

    }

}
