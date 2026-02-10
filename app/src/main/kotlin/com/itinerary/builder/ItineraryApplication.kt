package com.itinerary.builder

import android.app.Application
import com.itinerary.builder.di.appModule
import com.itinerary.builder.di.databaseModule
import com.itinerary.builder.di.domainModule
import com.itinerary.core.data.dataModule
import com.itinerary.core.network.networkModule
import com.itinerary.feature.details.di.detailsModule
import com.itinerary.feature.destinations.di.destinationsModule
import com.itinerary.feature.home.di.homeModule
import com.itinerary.feature.map.di.mapModule
import com.itinerary.feature.schedule.di.scheduleModule
import com.itinerary.feature.trip.di.tripModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ItineraryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@ItineraryApplication)
            modules(
                // Core modules
                appModule,
                databaseModule,
                networkModule,
                dataModule,
                domainModule,
                
                // Feature modules
                homeModule,
                tripModule,
                mapModule,
                destinationsModule,
                scheduleModule,
                detailsModule
            )
        }
    }
}
