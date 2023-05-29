import { Injectable } from '@angular/core';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { City } from './city';
import { Weather } from './weather';
import { Forecast } from './forecast';
import { ApiData } from './apidata';

@Injectable({
    providedIn: 'root'
})
export class ApiService {

    private apiServerUrl = "http://localhost:8080";
    constructor(private http: HttpClient) { }

    getCurrentData(): Observable<ApiData> {
        return this.http.get<ApiData>(`${this.apiServerUrl}/skycast`);
    }   

}