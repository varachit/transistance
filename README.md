# Transistance
Transistance, transit assistance and planner that navigate and reveal the most pleasing and efficient route to your destination.

Based on Java 11 with Maven project management, the application built with in-memory database H2, or an embedded relation database management system that runs along with the program. 

## Technology
### Backend
- Spring Framework
- Spring Web


## Architecture and Design
![alt text](https://i.imgur.com/d3zsWwR.jpg)

## Entity Relation
![alt text](https://i.imgur.com/eoa4Npi.jpg)

## Endpoints
Method: `GET`
URL: `api/v1/station`
Action: `Return all stations`

Method: `GET`
URL: `api/v1/station/{stationId}`
Action: `Return a station`

Method: `GET`
URL: `api/v1/station/search`
Parameters:
- `stationName`: Station name to search
- `stationType`: Station type to search
