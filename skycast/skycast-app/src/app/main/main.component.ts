import { HttpClient } from '@angular/common/http';
import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
// import { Chart } from 'chart.js';
// import { ChartOptions, ChartDataset, ChartType } from 'chart.js';

import * as d3 from 'd3';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
  encapsulation: ViewEncapsulation.None,
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
  
  // FORECAST CHART 

  // Define your chart data
  private data = [
    {day: 'Monday', temperature: 22},
    {day: 'Tuesday', temperature: 27},
    {day: 'Wednesday', temperature: 24},
    {day: 'Thursday', temperature: 26},
    {day: 'Friday', temperature: 20},
    {day: 'Saturday', temperature: 25}
  ];

  // // Chart type
  // public chartType: ChartType = 'line';  // or 'bar', 'radar', etc.,

  // // Chart colors
  // chartColors = [
  //   {
  //     backgroundColor: 'rgba(255,255,0,0.28)',
  //     borderColor: 'black'
  //   }
  // ];

  // // X-axis labels
  // chartLabels: string[] = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

  // // Temperature data
  // chartData: ChartDataset[] = [
  //   { 
  //     data: [30, 21, 22, 23, 19, 20, 21], // will populate this with the response from the API
  //     label: 'Temperature',
  //     borderColor: 'black',
  //     backgroundColor: 'rgba(255,255,0,0.28)',
  //   }
  // ];

  // // Options
  // chartOptions: ChartOptions = {
  //   responsive: true,
  //   scales: {
  //     y: {
  //       beginAtZero: true
  //     }
  //   }
  // };

  constructor(private http: HttpClient) {
    // Initialization inside the constructor
  }

  ngOnInit() {
    // populate the fields
    this.createChart();
  }

  private createChart(): void {

    let margin = {top: 20, right: 30, bottom: 40, left: 50};
    let width = 960 - margin.left - margin.right;
    let height = 500 - margin.top - margin.bottom;

    // Define your scales
    const xScale = d3.scalePoint().range([0, 500]).domain(this.data.map(d => d.day));
    const yScale = d3.scaleLinear().domain([0, d3.max(this.data.map(d => d.temperature!))!]).range([height, 0]);

    // Define the line
    const line = d3.line<any>()
      .x((d: any) => xScale(d.day) || 0)
      .y((d: any) => yScale(d.temperature) || 0);

    // Append the SVG object to the body of the page
    const svg = d3.select('div#chart')
      .append('svg')
        .attr('width', 600)
        .attr('height', 600)
      .append('g')
        .attr('transform', 'translate(50, 50)');

    // Add the line
    svg.append('path')
      .datum(this.data)
      .attr('fill', 'none')
      .attr('stroke', 'steelblue')
      .attr('stroke-width', 1.5)
      .attr('d', line);

    // Add the X Axis
    svg.append('g')
      .attr('transform', 'translate(0,' + 500 + ')')
      .call(d3.axisBottom(xScale));

    // Add the Y Axis
    svg.append('g')
      .call(d3.axisLeft(yScale));
  }

}