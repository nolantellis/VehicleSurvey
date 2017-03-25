Problem
------------------
Please Read Problem.md file for problem statement.

Tools And Technology Used
-------------------------
* Maven for dependency injection
* Java 8 for Backend Development
* Jacoco for code coverage. As cobertura does not support java 8 instrumentation as of now.

How to Run via JAVA
-----------------
java -jar VehicleSurvey-1.0-SNAPSHOT.jar <File Name>

From Project Directory : Vehicle Survey
java -jar target/VehicleSurvey-1.0-SNAPSHOT.jar src/test/resource/VehicleSurveySample.txt

The file name can be full path or relative path.

Run Via Maven
----------------
* The above program also runs after the tests are executed with the sample file which was attached with the problem.

Command
----------
mvn clean install
The above command will also run the program after all tests are passed.

Test Coverage Reports
----------------------
For Test run 'mvn test'
After running 'mvn clean install'. An index.html file is generated in target/site/jacoco directory. You can view the coverage report in the same.

Java API Docs for documentaion over classes and methods
-------------
 mvn javadoc:javadoc

ASSUMPTIONS
---------------------------
* Grouping by Minutes is considered to be in the same group by the hour.
** Example first 20 minutes of hour 1 is considered to be in same group for 1st 20 minutes of next hour.
** If that is not the case ,only 1 parameter needs to be changed to group by hours of day.
* Time of the vehicle passing the strip is considered average of time all the hosepoints of a car.
* Average distance between cars are in meters.
* All Reporting APIs are currently in 1 class for the given problem statement.

Important Classes with 1 line explaination
-----------------------------------------
VehicleStat - > Basic pojo which holds data of a vehicle . Data like average speed,average time.
VehicleStatReport - > This is a class which builds a report based ona  list of VehicleStat.
VehicleStatBuilder - > Main class which reads all points in a file and generate a list of Vehicle stats.
HosePoint - > An instant when a vehicle passes the strip on the road.

Main Class
---------------------------------------
VehicleSurvey class is the main class. in com.vehiclesurvey.app package.
