package com.vehiclesurvey.exception;

import com.vehiclesurvey.model.HoseDataPoint;

/**
 * An Instance of InvalidHoseDataPointException is thrown when a datapoint is
 * not properly defined and is used in creation of {@link HoseDataPoint}}
 * 
 * @author Nolan.Tellis
 * 
 */
public class InvalidHoseDataPointException extends Exception {

    String message;

    /**
     * 
     * @param message The message is used to give  more information
     * on the type of exception
     */
    public InvalidHoseDataPointException(String message) {
	super(message);
	this.message = message;
    }

}
