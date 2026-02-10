package com.itinerary.core.data.repository

import com.itinerary.core.database.dao.EdgeDao
import com.itinerary.core.database.dao.InterestMarkDao
import com.itinerary.core.database.dao.TripDao
import com.itinerary.core.database.entity.EdgeEntity
import com.itinerary.core.database.mapper.toDomain
import com.itinerary.core.database.mapper.toEntity
import com.itinerary.core.domain.model.EdgeType
import com.itinerary.core.domain.model.Trip
import com.itinerary.core.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TripRepositoryImpl(
    private val tripDao: TripDao,
    private val interestMarkDao: InterestMarkDao,
    private val edgeDao: EdgeDao
) : TripRepository {

    override fun getAllTrips(): Flow<List<Trip>> {
        return tripDao.getAllTrips().map { trips ->
            trips.map { it.toDomain() }
        }
    }

    override fun getTripById(id: Long): Flow<Trip?> {
        return tripDao.getTripById(id).map { it?.toDomain() }
    }

    override fun getFavoriteTrips(): Flow<List<Trip>> {
        return tripDao.getFavoriteTrips().map { trips ->
            trips.map { it.toDomain() }
        }
    }

    override fun searchTrips(query: String): Flow<List<Trip>> {
        return tripDao.searchTrips(query).map { trips ->
            trips.map { it.toDomain() }
        }
    }

    override suspend fun createTrip(trip: Trip): Long {
        return tripDao.insertTrip(trip.toEntity())
    }

    override suspend fun updateTrip(trip: Trip) {
        val updatedTrip = trip.copy(updatedAt = System.currentTimeMillis())
        tripDao.updateTrip(updatedTrip.toEntity())
    }

    override suspend fun deleteTrip(id: Long) {
        // Delete all interest marks associated with this trip
        interestMarkDao.deleteInterestMarksByTripId(id)
        
        // Delete all edges related to this trip (TAO cleanup)
        edgeDao.deleteEdgesFromSource(id, EdgeType.TRIP_INTEREST_MARK.value)
        edgeDao.deleteEdgesToTarget(id, EdgeType.TRIP_FAVORITE.value)
        
        // Delete the trip itself
        tripDao.deleteTrip(id)
    }

    override suspend fun toggleFavorite(id: Long) {
        tripDao.toggleFavorite(id, System.currentTimeMillis())
        
        // Update edge graph for favorite tracking (TAO)
        val edge = edgeDao.getEdge(id, id, EdgeType.TRIP_FAVORITE.value)
        if (edge == null) {
            // Create favorite edge
            edgeDao.insertEdge(
                EdgeEntity(
                    id1 = id,
                    id2 = id,
                    type = EdgeType.TRIP_FAVORITE.value,
                    timestamp = System.currentTimeMillis(),
                    data = null
                )
            )
        } else {
            // Remove favorite edge
            edgeDao.deleteEdge(id, id, EdgeType.TRIP_FAVORITE.value)
        }
    }
}
