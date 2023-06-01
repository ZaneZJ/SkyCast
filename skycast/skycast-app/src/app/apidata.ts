import { City } from "./city";
import { Forecast } from "./forecast";
import { Weather } from "./weather";

export interface ApiData {

    city: City;
    weather: Weather;
    forecastArray: Forecast[];
    
}