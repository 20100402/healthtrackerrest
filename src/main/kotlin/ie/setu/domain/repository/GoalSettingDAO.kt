package ie.setu.domain.repository

import ie.setu.domain.GoalSetting
import ie.setu.domain.db.GoalSetters
import ie.setu.utils.mapToGoalSetting
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


class GoalSettingDAO {

    //Get all the GoalSetters in the database regardless of user id
    fun getAll(): ArrayList<GoalSetting> {
        val goalSettingDetailsList: ArrayList<GoalSetting> = arrayListOf()
        transaction {
            GoalSetters.selectAll().map {
                goalSettingDetailsList.add(mapToGoalSetting(it)) }
        }
        return goalSettingDetailsList
    }

    //Find a specific GoalSetting by GoalSetting id
    fun findByGoalSettingId(id: Int): GoalSetting?{
        return transaction {
            GoalSetters
                .select() { GoalSetters.id eq id}
                .map{ mapToGoalSetting(it) }
                .firstOrNull()
        }
    }

    //Find all GoalSetters for a specific user id
    fun findByUserId(userId: Int): List<GoalSetting>{
        return transaction {
            GoalSetters
                .select { GoalSetters.userId eq userId}
                .map { mapToGoalSetting(it) }
        }
    }

    //Save an GoalSetting to the database
    fun save(goalSetting: GoalSetting): Int {
        return transaction {
            GoalSetters.insert {
                it[month] = goalSetting.month
                it[bodyFatPercentage] = goalSetting.bodyFatPercentage
                it[kilosReducedPerMonth] = goalSetting.kilosReducedPerMonth
                it[date] = goalSetting.date
                it[userId] = goalSetting.userId
            }
        } get GoalSetters.id
    }

    fun updateByGoalSettingId(goalSettingId: Int, goalSettingToUpdate: GoalSetting) : Int{
        return transaction {
            GoalSetters.update ({
                GoalSetters.id eq goalSettingId}) {
                it[month] = goalSettingToUpdate.month
                it[bodyFatPercentage] = goalSettingToUpdate.bodyFatPercentage
                it[kilosReducedPerMonth] = goalSettingToUpdate.kilosReducedPerMonth
                it[date] = goalSettingToUpdate.date
                it[userId] = goalSettingToUpdate.userId
            }
        }
    }

    fun deleteByGoalSettingId (GoalSettingId: Int): Int{
        return transaction{
            GoalSetters.deleteWhere { GoalSetters.id eq GoalSettingId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            GoalSetters.deleteWhere { GoalSetters.userId eq userId }
        }
    }
}