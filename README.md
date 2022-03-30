# Transistance
Transistance, transit assistance and planner that navigate and reveal the most pleasing and efficient route to your destination.

Based on Java 11 with Maven project management, the application built with in-memory database H2, or an embedded relation database management system that runs along with the program. 

## Technology
### Backend
- Spring Framework
- Spring Web


## Architecture and Design
![alt text](https://i.imgur.com/d3zsWwR.jpg)

## Endpoints
```
Method: GET
URL: api/v1/station
Action: Return all stations
```

```
Method: GET
URL: api/v1/station/{stationId}
Action: Return a station
```

```
Method: GET
URL: api/v1/station/search
Action: Return filtered stations
Parameters:
  - stationName: Station name to search
  - stationType: Station type to search
```

```
Method: POST
URL: api/v1/station
Action: Add a station
Variables:
  - stationName: String
  - stationCode: String
  - stationLatitude: Double
  - stationLongitude: Double
  - stationType: String
```

```
Method: PUT
URL: api/v1/station/{stationId}
Action: Update a station
Variables:
  - stationName: String
  - stationCode: String
  - stationLatitude: Double
  - stationLongitude: Double
  - stationType: String
```

```
Method: PUT
URL: api/v1/station/status/{stationId}
Action: Update a station
Variables:
  - stationStatus: String
```

```
Method: DELETE
URL: api/v1/station/{stationId}
Action: Delete a station
```

```
Method: GET
URL: api/v1/route
Action: Return all routes
```

```
Method: GET
URL: api/v1/route/{routeId}
Action: Return a route
```

```
Method: POST
URL: api/v1/route
Action: Add a route
Variables:
    routeName: String
    routeDescription: String
    routeType: String
    routeStations: List of Station
```

```
Method: PUT
URL: api/v1/route/{routeId}
Action: Update a route
Variables:
    routeName: String
    routeDescription: String
    routeType: String
    routeStations: List of Station
```

```
Method: DELETE
URL: api/v1/route/{routeId}
Action: Delete a route
```

```
Method: POST
URL: api/v1/route/{routeId}/addStation
Action: Add a station to a route
Variables:
  - stationName: String
  - stationCode: String
  - stationLatitude: Double
  - stationLongitude: Double
  - stationType: String
```

```
Method: DELETE
URL: api/v1/route/{routeId}/deleteStation
Action: Remove a station from a route
Variables:
    - stationId: Long
```