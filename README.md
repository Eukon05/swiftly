# Swiftly
Remitly Summer Internship 2025 recruitment task

## About the project
The app parses a provided Excel spreadsheet containing bank details into an SQL databse.  
The data is available to view and edit through a REST api, created using Spring Boot.

## How to run
The app requires Java 23 to be installed on your system.
### 1. Run from source
You need to start a PostgreSQL instance on port 5432 with the username `remitly`, password `intern` and the database name `swiftlydb`.

After cloning the repository, open the terminal in the directory of the clone where `pom.xml` file resides and run:
```shell
./mvnw spring-boot:run "-Dspring-boot.run.arguments='--swiftly.excelfile=your/path/to/file.xlsx'"
```
NOTE: You can skip the option for specifying the Excel file path. In that case, the app will use the excel file provided with the task, as it is included in the project files.

### 2. Run with Docker Compose [preferred]
Instead of going through the trouble of managing the database yourself, use Docker Compose to start the entire stack in one command.  
This method requires Docker and Docker Compose to be installed on your system as well!

- Clone the repository and open the directory where POM.xml resides in the terminal
- Run `./mvnw clean package` to build the project
- Run `docker compose up -d` to start both the database and the app in detached mode

This method will make use of the Excel file provided with the task.

## How to test
If you wish to run tests on the project, clone the repository and in the directory containing `POM.xml` run the following command:  
`./mvnw clean test`  
This will run the included suite of unit and integration tests.  
More information about the specifics of the tests can be found below.

## Endpoints

> [!IMPORTANT]  
> I had trouble understanding the descriptions of endpoints for creating a new bank, as well as for deleting one.  
>
> From my understanding, the only way to determine if a SWIFT code represents the HQ of a bank, is by checking if it's last three characters are 'XXX'.  
> The description of the POST endpoint, however, specifies a separate, `isHeadquarter` field in the request body.  
> In my opinion, this field is redundant, and it's use may result in incorrect labeling of database entries.  
> As such, I've decided that my solution to the task will ignore this field entirely, and decide if a bank is an HQ or not based on the SWIFT code alone.
>
> A similar situation applies to the DELETE endpoint. The task's description does not specify the request body for this endpoint, suggesting that the only needed value is the SWIFT code passed as a path variable, but the endpoint's description mentions that it also requires the bank's name and country.  
> 

**GET** `/api/v1/swift-codes/{swift}`  
Returns a JSON object containing bank details:  
```json
 {
  "swiftCode": "AIPOPLP1XXX",
  "bankName": "SANTANDER CONSUMER BANK SPOLKA AKCYJNA",
  "address": "STRZEGOMSKA 42C  WROCLAW, DOLNOSLASKIE, 53-611",
  "countryISO2": "PL",
  "countryName": "POLAND",
  "isHeadquarter": true
}
```  
If the requested SWIFT code represents the headquarters of a bank, the response also contains all of its branches:
```json
{
  "swiftCode": "ALBPPLPWXXX",
  "bankName": "ALIOR BANK SPOLKA AKCYJNA",
  "address": "LOPUSZANSKA BUSINESS PARK LOPUSZANSKA 38 D WARSZAWA, MAZOWIECKIE, 02-232",
  "countryISO2": "PL",
  "countryName": "POLAND",
  "branches": [
    {
      "swiftCode": "ALBPPLPWCUS",
      "bankName": "ALIOR BANK SPOLKA AKCYJNA",
      "address": "LOPUSZANSKA BUSINESS PARK LOPUSZANSKA 38 D WARSZAWA, MAZOWIECKIE, 02-232",
      "countryISO2": "PL",
      "countryName": "POLAND",
      "isHeadquarter": false
    }
  ],
  "isHeadquarter": true
}
```
When a SWIFT code is not present in the database, the user receives the following message:
```json
{
  "message": "Bank with the given SWIFT code not found"
}
```
The same applies to the  
**DELETE** `/api/v1/swift-codes/{swift}`  
endpoint, which removes the bank from the database.  
In addition to the swift code as a path variable, the endpoint also requires the following input:  
```json
{
  "bankName": "ALIOR BANK SPOLKA AKCYJNA",
  "countryISO2": "PL"
}
```
When the data does not match the one in the database, the user receives the following message:  
```json
{
  "message": "Provided data does not match the details of the bank"
}
```

