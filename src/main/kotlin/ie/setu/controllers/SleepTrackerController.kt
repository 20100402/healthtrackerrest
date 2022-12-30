package ie.setu.controllers

import ie.setu.domain.Activity
import ie.setu.domain.SleepTracker
import ie.setu.domain.repository.SleepTrackerDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

object SleepTrackerController {

    private val userDao = UserDAO()
    private val sleepTrackerDAO = SleepTrackerDAO()



    //--------------------------------------------------------------
    // SleepTrackerDAOI specifics
    //-------------------------------------------------------------

    @OpenApi(
        summary = "Get all SleepTrackerDetails",
        operationId = "getAllSleepTrackerDetails",
        tags = ["SleepTrackerDetails"],
        path = "/api/sleeptrackerdetails",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<SleepTracker>::class)])]
    )
    fun getAllSleepTrackerDetails(ctx: Context) {
        val sleepTrackerDetails = SleepTrackerController.sleepTrackerDAO.getAll()
        if (sleepTrackerDetails.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(sleepTrackerDetails)
    }

    @OpenApi(
        summary = "Get SleepTrackerDetails by User ID",
        operationId = "getSleepTrackerDetailsByUserId",
        tags = ["SleepTrackerDetails"],
        path = "/api/users/{user-id}/sleeptrackerdetails",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(SleepTracker::class)])]
    )
    fun getSleepTrackerDetailsByUserId(ctx: Context) {
        if (SleepTrackerController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val sleepTrackerDetails = SleepTrackerController.sleepTrackerDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (sleepTrackerDetails.isNotEmpty()) {
                ctx.json(sleepTrackerDetails)
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
        summary = "Get SleepTrackerDetails by SleepTracker ID",
        operationId = "getSleepTrackerDetailsBySleepTrackerId",
        tags = ["SleepTracker"],
        path = "/api/sleeptrackerdetails/{sleeptracker-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("sleeptracker-id", Int::class, "The SleepTracker ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(SleepTracker::class)])]
    )
    fun getSleepTrackerDetailsBySleepTrackerId(ctx: Context) {
        val sleepTracker = SleepTrackerController.sleepTrackerDAO.findBySleepTrackerId((ctx.pathParam("sleeptracker-id").toInt()))
        if (sleepTracker != null){
            ctx.json(sleepTracker)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add SleepTracker",
        operationId = "addSleepTracker",
        tags = ["SleepTracker"],
        path = "/api/sleeptrackerdetails",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("sleeptracker-id", Int::class, "The SleepTracker ID")],
        responses  = [OpenApiResponse("200")]
    )
    fun addSleepTracker(ctx: Context) {
        val sleepTracker : SleepTracker = jsonToObject(ctx.body())
        val userId = SleepTrackerController.userDao.findById(sleepTracker.userId)
        if (userId != null) {
            val sleepTrackerId = SleepTrackerController.sleepTrackerDAO.save(sleepTracker)
            sleepTracker.id = sleepTrackerId
            ctx.json(sleepTracker)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete SleepTrackerDetails by SleepTracker ID",
        operationId = "deleteSleepTrackerBySleepTrackerId",
        tags = ["SleepTracker"],
        path = "/api/sleeptrackerdetails/{sleeptracker-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("sleeptracker-id", Int::class, "The SleepTracker ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteSleepTrackerBySleepTrackerId(ctx: Context){
        if (SleepTrackerController.sleepTrackerDAO.deleteBySleepTrackerId(ctx.pathParam("sleeptracker-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Delete SleepTracker by User ID",
        operationId = "deleteSleepTrackerByUserId",
        tags = ["User"],
        path = "/api/users/{user-id}/sleeptrackerdetails",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteSleepTrackerByUserId(ctx: Context){
        if (SleepTrackerController.sleepTrackerDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
    @OpenApi(
        summary = "Update SleepTrackerDetails by SleepTracker ID",
        operationId = "updateSleepTracker",
        tags = ["SleepTracker"],
        path = "/api/sleeptrackerdetails/{sleeptracker-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("sleeptracker-id", Int::class, "The SleepTracker ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateSleepTracker(ctx: Context){
        val sleepTracker : SleepTracker = jsonToObject(ctx.body())
        if (SleepTrackerController.sleepTrackerDAO.updateBySleepTrackerId(
                sleepTrackerId = ctx.pathParam("sleeptracker-id").toInt(),
                sleepTrackerToUpdate =sleepTracker) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}