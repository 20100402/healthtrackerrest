package ie.setu.domain.repository

import ie.setu.domain.HeartRateMonitor
import ie.setu.domain.db.HeartRateMonitorDetails
import ie.setu.utils.mapToHeartRateMonitor
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class HeartRateMonitorDAO {

    //Get all the HeartRateMonitorDetails in the database regardless of user id
    fun getAll(): ArrayList<HeartRateMonitor> {
        val heartRateMonitorDetailsList: ArrayList<HeartRateMonitor> = arrayListOf()
        transaction {
            HeartRateMonitorDetails.selectAll().map {
                heartRateMonitorDetailsList.add(mapToHeartRateMonitor(it)) }
        }
        return heartRateMonitorDetailsList
    }

    //Find a specific HeartRateMonitor by HeartRateMonitor id
    fun findByHeartRateMonitorId(id: Int): HeartRateMonitor?{
        return transaction {
            HeartRateMonitorDetails
                .select() { HeartRateMonitorDetails.id eq id}
                .map{ mapToHeartRateMonitor(it) }
                .firstOrNull()
        }
    }

    //Find all HeartRateMonitorDetails for a specific user id
    fun findByUserId(userId: Int): List<HeartRateMonitor>{
        return transaction {
            HeartRateMonitorDetails
                .select { HeartRateMonitorDetails.userId eq userId}
                .map { mapToHeartRateMonitor(it) }
        }
    }

    //Save an HeartRateMonitor to the database
    fun save(heartRateMonitor: HeartRateMonitor): Int {
        return transaction {
            HeartRateMonitorDetails.insert {
                it[heartRate] = heartRateMonitor.heartRate
                it[pulse] = heartRateMonitor.pulse
                it[userId] = heartRateMonitor.userId
            }
        } get HeartRateMonitorDetails.id
    }

    fun updateByHeartRateMonitorId(heartRateMonitorId: Int, heartRateMonitorToUpdate: HeartRateMonitor) : Int{
        return transaction {
            HeartRateMonitorDetails.update ({
                HeartRateMonitorDetails.id eq heartRateMonitorId}) {
                it[heartRate] = heartRateMonitorToUpdate.heartRate
                it[pulse] = heartRateMonitorToUpdate.pulse
                it[userId] = heartRateMonitorToUpdate.userId
            }
        }
    }

    fun deleteByHeartRateMonitorId (HeartRateMonitorId: Int): Int{
        return transaction{
            HeartRateMonitorDetails.deleteWhere { HeartRateMonitorDetails.id eq HeartRateMonitorId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            HeartRateMonitorDetails.deleteWhere { HeartRateMonitorDetails.userId eq userId }
        }
    }
}