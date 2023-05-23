import { HttpClient } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
})
export class MainComponent implements OnInit {

  // HUMIDITY CHART

  view: [number, number] = [250, 250];
  // chart data
  results: any[] = [
    {
      "name": "HUMIDITY",
      "value": 34
    },
    {
      "name": "",
      "value": 66
    }
  ];
  colorScheme: any = {
    domain: ['black', 'rgba(187, 187, 187, 0.589)']
  };

  constructor(private http: HttpClient) { }

  ngOnInit() { }

}