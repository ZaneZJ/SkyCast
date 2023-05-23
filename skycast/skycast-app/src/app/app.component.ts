import { Component } from '@angular/core';
import { ForecastService } from './forecast.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'skycast-app';

  constructor(private forecastService: ForecastService) {}

  ngOnInit() { }
  
}