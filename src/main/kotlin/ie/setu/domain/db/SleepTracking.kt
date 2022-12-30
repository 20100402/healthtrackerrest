package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


object SleepTracking : Table("sleeptracking") {
    val id = integer("id").autoIncrement().primaryKey()
    val hours = double("hours")
    val date = datetime("date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}