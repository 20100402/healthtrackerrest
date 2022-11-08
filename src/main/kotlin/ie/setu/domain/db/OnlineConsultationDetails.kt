package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


object OnlineConsultationDetails : Table("onlineconsultationdetails") {
    val id = integer("id").autoIncrement().primaryKey()
    val doctorName = varchar("doctorname", 100)
    val appointmentDate = datetime("appointmentdate")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}