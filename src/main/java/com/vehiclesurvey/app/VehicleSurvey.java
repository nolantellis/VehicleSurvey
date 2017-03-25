package com.vehiclesurvey.app;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import com.vehiclesurvey.exception.InValidTimeIntervalException;
import com.vehiclesurvey.exception.InvalidHoseDataPointException;
import com.vehiclesurvey.model.Direction;
import com.vehiclesurvey.model.VehicleStats;
import com.vehiclesurvey.report.VehicleStatsReport;
import com.vehiclesurvey.util.FileReaderUtil;
import com.vehiclesurvey.util.VehicleStatsBuilder;

public class VehicleSurvey {

    /**
     * This is the main method of the class where the point of execution starts
     * 
     * @param args
     *            The argument should be full path or relative path to a file
     *            name containing data points of vehicles.
     * @param <T> Generic Return Type for Map template           
     * 
     */
    public static <T> void main(String[] args) {
	if (args.length != 1) {
	    System.out.println("Please Provide Data File which contains HosePoints");
	    System.exit(0);
	}

	boolean isAverageByDays = true;
	Consumer<T> printConsumer = System.out::println;
	List<String> dataPoints = getListOfDataPoints(args);

	List<VehicleStats> vehicleStats = new ArrayList<>();
	vehicleStats = getVehicleStatsList(dataPoints, vehicleStats);

	printMetaData(vehicleStats);
	VehicleStatsReport report = new VehicleStatsReport(vehicleStats);

	printMorningEveningReport(isAverageByDays, printConsumer, report);

	printHourlyReport(isAverageByDays, printConsumer, report);

	try {
	    printTimeIntervalReport(Direction.NORTH, "North", isAverageByDays, printConsumer, report);
	    printTimeIntervalReport(Direction.SOUTH, "South", isAverageByDays, printConsumer, report);
	} catch (InValidTimeIntervalException e) {
	    System.out.println("Invalid Time interval for reports");
	    System.exit(0);
	}
	printPeakVolumeHour(printConsumer, report);

	printAverageSpeedDistribution(printConsumer, report);

	printRoughDistanceReport(report);
    }

    private static <T> void printHourlyReport(boolean isAverageByDays, Consumer<T> printConsumer,
	    VehicleStatsReport report) {
	printHourlyReport(!isAverageByDays, printConsumer, report,
		"Hourly Report for Vehicle moving in north direction per day", Direction.NORTH);
	printHourlyReport(!isAverageByDays, printConsumer, report,
		"Hourly Report for Vehicle moving in south direction per day", Direction.SOUTH);
	printHourlyReport(isAverageByDays, printConsumer, report,
		"Hourly Report for Vehicle moving in north direction average over days", Direction.NORTH);
	printHourlyReport(isAverageByDays, printConsumer, report,
		"Hourly Report for Vehicle moving in south direction average over days", Direction.SOUTH);
    }

    private static <T> void printHourlyReport(boolean isAverageByDays, Consumer<T> printConsumer,
	    VehicleStatsReport report, String title, Direction direction) {
	generateReportTitle(title);
	Map reportData = report.getVehicleCountByHourForDirection(direction, isAverageByDays);
	reportData.entrySet().stream().forEach(printConsumer);
	System.out.println();
    }

    private static void printMetaData(List<VehicleStats> vehicleStats) {
	System.out.println("Total Vehicles :" + vehicleStats.size());

	System.out.println("Total Vehicles moving north bound:" + vehicleStats.stream().filter((VehicleStats s) -> {
	    return s.getDirection() == Direction.NORTH;
	}).count());

	System.out.println("Total Vehicles moving south bound:" + vehicleStats.stream().filter((VehicleStats s) -> {
	    return s.getDirection() == Direction.SOUTH;
	}).count());

	System.out.println("Total No of Days :" + vehicleStats.get(0).getTotalNumberOfDays());
    }

    private static <R> void printAverageSpeedDistribution(Consumer<R> printConsumerPeak, VehicleStatsReport report) {
	generateReportTitle("Average Speed Distribution Report averaged accross all days");
	report.getAveragedSpeedDistribution().entrySet()
		.forEach((Consumer<? super Entry<String, Long>>) printConsumerPeak);
    }

    private static void printRoughDistanceReport(VehicleStatsReport report) {
	generateReportTitle("Rough distance between vehicle in North Direction between 00:00 and 03:00");
	System.out.println(report.getRoughDistanceBetweenVehicleBetweenPeriod(Direction.NORTH, LocalTime.MIDNIGHT,
		LocalTime.of(03, 00)));

	generateReportTitle("Rough distance between vehicle in South Direction between 00:00 and 03:00");
	System.out.println(report.getRoughDistanceBetweenVehicleBetweenPeriod(Direction.SOUTH, LocalTime.MIDNIGHT,
		LocalTime.of(03, 00)));

	generateReportTitle("Rough distance between vehicle in North Direction between 03:00 and 09:00");
	System.out.println(report.getRoughDistanceBetweenVehicleBetweenPeriod(Direction.NORTH, LocalTime.of(3, 0),
		LocalTime.of(9, 00)));

	generateReportTitle("Rough distance between vehicle in South Direction between 03:00 and 09:00");
	System.out.println(report.getRoughDistanceBetweenVehicleBetweenPeriod(Direction.SOUTH, LocalTime.of(3, 0),
		LocalTime.of(9, 00)));
    }

