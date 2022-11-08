package ie.setu.domain.repository

import ie.setu.domain.SleepTracker
import ie.setu.domain.db.SleepTrackerDetails
import ie.setu.utils.mapToSleepTracker
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class SleepTrackerDAO {

    //Get all the SleepTrackerDetails in the database regardless of user id
    fun getAll(): ArrayList<SleepTracker> {
        val sleepTrackerDetailsList: ArrayList<SleepTracker> = arrayListOf()
        transaction {
            SleepTrackerDetails.selectAll().map {
                sleepTrackerDetailsList.add(mapToSleepTracker(it)) }
        }
        return sleepTrackerDetailsList
    }

    //Find a specific SleepTracker by SleepTracker id
    fun findBySleepTrackerId(id: Int): SleepTracker?{
        return transaction {
            SleepTrackerDetails
                .select() { SleepTrackerDetails.id eq id}
                .map{ mapToSleepTracker(it) }
                .firstOrNull()
        }
    }

    //Find all SleepTrackerDetails for a specific user id
    fun findByUserId(userId: Int): List<SleepTracker>{
        return transaction {
            SleepTrackerDetails
                .select { SleepTrackerDetails.userId eq userId}
                .map { mapToSleepTracker(it) }
        }
    }

    //Save an SleepTracker to the database
    fun save(sleepTracker: SleepTracker): Int {
        return transaction {
            SleepTrackerDetails.insert {
                it[hoursPerDay] = sleepTracker.hoursPerDay
                it[totalHoursPerWeek] = sleepTracker.totalHoursPerWeek
                it[userId] = sleepTracker.userId
            }
        } get SleepTrackerDetails.id
    }

    fun updateBySleepTrackerId(sleepTrackerId: Int, sleepTrackerToUpdate: SleepTracker) : Int{
        return transaction {
            SleepTrackerDetails.update ({
                SleepTrackerDetails.id eq sleepTrackerId}) {
                it[hoursPerDay] = sleepTrackerToUpdate.hoursPerDay
                it[totalHoursPerWeek] = sleepTrackerToUpdate.totalHoursPerWeek
                it[userId] = sleepTrackerToUpdate.userId
            }
        }
    }

    fun deleteBySleepTrackerId (SleepTrackerId: Int): Int{
        return transaction{
            SleepTrackerDetails.deleteWhere { SleepTrackerDetails.id eq SleepTrackerId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            SleepTrackerDetails.deleteWhere { SleepTrackerDetails.userId eq userId }
        }
    }
}