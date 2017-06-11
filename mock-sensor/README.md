1. Run next command in current directory to build mock sensor: 

 `mvn clean install`

2.  Navigate to directory target:

`cd target`

3. Execute jar file to launch mock sensor program
 
`java -jar sensor-mock.jar 1 10`
 
The first number specify sensor identifier, it is required parameter. The second one is how often sensor will sens data to server or log frequency.

The second parameter is measured in seconds and optional, if you don't specify frequency, program will use default value -- 15.
 
  
4. After executing jar you may see next output messages:
  
  4.1 Sensor is not created 
    `2017-06-11 19:31:22 [Thread-0] INFO  c.r.plantcare.mocksensor.Sensor - new value: 15`
    `2017-06-11 19:31:22 [Thread-0] INFO  c.r.plantcare.mocksensor.Sensor - Status code: 404; Reason phrase: ; Response message: {"message":"Sensor with id [1] does not exist"}; `
  
  4.2 One did not bind sensor to plant so it is not activated
    `2017-06-11 19:37:38 [Thread-0] INFO  c.r.plantcare.mocksensor.Sensor - new value: 89`
    `2017-06-11 19:37:38 [Thread-0] INFO  c.r.plantcare.mocksensor.Sensor - Status code: 428; Reason phrase: ; Response message: {"message":"Sensor with id [1] is not activated"};`
   
  4.3 Sensor stars logging data to database:
    `2017-06-11 19:39:53 [Thread-0] INFO  c.r.plantcare.mocksensor.Sensor - new value: 34`
    `2017-06-11 19:39:53 [Thread-0] INFO  c.r.plantcare.mocksensor.Sensor - Status code: 200; Reason phrase: ; Response message: ; `
    
    