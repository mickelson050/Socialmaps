import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AgmCoreModule } from '@agm/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { MapMenuComponent } from './map-menu/map-menu.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    MapMenuComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyALJZuq_Fj4K-NYoFqE5tOhW4vPiY5nHr8'
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
