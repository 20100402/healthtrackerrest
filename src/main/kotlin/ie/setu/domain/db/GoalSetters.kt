package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


object GoalSetters : Table("goalsetters") {
    val id = integer("id").autoIncrement().primaryKey()
    val month = varchar("month", 100)
    val bodyFatPercentage = double("bodyfatpercentage")
    val kilosReducedPerMonth = integer("kilosreducedpermonth")
    val date = datetime("date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}