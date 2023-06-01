import { Component, OnInit } from '@angular/core';
import { Chart, ChartConfiguration } from 'chart.js';
import { ApiService } from '../api.service';
import { ApiData } from '../apidata';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
})
export class MainComponent implements OnInit {

  // DATA VALUES

  city: string = "___";
  humidity: number = 0;
  currentDate: string | null = "___";
  feelsLike: number = 0;
  tempMin: number = 0;
  tempMax: number = 0;
  sunrise: string = "___";
  sunset: string = "___";
  temp: number = 0;
  windSpeed: number = 0;
  dates: string[] = []; // Today + 5 days
  tempsPlus: number[] = []; // All temps
  tempsMinus: number[] = []; // All temps
  averageTemps: number[] = []; // Average for all days

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
    private apiService: ApiService,
    private datePipe: DatePipe
    ) { 

    this.apiService.getCurrentData().subscribe({
      next: data => {
        this.apiData = data;
        console.log(data);
        // CITY DATA
        this.city = this.apiData?.city.name;
        // WEATHER DATA
        this.humidity = this.apiData?.weather.humidity;
        this.currentDate = this.apiData?.weather.dt;
        this.feelsLike = this.apiData?.weather.feelsLike;
        this.tempMin = this.apiData?.weather.tempMin;
        this.tempMax = this.apiData?.weather.tempMax;
        this.sunrise = this.apiData?.weather.sysSunrise;
        this.sunset = this.apiData?.weather.sysSunset;
        this.temp = this.apiData?.weather.temp;
        this.windSpeed = this.apiData?.weather.windSpeed;

        this.formatCurrentDate();
        this.formatTempData();
        this.createDatesArray();
        this.formatSunsetAndSurnriseDate();
        this.readChartData();
        this.createTeampsArray();
        this.createChart();

      },
      error: console.error
    });

  }

  ngOnInit(): void {
    // Initialized in constructor
  }

  // DATE FORMATER

  formatCurrentDate() {
    if (this.currentDate !== null) {
      const date = new Date(this.currentDate);
      if (isNaN(date.getTime())) {
          console.error('Invalid date:', this.currentDate);
      } else {
          this.currentDate = this.datePipe.transform(date, 'EEEE dd');
      }
    }
  }

  formatSunsetAndSurnriseDate() {
    if (this.sunrise !== null && this.sunset !== null) {
      this.sunrise = this.sunrise.substring(11, 16);
      this.sunset = this.sunset.substring(11, 16);
    }
  }

  formatTempData() {
    this.feelsLike = Math.round(this.feelsLike);
    this.tempMin = Math.round(this.tempMin);
    this.tempMax = Math.round(this.tempMax);
    this.temp = Math.round(this.temp);
  }

  // FORECAST CHART

  readChartData() {
    if (this.apiData && this.apiData.forecastArray) {
      for (let forecast of this.apiData.forecastArray) {
        if (forecast.temp >= 0) {
          this.tempsPlus.push(forecast.temp);
          this.tempsMinus.push(0);
        } else {
          this.tempsPlus.push(0);
          this.tempsMinus.push(Math.abs(forecast.temp));
        }
      }
      console.log(this.tempsPlus);
      console.log(this.tempsMinus);
    }
  }
 
  createDatesArray() {
    if (this.currentDate !== null) {
      let startDate = new Date(this.currentDate);
      let weekdays = ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"];
  
      for (let i = 0; i <= 5; i++) {
        let newDate = new Date(
          startDate.getFullYear(),
          startDate.getMonth(),
          startDate.getDate() + i
        );
  
        let dayOfWeek = weekdays[newDate.getDay()];
  
        // Add the day of the week and date string to the array
        this.dates.push(dayOfWeek); 
      }
  
      console.log(this.dates);
    }
  }

  createTeampsArray() {
    
    if (this.apiData !== undefined) {
        
      let oldDateString = "";
      let dataCount = 0;
      let tempTotal = 0;
      let isFirstValue = true;

      for (let i = 0; i < this.apiData.forecastArray.length; i++) {
        // Format the new date as a new string
        let newDateString = this.apiData?.forecastArray[i].dtTxt.substring(0, 10);

        if (oldDateString == newDateString) {
          if (i == (this.apiData.forecastArray.length - 1)) {
            let avTemp = tempTotal / dataCount;
            this.averageTemps.push(Math.round(avTemp));
          } else {
            dataCount++;
            tempTotal += this.apiData.forecastArray[i].temp;
          }
        } else {
          if (!isFirstValue) {
            let avTemp = tempTotal / dataCount;
            this.averageTemps.push(Math.round(avTemp));
            dataCount = 1;
            tempTotal = this.apiData.forecastArray[i].temp;
            oldDateString = newDateString;
          } else {
            dataCount = 1;
            tempTotal = this.apiData.forecastArray[i].temp;
            oldDateString = newDateString;
            isFirstValue = false;
          }
        }
      
      }

      console.log(this.averageTemps);

    }
  }

  createChart() {
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
        labels: this.tempsPlus.map((_, index) => `Day ${index + 1}`),
        datasets: [
          {
            label: 'plus',
            data: this.tempsPlus,
            fill: true,
            backgroundColor: gradientPlus,
            borderColor: 'transparent'
          },
          {
            label: 'minus',
            data: this.tempsMinus,
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