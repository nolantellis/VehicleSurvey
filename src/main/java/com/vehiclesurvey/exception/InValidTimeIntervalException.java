package com.vehiclesurvey.exception;

import com.vehiclesurvey.report.VehicleStatsReport;

/**
 * This exception is generated when an invalid time interval is used for generation of Vehicle stats report
 * @see VehicleStatsReport
 * @author Nolan.Tellis
 *
 */
public class InValidTimeIntervalException extends Exception {
    
    String message;

    public InValidTimeIntervalException(String message) {
	super();
	this.message = message;
    }
    
    

}
