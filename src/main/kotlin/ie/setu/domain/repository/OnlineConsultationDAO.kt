package ie.setu.domain.repository

import ie.setu.domain.OnlineConsultation
import ie.setu.domain.db.OnlineConsultations
import ie.setu.utils.mapToOnlineConsultation
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class OnlineConsultationDAO {

    //Get all the OnlineConsultations in the database regardless of user id
    fun getAll(): ArrayList<OnlineConsultation> {
        val onlineConsultationDetailsList: ArrayList<OnlineConsultation> = arrayListOf()
        transaction {
            OnlineConsultations.selectAll().map {
                onlineConsultationDetailsList.add(mapToOnlineConsultation(it)) }
        }
        return onlineConsultationDetailsList
    }

    //Find a specific OnlineConsultation by OnlineConsultation id
    fun findByOnlineConsultationId(id: Int): OnlineConsultation?{
        return transaction {
            OnlineConsultations
                .select() { OnlineConsultations.id eq id}
                .map{ mapToOnlineConsultation(it) }
                .firstOrNull()
        }
    }

    //Find all OnlineConsultations for a specific user id
    fun findByUserId(userId: Int): List<OnlineConsultation>{
        return transaction {
            OnlineConsultations
                .select { OnlineConsultations.userId eq userId}
                .map { mapToOnlineConsultation(it) }
        }
    }

    //Save an OnlineConsultation to the database
    fun save(onlineConsultation: OnlineConsultation): Int {
        return transaction {
            OnlineConsultations.insert {
                it[doctorName] = onlineConsultation.doctorName
                it[appointmentDate] = onlineConsultation.appointmentDate
                it[remarks] = onlineConsultation.remarks
                it[userId] = onlineConsultation.userId
            }
        } get OnlineConsultations.id
    }

    fun updateByOnlineConsultationId(onlineConsultationId: Int, onlineConsultationToUpdate: OnlineConsultation) : Int{
        return transaction {
            OnlineConsultations.update ({
                OnlineConsultations.id eq onlineConsultationId}) {
                it[doctorName] = onlineConsultationToUpdate.doctorName
                it[appointmentDate] = onlineConsultationToUpdate.appointmentDate
                it[remarks] = onlineConsultationToUpdate.remarks
                it[userId] = onlineConsultationToUpdate.userId
            }
        }
    }

    fun deleteByOnlineConsultationId (OnlineConsultationId: Int): Int{
        return transaction{
            OnlineConsultations.deleteWhere { OnlineConsultations.id eq OnlineConsultationId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            OnlineConsultations.deleteWhere { OnlineConsultations.userId eq userId }
        }
    }
}