package ie.setu.controllers

import ie.setu.domain.Activity
import ie.setu.domain.HeartRateMonitor
import ie.setu.domain.FoodTracker
import ie.setu.domain.repository.HeartRateMonitorDAO
import ie.setu.domain.repository.FoodTrackerDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*


object HeartRateMonitorController {

    private val userDao = UserDAO()
    private val heartRateMonitorDAO = HeartRateMonitorDAO()



    //--------------------------------------------------------------
    // HeartRateMonitorDAOI specifics
    //-------------------------------------------------------------

    @OpenApi(
        summary = "Get all HeartRateMonitorDetails",
        operationId = "getAllHeartRateMonitorDetails",
        tags = ["HeartRateMonitorDetails"],
        path = "/api/heartrateMonitordetails",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<HeartRateMonitor>::class)])]
    )
    fun getAllHeartRateMonitorDetails(ctx: Context) {
        val heartRateMonitorDetails = HeartRateMonitorController.heartRateMonitorDAO.getAll()
        if (heartRateMonitorDetails.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(heartRateMonitorDetails)
    }

    @OpenApi(
        summary = "Get HeartRateMonitorDetails by User ID",
        operationId = "getHeartRateMonitorDetailsByUserId",
        tags = ["HeartRateMonitor"],
        path = "/api/users/{user-id}/heartratemonitordetails",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(HeartRateMonitor::class)])]
    )
    fun getHeartRateMonitorDetailsByUserId(ctx: Context) {
        if (HeartRateMonitorController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val heartRateMonitorDetails = HeartRateMonitorController.heartRateMonitorDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (heartRateMonitorDetails.isNotEmpty()) {
                ctx.json(heartRateMonitorDetails)
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
        summary = "Get HeartRateMonitorDetails by HeartRateMonitor ID",
        operationId = "getHeartRateMonitorDetailsByHeartRateMonitorId",
        tags = ["HeartRateMonitor"],
        path = "/api/heartratemonitordetails/{heartratemonitor-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("heartratemonitor-id", Int::class, "The HeartRateMonitor ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Activity::class)])]
    )
    fun getHeartRateMonitorDetailsByHeartRateMonitorId(ctx: Context) {
        val heartRateMonitor = HeartRateMonitorController.heartRateMonitorDAO.findByHeartRateMonitorId((ctx.pathParam("heartratemonitor-id").toInt()))
        if (heartRateMonitor != null){
            ctx.json(heartRateMonitor)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add HeartRateMonitorDetails",
        operationId = "addHeartRateMonitorDetails",
        tags = ["HeartRateMonitor"],
        path = "/api/heartratemonitordetails",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("heartratemonitor-id", Int::class, "The HeartRateMonitor ID")],
        responses  = [OpenApiResponse("200")]
    )
    fun addHeartRateMonitor(ctx: Context) {
        val heartRateMonitor : HeartRateMonitor = jsonToObject(ctx.body())
        val userId = HeartRateMonitorController.userDao.findById(heartRateMonitor.userId)
        if (userId != null) {
            val heartRateMonitorId = HeartRateMonitorController.heartRateMonitorDAO.save(heartRateMonitor)
            heartRateMonitor.id = heartRateMonitorId
            ctx.json(heartRateMonitor)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete HeartRateMonitorDetails by HeartRateMonitor ID",
        operationId = "deleteHeartRateMonitorByHeartRateMonitorId",
        tags = ["HeartRateMonitor"],
        path = "/api/heartratemonitordetails/{heartratemonitor-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("heartratemonitor-id", Int::class, "The HeartRateMonitor ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteHeartRateMonitorByHeartRateMonitorId(ctx: Context){
        if (HeartRateMonitorController.heartRateMonitorDAO.deleteByHeartRateMonitorId(ctx.pathParam("heartratemonitor-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Delete HeartRateMonitorDetails by User ID",
        operationId = "deleteHeartRateMonitorByUserId",
        tags = ["User"],
        path = "/api/users/{user-id}/heartratemonitordetails",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteHeartRateMonitorByUserId(ctx: Context){
        if (HeartRateMonitorController.heartRateMonitorDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update HeartRateMonitorDetails by HeartRateMonitor ID",
        operationId = "updateHeartRateMonitor",
        tags = ["HeartRateMonitor"],
        path = "/api/heartratemonitordetails/{heartratemonitor-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("heartratemonitor-id", Int::class, "The HeartRateMonitor ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateHeartRateMonitor(ctx: Context){
        val heartRateMonitor : HeartRateMonitor = jsonToObject(ctx.body())
        if (HeartRateMonitorController.heartRateMonitorDAO.updateByHeartRateMonitorId(
                heartRateMonitorId = ctx.pathParam("heartratemonitor-id").toInt(),
                heartRateMonitorToUpdate =heartRateMonitor) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}