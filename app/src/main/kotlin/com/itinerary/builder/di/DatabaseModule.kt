package com.itinerary.builder.di

import androidx.room.Room
import com.itinerary.core.common.Constants
import com.itinerary.core.database.ItineraryDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            ItineraryDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<ItineraryDatabase>().tripDao() }
    single { get<ItineraryDatabase>().interestMarkDao() }
    single { get<ItineraryDatabase>().edgeDao() }
}
