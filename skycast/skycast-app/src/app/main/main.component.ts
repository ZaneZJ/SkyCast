import { HttpClient } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { Chart, ChartItem, ChartConfiguration } from 'chart.js';
import { ApiService } from '../api.service';
import { ApiData } from '../apidata';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
})
export class MainComponent implements OnInit {

  // FORECAST CHART

  public chart: any;
  apiData: ApiData | undefined;

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

  constructor(
    private http: HttpClient,
    private apiService: ApiService
    ) { 
    
    // this.apiData = ;

    this.apiService.getCurrentData().subscribe({
      next: data => {
        this.apiData = data;
        console.log(data);
      },
      error: console.error
    });


  }

  ngOnInit(): void {

    this.createChart();

  }

  // FORECAST CHART

  createChart(){
    let canvas = document.getElementById("canvas") as HTMLCanvasElement | null;

    console.log('Canvas:', canvas); // Log canvas element
    
    if (!canvas) {
      console.error('Unable to find canvas element');
      return;
    }

    const ctx = canvas.getContext('2d');
    if (!ctx) {
      console.error('Unable to get 2d context');
      return;
    }
    
    // Apply multiply blend when drawing datasets
    let multiply = {
      id: 'multiplyPlugin', // Unique string id for the plugin
      beforeDatasetsDraw: function(chart: Chart, options: any) {
        chart.ctx.globalCompositeOperation = 'multiply';
      },
      afterDatasetsDraw: function(chart: Chart, options: any) {
        chart.ctx.globalCompositeOperation = 'source-over';
      },
    };

    // Gradient color - this week plus
    let gradientPlus = ctx.createLinearGradient(0, 0, 0, 400);
    gradientPlus.addColorStop(0, '#e3bf97b3');
    gradientPlus.addColorStop(1, '#938b87');

    // Gradient color - this week minus
    let gradientMinus = ctx.createLinearGradient(0, 0, 0, 400);
    gradientMinus.addColorStop(0, '#7d7dd8');
    gradientMinus.addColorStop(1, '#babaee70');

    let config: ChartConfiguration = {
      type: 'line',
      data: {
        labels: ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"],
        datasets: [
          {
            label: 'plus',
            data: [24, 18, 0, 18, 24, 36, 28, 24, 18, 16, 18, 24, 36, 28],
            fill: true,
            backgroundColor: gradientPlus,
            borderColor: 'transparent'
          },
          {
            label: 'minus',
            data: [0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            fill: true,
            backgroundColor: gradientMinus,
            borderColor: 'transparent'
          }
        ]
      },
      options: {
        elements: { 
            line: {
              tension: 0.4  
            },
          point: {
            radius: 0,
            hitRadius: 0, 
            hoverRadius: 0 
          } 
        },
        scales: {
          x: { display: false },
          y: {
            display: false,
            beginAtZero: true,
          }
        }
      },
      plugins: [multiply],
    };

    console.log('Config:', config); // Log config

    this.chart = new Chart(ctx, config);
  }

}