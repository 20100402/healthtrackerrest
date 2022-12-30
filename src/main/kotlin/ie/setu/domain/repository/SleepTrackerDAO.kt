package ie.setu.domain.repository

import ie.setu.domain.SleepTracker
import ie.setu.domain.db.SleepTracking
import ie.setu.utils.mapToSleepTracker
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class SleepTrackerDAO {

    //Get all the SleepTracking in the database regardless of user id
    fun getAll(): ArrayList<SleepTracker> {
        val sleepTrackerDetailsList: ArrayList<SleepTracker> = arrayListOf()
        transaction {
            SleepTracking.selectAll().map {
                sleepTrackerDetailsList.add(mapToSleepTracker(it)) }
        }
        return sleepTrackerDetailsList
    }

    //Find a specific SleepTracker by SleepTracker id
    fun findBySleepTrackerId(id: Int): SleepTracker?{
        return transaction {
            SleepTracking
                .select() { SleepTracking.id eq id}
                .map{ mapToSleepTracker(it) }
                .firstOrNull()
        }
    }

    //Find all SleepTracking for a specific user id
    fun findByUserId(userId: Int): List<SleepTracker>{
        return transaction {
            SleepTracking
                .select { SleepTracking.userId eq userId}
                .map { mapToSleepTracker(it) }
        }
    }

    //Save an SleepTracker to the database
    fun save(sleepTracker: SleepTracker): Int {
        return transaction {
            SleepTracking.insert {
                it[hours] = sleepTracker.hours
                it[date] = sleepTracker.date
                it[userId] = sleepTracker.userId
            }
        } get SleepTracking.id
    }

    fun updateBySleepTrackerId(sleepTrackerId: Int, sleepTrackerToUpdate: SleepTracker) : Int{
        return transaction {
            SleepTracking.update ({
                SleepTracking.id eq sleepTrackerId}) {
                it[hours] = sleepTrackerToUpdate.hours
                it[date] = sleepTrackerToUpdate.date
                it[userId] = sleepTrackerToUpdate.userId
            }
        }
    }

    fun deleteBySleepTrackerId (SleepTrackerId: Int): Int{
        return transaction{
            SleepTracking.deleteWhere { SleepTracking.id eq SleepTrackerId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            SleepTracking.deleteWhere { SleepTracking.userId eq userId }
        }
    }
}