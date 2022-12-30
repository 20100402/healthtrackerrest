package ie.setu.domain.repository

import ie.setu.domain.FoodTracker
import ie.setu.domain.db.FoodIntakeTracker
import ie.setu.utils.mapToFoodTracker
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class FoodTrackerDAO {

    //Get all the FoodTrackerDetails in the database regardless of user id
    fun getAll(): ArrayList<FoodTracker> {
        val foodTrackerDetailsList: ArrayList<FoodTracker> = arrayListOf()
        transaction {
            FoodIntakeTracker.selectAll().map {
                foodTrackerDetailsList.add(mapToFoodTracker(it)) }
        }
        return foodTrackerDetailsList
    }

    //Find a specific FoodTracker by FoodTracker id
    fun findByFoodTrackerId(id: Int): FoodTracker?{
        return transaction {
            FoodIntakeTracker
                .select() { FoodIntakeTracker.id eq id}
                .map{ mapToFoodTracker(it) }
                .firstOrNull()
        }
    }

    //Find all FoodTrackerDetails for a specific user id
    fun findByUserId(userId: Int): List<FoodTracker>{
        return transaction {
            FoodIntakeTracker
                .select { FoodIntakeTracker.userId eq userId}
                .map { mapToFoodTracker(it) }
        }
    }

    //Save an FoodTracker to the database
    fun save(foodTracker: FoodTracker): Int {
        return transaction {
            FoodIntakeTracker.insert {
                it[meal] = foodTracker.meal
                it[caloriesIntake] = foodTracker.caloriesIntake
                it[date] = foodTracker.date
                it[userId] = foodTracker.userId
            }
        } get FoodIntakeTracker.id
    }

    fun updateByFoodTrackerId(foodTrackerId: Int, foodTrackerToUpdate: FoodTracker) : Int{
        return transaction {
            FoodIntakeTracker.update ({
                FoodIntakeTracker.id eq foodTrackerId}) {
                it[meal] = foodTrackerToUpdate.meal
                it[caloriesIntake] = foodTrackerToUpdate.caloriesIntake
                it[date] = foodTrackerToUpdate.date
                it[userId] = foodTrackerToUpdate.userId
            }
        }
    }

    fun deleteByFoodTrackerId (FoodTrackerId: Int): Int{
        return transaction{
            FoodIntakeTracker.deleteWhere { FoodIntakeTracker.id eq FoodTrackerId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            FoodIntakeTracker.deleteWhere { FoodIntakeTracker.userId eq userId }
        }
    }
}