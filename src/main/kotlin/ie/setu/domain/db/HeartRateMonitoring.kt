package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


object HeartRateMonitoring : Table("heartratemonitoring") {
    val id = integer("id").autoIncrement().primaryKey()
    val pulse = integer("pulse")
    val date = datetime("date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}