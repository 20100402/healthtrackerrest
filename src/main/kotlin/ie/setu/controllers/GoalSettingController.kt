package ie.setu.controllers

import ie.setu.domain.Activity
import ie.setu.domain.GoalSetting
import ie.setu.domain.repository.GoalSettingDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*


object GoalSettingController {

    private val userDao = UserDAO()
    private val goalSettingDAO = GoalSettingDAO()



    //--------------------------------------------------------------
    // GoalSettingDAOI specifics
    //-------------------------------------------------------------

    @OpenApi(
        summary = "Get all GoalSettingDetails",
        operationId = "getAllGoalSettingDetails",
        tags = ["GoalSettingDetails"],
        path = "/api/goalsettingdetails",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<GoalSetting>::class)])]
    )
    fun getAllGoalSettingDetails(ctx: Context) {
        val goalSettingDetails = GoalSettingController.goalSettingDAO.getAll()
        if (goalSettingDetails.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(goalSettingDetails)
    }

    @OpenApi(
        summary = "Get GoalSettingDetails by User ID",
        operationId = "getGoalSettingDetailsByUserId",
        tags = ["GoalSetting"],
        path = "/api/users/{user-id}/goalsettingdetails",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("goalsetting-id", Int::class, "The Goal Setting ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(GoalSetting::class)])]
    )
    fun getGoalSettingDetailsByUserId(ctx: Context) {
        if (GoalSettingController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val goalSettingDetails = GoalSettingController.goalSettingDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (goalSettingDetails.isNotEmpty()) {
                ctx.json(goalSettingDetails)
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
        summary = "Get GoalSettingDetails by GoalSetting ID",
        operationId = "getGoalSettingDetailsByGoalSettingId",
        tags = ["GoalSetting"],
        path = "/api/goalsettingdetails/{goalsetting-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("goalsetting-id", Int::class, "The GoalSetting ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(GoalSetting::class)])]
    )
    fun getGoalSettingDetailsByGoalSettingId(ctx: Context) {
        val goalSetting = GoalSettingController.goalSettingDAO.findByGoalSettingId((ctx.pathParam("goalsetting-id").toInt()))
        if (goalSetting != null){
            ctx.json(goalSetting)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add GoalSettingDetails",
        operationId = "addGoalSettingDetails",
        tags = ["GoalSetting"],
        path = "/api/goalsettingdetails",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("goalsetting-id", Int::class, "The GoalSetting ID")],
        responses  = [OpenApiResponse("200")]
    )
    fun addGoalSetting(ctx: Context) {
        val goalSetting : GoalSetting = jsonToObject(ctx.body())
        val userId = GoalSettingController.userDao.findById(goalSetting.userId)
        if (userId != null) {
            val goalSettingId = GoalSettingController.goalSettingDAO.save(goalSetting)
            goalSetting.id = goalSettingId
            ctx.json(goalSetting)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete GoalSettingDetails by GoalSetting ID",
        operationId = "deleteGoalSettingByGoalSettingId",
        tags = ["GoalSetting"],
        path = "/api/goalsettingdetails/{goalsetting-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("goalsetting-id", Int::class, "The GoalSetting ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteGoalSettingByGoalSettingId(ctx: Context){
        if (GoalSettingController.goalSettingDAO.deleteByGoalSettingId(ctx.pathParam("goalsetting-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Delete GoalSettingDetails by GoalSetting ID",
        operationId = "deleteGoalSettingDetailsByUserId",
        tags = ["User"],
        path = "/api/users/{user-id}/goalsettingdetails",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteGoalSettingByUserId(ctx: Context){
        if (GoalSettingController.goalSettingDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
    @OpenApi(
        summary = "Update GoalSettingDetails by GoalSetting ID",
        operationId = "updateGoalSetting",
        tags = ["GoalSetting"],
        path = "/api/goalsettingdetails/{goalsetting-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("goalsetting-id", Int::class, "The GoalSetting ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateGoalSetting(ctx: Context){
        val goalSetting : GoalSetting = jsonToObject(ctx.body())
        if (GoalSettingController.goalSettingDAO.updateByGoalSettingId(
                goalSettingId = ctx.pathParam("goalsetting-id").toInt(),
                goalSettingToUpdate = goalSetting) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}