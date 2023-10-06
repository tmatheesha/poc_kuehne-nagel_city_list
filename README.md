# poc_kuehne-nagel_city_list

POC (Proof of Concept) Project for Kuehne Nagel to manage City Details. 

## Technical Elaboration
1. This Project is Java project build with java 17 sdk.
2. This Project Uses SpringBoot framework for hosting the build
3. Maven is used as a dependency management and build management.
4. For Database MySQL has been used.

## Instructions
1. Clone the project.
2. configure the Db details (spring.datasource.url, spring.datasource.password, spring.datasource.username) in the application.yml (src/main/resources/application.yml)
3. Run a "mvn clean install".This will create an executable file (city_list-0.0.1-SNAPSHOT) in the target directory.
4. Run the "java -jar city_list-0.0.1-SNAPSHOT.jar" command to start the application at port 8084.
5. Open any browser and enter http://localhost:8084/swagger-ui.html#
6. use the uploadCities API to upload the Image-Save.csv (src/main/resources/Image-Save.csv)

## APIs
There are 3 main APIs developed in the project.
1. uploadCities - upload CSV file with city information
2. getPageableCityList - retrieve city list.
3. updateCity - update a single city.

