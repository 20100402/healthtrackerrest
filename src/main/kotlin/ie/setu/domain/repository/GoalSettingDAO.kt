package ie.setu.domain.repository

import ie.setu.domain.GoalSetting
import ie.setu.domain.db.GoalSettingDetails
import ie.setu.utils.mapToGoalSetting
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


class GoalSettingDAO {

    //Get all the GoalSettingDetails in the database regardless of user id
    fun getAll(): ArrayList<GoalSetting> {
        val goalSettingDetailsList: ArrayList<GoalSetting> = arrayListOf()
        transaction {
            GoalSettingDetails.selectAll().map {
                goalSettingDetailsList.add(mapToGoalSetting(it)) }
        }
        return goalSettingDetailsList
    }

    //Find a specific GoalSetting by GoalSetting id
    fun findByGoalSettingId(id: Int): GoalSetting?{
        return transaction {
            GoalSettingDetails
                .select() { GoalSettingDetails.id eq id}
                .map{ mapToGoalSetting(it) }
                .firstOrNull()
        }
    }

    //Find all GoalSettingDetails for a specific user id
    fun findByUserId(userId: Int): List<GoalSetting>{
        return transaction {
            GoalSettingDetails
                .select { GoalSettingDetails.userId eq userId}
                .map { mapToGoalSetting(it) }
        }
    }

    //Save an GoalSetting to the database
    fun save(goalSetting: GoalSetting): Int {
        return transaction {
            GoalSettingDetails.insert {
                it[month] = goalSetting.month
                it[bodyFatPercentage] = goalSetting.bodyFatPercentage
                it[kilosReducedPerMonth] = goalSetting.kilosReducedPerMonth
                it[userId] = goalSetting.userId
            }
        } get GoalSettingDetails.id
    }

    fun updateByGoalSettingId(goalSettingId: Int, goalSettingToUpdate: GoalSetting) : Int{
        return transaction {
            GoalSettingDetails.update ({
                GoalSettingDetails.id eq goalSettingId}) {
                it[month] = goalSettingToUpdate.month
                it[bodyFatPercentage] = goalSettingToUpdate.bodyFatPercentage
                it[kilosReducedPerMonth] = goalSettingToUpdate.kilosReducedPerMonth
                it[userId] = goalSettingToUpdate.userId
            }
        }
    }

    fun deleteByGoalSettingId (GoalSettingId: Int): Int{
        return transaction{
            GoalSettingDetails.deleteWhere { GoalSettingDetails.id eq GoalSettingId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            GoalSettingDetails.deleteWhere { GoalSettingDetails.userId eq userId }
        }
    }
}