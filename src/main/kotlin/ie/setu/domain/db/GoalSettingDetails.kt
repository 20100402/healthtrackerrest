package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


object GoalSettingDetails : Table("goalsettingdetails") {
    val id = integer("id").autoIncrement().primaryKey()
    val month = varchar("month", 100)
    val bodyFatPercentage = double("bodyfatpercentage")
    val kilosReducedPerMonth = integer("kilosreducedpermonth")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}