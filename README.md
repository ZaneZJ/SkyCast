# SkyCast

Simple RESTful web service that would satisfy a set of functional requirements, as well as a list of non-functional requirements. 

### Functional requirements:

A web service that handle GET requests to path “weather” by returning the weather data determined by IP of the request originator.
Upon receiving a request, the service perform a geolocation search using a non-commercial, 3rd party IP to location provider.
Having performed the reverse geo search service use another non-commercial, 3rd party service to determine current weather conditions using the coordinates of the IP.

### Non-functional requirements:

1. Test coverage should be not less than 80%
2. Implemented web service should be resilient to 3rd party service unavailability
3. Data from 3rd party providers should be stored in a database
4. An in-memory cache should be used as the first layer in data retrieval
5. DB schema should allow a historical analysis of both queries from a specific IP and of weather
conditions for specific coordinates
6. DB schema versioning should be implemented

### Demo:

API used:
1. https://ipgeolocation.io/
2. https://openweathermap.org/

On the top part of the app data is coming from Current Weather Data API, but the bottom chart is populaated using 5 Day / 3 Hour Forecast where the chart itself is created by retreaving all data values by every 3h, and on hover the user is able to see the day and the averrage temperature. 

![Demo](https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExMmRiYWI0YjUyZDBjODM3OWU1Zjk5ZTI0OTBmYmU3NWI3Njk3NTJhMyZlcD12MV9pbnRlcm5hbF9naWZzX2dpZklkJmN0PWc/Kmv0dtrk42zoAs2wYy/giphy.gif)

### Testing:

![Demo](https://media2.giphy.com/media/v1.Y2lkPTc5MGI3NjExYWMwYzYwOWFhZGZlMmY2YTA0Y2Y5MTBlYzJhZmRjMmQ2NTU2MmRmZSZlcD12MV9pbnRlcm5hbF9naWZzX2dpZklkJmN0PWc/x2Vn82qJmdbbO7uOJI/giphy.gif)
