package ie.setu.domain.repository

import ie.setu.domain.HeartRateMonitor
import ie.setu.domain.db.HeartRateMonitoring
import ie.setu.utils.mapToHeartRateMonitor
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class HeartRateMonitorDAO {

    //Get all the HeartRateMonitoring in the database regardless of user id
    fun getAll(): ArrayList<HeartRateMonitor> {
        val heartRateMonitorDetailsList: ArrayList<HeartRateMonitor> = arrayListOf()
        transaction {
            HeartRateMonitoring.selectAll().map {
                heartRateMonitorDetailsList.add(mapToHeartRateMonitor(it)) }
        }
        return heartRateMonitorDetailsList
    }

    //Find a specific HeartRateMonitor by HeartRateMonitor id
    fun findByHeartRateMonitorId(id: Int): HeartRateMonitor?{
        return transaction {
            HeartRateMonitoring
                .select() { HeartRateMonitoring.id eq id}
                .map{ mapToHeartRateMonitor(it) }
                .firstOrNull()
        }
    }

    //Find all HeartRateMonitoring for a specific user id
    fun findByUserId(userId: Int): List<HeartRateMonitor>{
        return transaction {
            HeartRateMonitoring
                .select { HeartRateMonitoring.userId eq userId}
                .map { mapToHeartRateMonitor(it) }
        }
    }

    //Save an HeartRateMonitor to the database
    fun save(heartRateMonitor: HeartRateMonitor): Int {
        return transaction {
            HeartRateMonitoring.insert {
                it[pulse] = heartRateMonitor.pulse
                it[date] = heartRateMonitor.date
                it[userId] = heartRateMonitor.userId
            }
        } get HeartRateMonitoring.id
    }

    fun updateByHeartRateMonitorId(heartRateMonitorId: Int, heartRateMonitorToUpdate: HeartRateMonitor) : Int{
        return transaction {
            HeartRateMonitoring.update ({
                HeartRateMonitoring.id eq heartRateMonitorId}) {
                it[pulse] = heartRateMonitorToUpdate.pulse
                it[date] = heartRateMonitorToUpdate.date
                it[userId] = heartRateMonitorToUpdate.userId
            }
        }
    }

    fun deleteByHeartRateMonitorId (HeartRateMonitorId: Int): Int{
        return transaction{
            HeartRateMonitoring.deleteWhere { HeartRateMonitoring.id eq HeartRateMonitorId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            HeartRateMonitoring.deleteWhere { HeartRateMonitoring.userId eq userId }
        }
    }
}