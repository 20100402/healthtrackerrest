package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


object HeartRateMonitorDetails : Table("heartratemonitordetails") {
    val id = integer("id").autoIncrement().primaryKey()
    val heartRate = integer("heartrate")
    val pulse = double("pulse")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}