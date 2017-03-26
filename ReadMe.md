Problem
------------------
Please Read Problem.md file for problem statement.

Reason For Choosing the Problem
-------------------------------
The 1800-No Problem is more on backtracking algorithm going by the books.
I liked the Vehicle Survey problem because it has many aspects to be tested upon.
* Proper choice of data structure which will make reporting challenge easy and reusable.
* The reporting query gave me a hint that using Stream API will reduce the calculations and reporting would not be a complicated task.
* The most important aspect was the data structure to store per vehicle data. If the data structure was right, then the reporting would be easy.

Design Approach
------------------
* I divided the problem in 3 parts.
* Parsing the data file and creating a data structure for a single vehicle statistic.
* For a List of Vehicle Stats run reports on the list, grouping by various categories.
* Using test driven development for a sample of 4 to 5 data points, I created around 35 test cases which tests the reports and manual calculations to verify the same.

If it works for 4 sample data. The same will work for the large sample set provided in the problem.

* I highly doubt I would be able to solve the problem if it was not for TDD type programming.
* Making any changes in the structure was made with confidence of not breaking code.


While processing each data point in the file, I used a state transaction flow for the below reasons.
* It reduces complexity of coding
* Easy to create a vehicle stat
* Calculate Average Speed and time while creating the vehicle stats, will help to avoid overhead of calculation during reporting.
* For a large data set, Stream API uses multiple threads in the background hence speeding up the reporting process.

Pattern in report
* Every Report is grouped by Direction of vehicle and then grouped by function of the Vehicle Stat data structure.
* So making use of API like predicate and Functions of functional interface, the base structure of all reports were reuseable.


Tools And Technology Used
-------------------------
* Maven for dependency injection
* Java 8 for Backend Development
* Jacoco for code coverage. As cobertura does not support java 8 instrumentation as of now.

How to Run via JAVA
-----------------
```sh
* java -jar VehicleSurvey-1.0-SNAPSHOT.jar src/test/resource/VehicleSurveySample.txt
```
* From Project Directory : Vehicle Survey
* java -jar target/VehicleSurvey-1.0-SNAPSHOT.jar src/test/resource/VehicleSurveySample.txt

* The file name can be full path or relative path.

Run Via Maven
----------------
* The above program also runs in the install phase and is executed with the sample file which was attached with the problem.

Command
----------
* mvn clean install
* The above command will also run the program after all tests are passed.

Test Coverage Reports
----------------------
* For Test run 'mvn test'
* After running 'mvn clean install'. An index.html file is generated in target/site/jacoco directory. You can view the coverage report in the same.

Java API Docs for documentaion over classes and methods
-------------
 mvn javadoc:javadoc

ASSUMPTIONS
---------------------------
* Grouping by Minutes is considered to be in the same group by the hour.
* Example first 20 minutes of hour 1 is considered to be in same group for 1st 20 minutes of next hour.
* If that is not the case ,only 1 parameter needs to be changed to group by hours of day.
* Time of the vehicle passing the strip is considered average of time all the hosepoints of a car.
* Average distance between cars are in meters.
* All Reporting APIs are currently in 1 class for the given problem statement.

Important Classes with 1 line explaination
-----------------------------------------
* VehicleStat - > Basic pojo which holds data of a vehicle . Data like average speed,average time.
* VehicleStatReport - > This is a class which builds a report based ona  list of VehicleStat.
* VehicleStatBuilder - > Main class which reads all points in a file and generate a list of Vehicle stats.
* HosePoint - > An instant when a vehicle passes the strip on the road.

Main Class
---------------------------------------
VehicleSurvey class is the main class. in com.vehiclesurvey.app package.
