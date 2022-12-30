package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object FoodIntakeTracker : Table("foodintaketracker") {
    val id = integer("id").autoIncrement().primaryKey()
    val meal = varchar("meal", 100)
    val caloriesIntake = integer("caloriesintake")
    val date = datetime("date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}