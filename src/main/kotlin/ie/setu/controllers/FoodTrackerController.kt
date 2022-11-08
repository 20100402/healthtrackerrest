package ie.setu.controllers

import ie.setu.domain.FoodTracker
import ie.setu.domain.db.FoodTrackerDetails
import ie.setu.domain.repository.FoodTrackerDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object FoodTrackerController {

    private val userDao = UserDAO()
    private val foodTrackerDAO = FoodTrackerDAO()



    //--------------------------------------------------------------
    // FoodTrackerDAOI specifics
    //-------------------------------------------------------------

    fun getAllFoodTrackerDetails(ctx: Context) {
        val foodTrackerDetails = FoodTrackerController.foodTrackerDAO.getAll()
        if (foodTrackerDetails.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(foodTrackerDetails)
    }

    fun getFoodTrackerDetailsByUserId(ctx: Context) {
        if (FoodTrackerController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val foodTrackerDetails = FoodTrackerController.foodTrackerDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (foodTrackerDetails.isNotEmpty()) {
                ctx.json(foodTrackerDetails)
                ctx.status(200)
            }
            else{
                ctx.status(404)
            }
        }
        else{
            ctx.status(404)
        }
    }

    fun getFoodTrackerDetailsByFoodTrackerId(ctx: Context) {
        val foodTracker = FoodTrackerController.foodTrackerDAO.findByFoodTrackerId((ctx.pathParam("FoodTracker-id").toInt()))
        if (foodTracker != null){
            ctx.json(foodTracker)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addFoodTracker(ctx: Context) {
        val foodTracker : FoodTracker = jsonToObject(ctx.body())
        val userId = FoodTrackerController.userDao.findById(foodTracker.userId)
        if (userId != null) {
            val foodTrackerId = FoodTrackerController.foodTrackerDAO.save(foodTracker)
            foodTracker.id = foodTrackerId
            ctx.json(foodTracker)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    fun deleteFoodTrackerDetailsByFoodTrackerDetailsId(ctx: Context){
        if (FoodTrackerController.foodTrackerDAO.deleteByFoodTrackerId(ctx.pathParam("FoodTrackerDetails-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteFoodTrackerDetailsByUserId(ctx: Context){
        if (FoodTrackerController.foodTrackerDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateFoodTrackerDetails(ctx: Context){
        val foodTracker : FoodTracker = jsonToObject(ctx.body())
        if (FoodTrackerController.foodTrackerDAO.updateByFoodTrackerId(
                foodTrackerId = ctx.pathParam("foodTracker-id").toInt(),
                foodTrackerToUpdate =foodTracker) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}