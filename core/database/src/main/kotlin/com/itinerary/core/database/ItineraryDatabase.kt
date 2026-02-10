package com.itinerary.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itinerary.core.database.dao.EdgeDao
import com.itinerary.core.database.dao.InterestMarkDao
import com.itinerary.core.database.dao.TripDao
import com.itinerary.core.database.entity.EdgeEntity
import com.itinerary.core.database.entity.InterestMarkEntity
import com.itinerary.core.database.entity.TripEntity

@Database(
    entities = [
        TripEntity::class,
        InterestMarkEntity::class,
        EdgeEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class ItineraryDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun interestMarkDao(): InterestMarkDao
    abstract fun edgeDao(): EdgeDao
}
