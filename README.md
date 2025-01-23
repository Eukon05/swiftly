# Swiftly
Remitly Summer Internship 2025 recruitment task

## About the project
The app parses a provided Excel spreadsheet containing bank details into an SQL databse.  
The data is available to view and edit through a REST api, created using Spring Boot.  

## Endpoints
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

**GET** `/api/v1/swift-codes/country/{countryISO2}`  
Returns a list of all banks located in the specified country:
```json
{
  "countryISO2": "PL",
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
All fields specified above CANNOT BE NULL OR EMPTY, except for the address field.  
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