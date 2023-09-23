# Incidents Service


Contains the source code for an Incidents Service, which allows users to create and manage incidents related to power plants. It provides REST API for creating incidents and sending them to a Kafka topic.It  exposes a REST endpoint that takes as input a powerplant ID and generates a new Kafka message with the incident

# API Endpoints

## Create Incident
Create a new incident for a power plant.

### URL: 
http://localhost:8083/incidents/{plantId}

### Method:
 POST

### Request Body:
{
  
  "description": "Incident description",
  "severity": "High",
  "status": "Open",
  "reportedBy": {
    
   "name": "John",
  "lastName": "Doe",
  "email": "john.doe@abc.com"
  },
  "assignedTo": {
  "name": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@abc.com"
  }
}
Response:

HTTP Status: 201 (Created)

### Response Body:
{
  "id": 1,
  "timestamp": "2023-05-14T10:30:00Z",
  "description": "Incident description",
  "severity": "High",
  "status": "Open",
  "resolution": null,
  "resolvedAt": null,
  "reportedBy": {
   "id": 1,
   "name": "John",
   "lastName": "Doe",
   "email": "john.doe@example.com"
  },
  "assignedTo": {
   "id": 2,
   "name": "Jane",
   "lastName": "Smith",
   "email": "jane.smith@example.com"
  },
  "powerPlant": {
   "id": plantId,
   "gppd_idnr": "GPPD123",
   "country": "Country",
   "country_long": "Country Name",
   "name": "Power Plant",
   "capacity_mw": 100.0,
   "latitude": 12.34,
   "longitude": 56.78,
   "primary_fuel": "Fuel"
  }
  }

# Database

Make sure you have the PSQL server running and update the database configurations in the application.properties file.

# Kafka

The application sends incidents to a Kafka topic named "incidents". Make sure you have Kafka installed and running.

