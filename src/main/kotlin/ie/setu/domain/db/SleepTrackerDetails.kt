package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


object SleepTrackerDetails : Table("sleeptrackerdetails") {
    val id = integer("id").autoIncrement().primaryKey()
    val totalHoursPerWeek = double("totalhoursperweek")
    val hoursPerDay = double("hoursperday")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}