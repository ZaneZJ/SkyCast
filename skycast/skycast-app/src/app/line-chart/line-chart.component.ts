import { Component, OnInit } from '@angular/core';
import { Chart, ChartItem, ChartConfiguration } from 'chart.js';

@Component({
  selector: 'app-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.css']
})
export class LineChartComponent {
  constructor() { }

  public chart: any;

  ngOnInit(): void {
    this.createChart();
  }

  createChart(){
    var canvas = document.getElementById("canvas") as HTMLCanvasElement | null;

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
    var multiply = {
      id: 'multiplyPlugin', // Unique string id for the plugin
      beforeDatasetsDraw: function(chart: Chart, options: any) {
        chart.ctx.globalCompositeOperation = 'multiply';
      },
      afterDatasetsDraw: function(chart: Chart, options: any) {
        chart.ctx.globalCompositeOperation = 'source-over';
      },
    };

    // Gradient color - this week
    var gradientThisWeek = ctx.createLinearGradient(0, 0, 0, 150);
    gradientThisWeek.addColorStop(0, '#5555FF');
    gradientThisWeek.addColorStop(1, '#9787FF');

    // Gradient color - previous week
    var gradientPrevWeek = ctx.createLinearGradient(0, 0, 0, 150);
    gradientPrevWeek.addColorStop(0, '#FF55B8');
    gradientPrevWeek.addColorStop(1, '#FF8787');
    

    console.log('gradientPrevWeek:', gradientPrevWeek); // Log canvas element

    let config: ChartConfiguration = {
      type: 'line',
      data: {
        labels: ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"],
        datasets: [
          {
            label: 'This week',
            data: [24, 18, 16, 18, 24, 36, 28],
            fill: true,
            backgroundColor: gradientThisWeek,
            pointBackgroundColor: '#5555FF',
            pointBorderColor: '#9787FF'
          },
          {
            label: 'Previous week',
            data: [20, 22, 30, 22, 18, 22, 30],
            fill: true,
            backgroundColor: gradientPrevWeek,
            pointBackgroundColor: '#FF55B8',
            pointBorderColor: '#FF8787',
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
            hitRadius: 5, 
            hoverRadius: 5 
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