    private static <R> void printPeakVolumeHour(Consumer<R> printConsumerPeak, VehicleStatsReport report) {
	generateReportTitle("Peak Volume Time average by day");
	List<Entry<String, Long>> reportData = report.getPeakVehicleTimeByHoursOverAverageDays();
	reportData.stream().forEach((Consumer<? super Entry<String, Long>>) printConsumerPeak);
	System.out.println();
    }

    private static <T> void printMorningEveningReport(boolean isAverageByDays, Consumer<T> printConsumer,
	    VehicleStatsReport report) {
	printMorningEveningReport("Statistics of Vehicles in North Direction in the Morning And Evening per Day",
		Direction.NORTH, isAverageByDays, printConsumer, report);

	printMorningEveningReport("Statistics of Vehicles in South Direction in the Morning And Evening per Day",
		Direction.SOUTH, isAverageByDays, printConsumer, report);

	printMorningEveningReport(
		"Statistics of Vehicles in North Direction in the Morning And Evening average by days", Direction.NORTH,
		!isAverageByDays, printConsumer, report);

	printMorningEveningReport(
		"Statistics of Vehicles in South Direction in the Morning And Evening average by days", Direction.SOUTH,
		!isAverageByDays, printConsumer, report);
    }

    private static <T> void printTimeIntervalReport(Direction direction, String directionString,
	    boolean isAverageByDays, Consumer<T> printConsumer, VehicleStatsReport report)
	    throws InValidTimeIntervalException {
	printTimeIntervalReport(!isAverageByDays, printConsumer, report, "Statistics of Vehicles in " + directionString
		+ " Direction by 30 minute interval of every hour per day", 30, direction);

	printTimeIntervalReport(!isAverageByDays, printConsumer, report, "Statistics of Vehicles in " + directionString
		+ " Direction by 20 minute interval of every hour per day", 20, direction);

	printTimeIntervalReport(!isAverageByDays, printConsumer, report, "Statistics of Vehicles in " + directionString
		+ " Direction by 15 minute interval of every hour per day", 15, direction);

	printTimeIntervalReport(isAverageByDays, printConsumer, report, "Statistics of Vehicles in " + directionString
		+ " Direction by 30 minute interval of every hour average by day", 30, direction);

	printTimeIntervalReport(isAverageByDays, printConsumer, report, "Statistics of Vehicles in " + directionString
		+ " Direction by 20 minute interval of every hour average by day", 20, direction);

	printTimeIntervalReport(isAverageByDays, printConsumer, report, "Statistics of Vehicles in " + directionString
		+ " Direction by 15 minute interval of every hour average by day", 15, direction);
    }

    private static <T> void printTimeIntervalReport(boolean isAverageByDays, Consumer<T> printConsumer,
	    VehicleStatsReport report, String title, int timeInterVal, Direction direction)
	    throws InValidTimeIntervalException {
	generateReportTitle(title);
	Map reportData = report.getVehicleCountByMinuteDivisionForDirection(direction, timeInterVal, isAverageByDays);
	reportData.entrySet().stream().forEach(printConsumer);
	System.out.println();
    }

    private static <T> void printMorningEveningReport(String title, Direction d, boolean isAverageByDays,
	    Consumer<T> printConsumer, VehicleStatsReport report) {
	generateReportTitle(title);

	Map reportData = report.getVehicleCountByMorningEveningForDirection(d, !isAverageByDays);

	reportData.entrySet().stream().forEach(printConsumer);
	System.out.println();
    }

    private static void generateReportTitle(String title) {
	System.out.println("");
	System.out.println(title);

	IntStream.range(0, 200).mapToObj((i) -> "-").forEach(System.out::print);
	System.out.println("\n");

    }

    private static List<VehicleStats> getVehicleStatsList(List<String> dataPoints, List<VehicleStats> vehicleStats) {
	try {
	    vehicleStats = new VehicleStatsBuilder().getVehicleStatsFromDataPoints(dataPoints);
	} catch (InvalidHoseDataPointException e) {
	    System.out.println("Error while processing a DataPoint " + e.getMessage());
	    System.exit(0);
	}
	return vehicleStats;
    }

    private static List<String> getListOfDataPoints(String[] args) {
	List<String> datPoints = null;
	try {
	    datPoints = FileReaderUtil.readFile(args[0]);
	} catch (IOException e) {
	    System.out.println("The File Does not Exists at the given path.");
	    System.exit(0);
	}
	return datPoints;
    }
}
