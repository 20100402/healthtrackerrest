package ie.setu.controllers

import ie.setu.domain.Activity
import ie.setu.domain.FoodTracker
import ie.setu.domain.repository.FoodTrackerDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

object FoodTrackerController {

    private val userDao = UserDAO()
    private val foodTrackerDAO = FoodTrackerDAO()



    //--------------------------------------------------------------
    // FoodTrackerDAOI specifics
    //-------------------------------------------------------------

    @OpenApi(
        summary = "Get all FoodTrackerDetails",
        operationId = "getAllFoodTrackerDetails",
        tags = ["FoodTrackerDetails"],
        path = "/api/foodtrackerdetails",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<FoodTracker>::class)])]
    )
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

    @OpenApi(
        summary = "Get FoodTrackerDetails by User ID",
        operationId = "getFoodTrackerDetailsByUserId",
        tags = ["FoodTracker"],
        path = "/api/users/{user-id}/foodtrackerdetails",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(FoodTracker::class)])]
    )
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

    @OpenApi(
        summary = "Get FoodTrackerDetails by FoodTracker ID",
        operationId = "getFoodTrackerDetailsByFoodTrackerId",
        tags = ["FoodTracker"],
        path = "/api/foodtrackerdetails/{foodtracker-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("foodtracker-id", Int::class, "The FoodTracker ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(FoodTracker::class)])]
    )
    fun getFoodTrackerDetailsByFoodTrackerId(ctx: Context) {
        val foodTracker = FoodTrackerController.foodTrackerDAO.findByFoodTrackerId((ctx.pathParam("foodtracker-id").toInt()))
        if (foodTracker != null){
            ctx.json(foodTracker)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add FoodTrackerDetails",
        operationId = "addFoodTracker",
        tags = ["FoodTracker"],
        path = "/api/foodtrackerdetails",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("foodtracker-id", Int::class, "The foodtracker ID")],
        responses  = [OpenApiResponse("200")]
    )
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

    @OpenApi(
        summary = "Delete foodtrackerdetails by foodtracker ID",
        operationId = "deleteFoodTrackerDetailsByFoodTrackerDetailsId",
        tags = ["FoodTrackerDetails"],
        path = "/api/foodtrackerdetails/{foodtracker-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("foodtracker-id", Int::class, "The FoodTracker ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteFoodTrackerDetailsByFoodTrackerDetailsId(ctx: Context){
        if (FoodTrackerController.foodTrackerDAO.deleteByFoodTrackerId(ctx.pathParam("foodtracker-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Delete FoodTrackerDetails by User ID",
        operationId = "deleteFoodTrackerDetailsByUserId",
        tags = ["User"],
        path = "/api/users/{user-id}/foodtrackerdetails",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("foodtracker-id", Int::class, "The FoodTracker ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteFoodTrackerDetailsByUserId(ctx: Context){
        if (FoodTrackerController.foodTrackerDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update FoodtrackerDetails by FoodTracker ID",
        operationId = "updateFoodTrackerDetails",
        tags = ["FoodTracker"],
        path = "/api/foodtrackerdetails/{foodtracker-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("foodtracker-id", Int::class, "The FoodTracker ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateFoodTrackerDetails(ctx: Context){
        val foodTracker : FoodTracker = jsonToObject(ctx.body())
        if (FoodTrackerController.foodTrackerDAO.updateByFoodTrackerId(
                foodTrackerId = ctx.pathParam("foodtracker-id").toInt(),
                foodTrackerToUpdate =foodTracker) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}