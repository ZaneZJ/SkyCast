import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
// import 'rxjs/add/operator/map'

@Injectable({
    providedIn: 'root'
})
export class ForecastService {
    
    
    constructor(private _http: HttpClient) { }

    
}