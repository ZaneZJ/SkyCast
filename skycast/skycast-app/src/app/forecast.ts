import { City } from "./city";

export interface Forecast {

    id: number;
    dt: number;
    temp: number;
    feelsLike: number;
    tempMin: number;
    tempMax: number;
    pressure: number;
    seaLevel: number;
    grndLevel: number;
    humidity: number;
    tempKf: number;
    weatherId: number;
    weatherMain: string;
    weatherDescription: string;
    weatherIcon: string;
    cloudsAll: number;
    windSpeed: number;
    windDeg: number;
    windGust: number;
    visibility: number;
    dtTxt: string;
    city: City;

}