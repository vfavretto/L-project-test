package com.itinerary.core.data.repository

import com.itinerary.core.database.dao.EdgeDao
import com.itinerary.core.database.dao.InterestMarkDao
import com.itinerary.core.database.entity.EdgeEntity
import com.itinerary.core.database.mapper.toDomain
import com.itinerary.core.database.mapper.toEntity
import com.itinerary.core.domain.model.EdgeType
import com.itinerary.core.domain.model.InterestMark
import com.itinerary.core.domain.repository.InterestMarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InterestMarkRepositoryImpl(
    private val interestMarkDao: InterestMarkDao,
    private val edgeDao: EdgeDao
) : InterestMarkRepository {

    override fun getInterestMarksByTripId(tripId: Long): Flow<List<InterestMark>> {
        return interestMarkDao.getInterestMarksByTripId(tripId).map { marks ->
            marks.map { it.toDomain() }
        }
    }

    override fun getInterestMarkById(id: Long): Flow<InterestMark?> {
        return interestMarkDao.getInterestMarkById(id).map { it?.toDomain() }
    }

    override fun getScheduledInterestMarks(tripId: Long): Flow<List<InterestMark>> {
        return interestMarkDao.getScheduledInterestMarks(tripId).map { marks ->
            marks.map { it.toDomain() }
        }
    }

    override fun searchInterestMarks(tripId: Long, query: String): Flow<List<InterestMark>> {
        return interestMarkDao.searchInterestMarks(tripId, query).map { marks ->
            marks.map { it.toDomain() }
        }
    }

    override suspend fun createInterestMark(interestMark: InterestMark): Long {
        val markId = interestMarkDao.insertInterestMark(interestMark.toEntity())
        
        // Create edge in TAO graph to link trip and interest mark
        val edge = EdgeEntity(
            id1 = interestMark.tripId,
            id2 = markId,
            type = EdgeType.TRIP_INTEREST_MARK.value,
            timestamp = System.currentTimeMillis(),
            data = null
        )
        edgeDao.insertEdge(edge)
        
        // If scheduled, create schedule edge
        if (interestMark.scheduledDate != null) {
            val scheduleEdge = EdgeEntity(
                id1 = markId,
                id2 = interestMark.tripId,
                type = EdgeType.MARK_SCHEDULED.value,
                timestamp = interestMark.scheduledDate,
                data = null
            )
            edgeDao.insertEdge(scheduleEdge)
        }
        
        return markId
    }

    override suspend fun updateInterestMark(interestMark: InterestMark) {
        val updatedMark = interestMark.copy(updatedAt = System.currentTimeMillis())
        interestMarkDao.updateInterestMark(updatedMark.toEntity())
        
        // Update schedule edge if necessary
        if (updatedMark.scheduledDate != null) {
            val existingEdge = edgeDao.getEdge(
                updatedMark.id,
                updatedMark.tripId,
                EdgeType.MARK_SCHEDULED.value
            )
            
            if (existingEdge == null) {
                // Create new schedule edge
                val scheduleEdge = EdgeEntity(
                    id1 = updatedMark.id,
                    id2 = updatedMark.tripId,
                    type = EdgeType.MARK_SCHEDULED.value,
                    timestamp = updatedMark.scheduledDate,
                    data = null
                )
                edgeDao.insertEdge(scheduleEdge)
            } else {
                // Update existing edge
                val updatedEdge = existingEdge.copy(timestamp = updatedMark.scheduledDate)
                edgeDao.insertEdge(updatedEdge)
            }
        } else {
            // Remove schedule edge if date was cleared
            edgeDao.deleteEdge(
                updatedMark.id,
                updatedMark.tripId,
                EdgeType.MARK_SCHEDULED.value
            )
        }
    }

    override suspend fun deleteInterestMark(id: Long) {
        // Get the mark to find its trip ID
        val mark = interestMarkDao.getInterestMarkById(id)
        
        // Delete all edges related to this interest mark (TAO cleanup)
        mark.collect { markEntity ->
            if (markEntity != null) {
                edgeDao.deleteEdge(markEntity.tripId, id, EdgeType.TRIP_INTEREST_MARK.value)
                edgeDao.deleteEdge(id, markEntity.tripId, EdgeType.MARK_SCHEDULED.value)
            }
        }
        
        // Delete the interest mark itself
        interestMarkDao.deleteInterestMark(id)
    }

    override suspend fun getInterestMarkCount(tripId: Long): Int {
        return interestMarkDao.getInterestMarkCount(tripId)
    }
}