**GET** `/api/v1/swift-codes/country/{countryISO2}`  
Returns a list of all banks located in the specified country:
```json
{
  "countryISO2": "PL",
  "countryName": "POLAND",
  "swiftCodes": [
    {
      "swiftCode": "AIPOPLP1XXX",
      "bankName": "SANTANDER CONSUMER BANK SPOLKA AKCYJNA",
      "address": "STRZEGOMSKA 42C  WROCLAW, DOLNOSLASKIE, 53-611",
      "countryISO2": "PL",
      "countryName": "POLAND",
      "isHeadquarter": true
    },
    ...
  ]
}
```
When the provided ISO2 is invalid or doesn't correspond to a country, the user receives the following message:  
```json
{
  "message": "getBanksByCountry.countryISO2: The bank's country code must be a valid ISO2 code"
}
```

**POST** `/api/v1/swift-codes`
This endpoint creates a new bank entry in the database. It accepts the following input:  
```json
{
  "swiftCode": "12345678",
  "countryISO2": "PL",
  "countryName": "POLAND",
  "address": "Warszawska 21, Kraków",
  "bankName": "ING"
}
```
All fields specified above CANNOT BE NULL OR EMPTY.  
Swift code must be between 8 and 11 chars. long, countryISO2 must be 2 chars. long and represent a real country.  

Failing to meet these requirements will result in an error message (depending on the mistakes):
```json
{
  "message": "The bank's swift code must be between 8 and 11 characters long"
}
```
The app will not allow creating a bank with a SWIFT code that already belongs to another bank:  
```json
{
  "message": "Bank with the given SWIFT code already exists"
}
```

## Excel parsing
The app will attempt to parse an Excel spreadsheet on the path provided by the user via the `swiftly.excelfile` Spring property (for example through env. variables).  
If the property is null, the app will skip the parsing completely. 

When the parser encounters a row that contains invalid data, it will output a log to the console:  
```
2025-01-23T22:10:44.828+01:00  WARN 696 --- [swiftly] [           main] ovh.eukon05.swiftly.excel.ExcelService   : Invalid row found at 15, skipping...
```  

If the bank we are trying to parse already exists in the database, the user will also be notified:
```
2025-01-23T22:10:45.378+01:00  WARN 696 --- [swiftly] [           main] ovh.eukon05.swiftly.excel.ExcelService   : Duplicate row found at 541, skipping...
```

It's important to know, that the parser will ALWAYS skip the first row of the spreadsheet, as it is expected to be a header row.  

All endpoints can be tested with SwaggerUI, which is available at `/api/v1/swagger-ui.html`

## Tests
The app contains both unit and integration tests to ensure reliability.  
Unit tests are performed with JUnit 5 and Mockito, and they make sure that BankService correctly interacts with a database.  

There is a separate set of unit tests dedicated to data validation.  
They check if BankDTO and BankEntity throw exceptions, when someone tries to create an instance of them with invalid data.  
Data integrity is provided by checks in constructors, and by utilising Jakarta Validation with annotations on fields.  

Integration tests use an in-memory H2 database to test if the entire cycle of interacting with the app works as intended.  
They simulate HTTP requests with MockMvc, which then passes the data to real service and database layers below.  
The tests check if the users then receives the correct data, and if the data aligns with operations performed on the database (such as when there is a request to create a new entry, the tests also check if the entry gets saved in the database).

## Edge cases
As mentioned before, data validation relies on exceptions thrown by BankDTO and BankEntity.  
The exceptions are then caught by BankControllerAdvice, and their messages are displayed to the user in the form of a simple JSON object, accompanied by a BAD REQUEST HTTP status code.  

Situations when a bank does not exist or when a user tries to override a bank are also handled by BankControllerAdvice, but by utilizing special exceptions thrown by BankService, to make it clear what exactly happened.
