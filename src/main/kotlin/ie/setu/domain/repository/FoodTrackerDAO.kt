package ie.setu.domain.repository

import ie.setu.domain.FoodTracker
import ie.setu.domain.db.Activities
import ie.setu.domain.db.FoodTrackerDetails
import ie.setu.utils.mapToFoodTracker
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class FoodTrackerDAO {

    //Get all the FoodTrackerDetails in the database regardless of user id
    fun getAll(): ArrayList<FoodTracker> {
        val foodTrackerDetailsList: ArrayList<FoodTracker> = arrayListOf()
        transaction {
            FoodTrackerDetails.selectAll().map {
                foodTrackerDetailsList.add(mapToFoodTracker(it)) }
        }
        return foodTrackerDetailsList
    }

    //Find a specific FoodTracker by FoodTracker id
    fun findByFoodTrackerId(id: Int): FoodTracker?{
        return transaction {
            FoodTrackerDetails
                .select() { FoodTrackerDetails.id eq id}
                .map{ mapToFoodTracker(it) }
                .firstOrNull()
        }
    }

    //Find all FoodTrackerDetails for a specific user id
    fun findByUserId(userId: Int): List<FoodTracker>{
        return transaction {
            FoodTrackerDetails
                .select { FoodTrackerDetails.userId eq userId}
                .map { mapToFoodTracker(it) }
        }
    }

    //Save an FoodTracker to the database
    fun save(foodTracker: FoodTracker): Int {
        return transaction {
            FoodTrackerDetails.insert {
                it[meal] = foodTracker.meal
                it[caloriesIntake] = foodTracker.caloriesIntake
                it[userId] = foodTracker.userId
            }
        } get FoodTrackerDetails.id
    }

    fun updateByFoodTrackerId(foodTrackerId: Int, foodTrackerToUpdate: FoodTracker) : Int{
        return transaction {
            FoodTrackerDetails.update ({
                FoodTrackerDetails.id eq foodTrackerId}) {
                it[meal] = foodTrackerToUpdate.meal
                it[caloriesIntake] = foodTrackerToUpdate.caloriesIntake
                it[userId] = foodTrackerToUpdate.userId
            }
        }
    }

    fun deleteByFoodTrackerId (FoodTrackerId: Int): Int{
        return transaction{
            FoodTrackerDetails.deleteWhere { FoodTrackerDetails.id eq FoodTrackerId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            FoodTrackerDetails.deleteWhere { FoodTrackerDetails.userId eq userId }
        }
    }
}