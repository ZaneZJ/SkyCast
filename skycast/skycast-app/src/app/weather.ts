import { City } from "./city";

export interface Weather {

    id: number;
    dt: string;
    weatherDescription: string;
    weatherIcon: string;
    temp: number;
    feelsLike: number;
    tempMin: number;
    tempMax: number;
    pressure: number;
    humidity: number;
    windSpeed: number;
    windDeg: number;
    cloudsAll: number;
    sysSunrise: string;
    sysSunset: string;
    city: City;

}