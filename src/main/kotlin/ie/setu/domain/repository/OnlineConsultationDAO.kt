package ie.setu.domain.repository

import ie.setu.domain.OnlineConsultation
import ie.setu.domain.db.OnlineConsultationDetails
import ie.setu.utils.mapToOnlineConsultation
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class OnlineConsultationDAO {

    //Get all the OnlineConsultationDetails in the database regardless of user id
    fun getAll(): ArrayList<OnlineConsultation> {
        val onlineConsultationDetailsList: ArrayList<OnlineConsultation> = arrayListOf()
        transaction {
            OnlineConsultationDetails.selectAll().map {
                onlineConsultationDetailsList.add(mapToOnlineConsultation(it)) }
        }
        return onlineConsultationDetailsList
    }

    //Find a specific OnlineConsultation by OnlineConsultation id
    fun findByOnlineConsultationId(id: Int): OnlineConsultation?{
        return transaction {
            OnlineConsultationDetails
                .select() { OnlineConsultationDetails.id eq id}
                .map{ mapToOnlineConsultation(it) }
                .firstOrNull()
        }
    }

    //Find all OnlineConsultationDetails for a specific user id
    fun findByUserId(userId: Int): List<OnlineConsultation>{
        return transaction {
            OnlineConsultationDetails
                .select { OnlineConsultationDetails.userId eq userId}
                .map { mapToOnlineConsultation(it) }
        }
    }

    //Save an OnlineConsultation to the database
    fun save(onlineConsultation: OnlineConsultation): Int {
        return transaction {
            OnlineConsultationDetails.insert {
                it[doctorName] = onlineConsultation.doctorName
                it[appointmentDate] = onlineConsultation.appointmentDate
                it[userId] = onlineConsultation.userId
            }
        } get OnlineConsultationDetails.id
    }

    fun updateByOnlineConsultationId(onlineConsultationId: Int, onlineConsultationToUpdate: OnlineConsultation) : Int{
        return transaction {
            OnlineConsultationDetails.update ({
                OnlineConsultationDetails.id eq onlineConsultationId}) {
                it[doctorName] = onlineConsultationToUpdate.doctorName
                it[appointmentDate] = onlineConsultationToUpdate.appointmentDate
                it[userId] = onlineConsultationToUpdate.userId
            }
        }
    }

    fun deleteByOnlineConsultationId (OnlineConsultationId: Int): Int{
        return transaction{
            OnlineConsultationDetails.deleteWhere { OnlineConsultationDetails.id eq OnlineConsultationId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            OnlineConsultationDetails.deleteWhere { OnlineConsultationDetails.userId eq userId }
        }
    }
}