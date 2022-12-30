package ie.setu.controllers

import ie.setu.domain.Activity
import ie.setu.domain.OnlineConsultation
import ie.setu.domain.repository.OnlineConsultationDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

object OnlineConsultationController {

    private val userDao = UserDAO()
    private val onlineConsultationDAO = OnlineConsultationDAO()



    //--------------------------------------------------------------
    // OnlineConsultationDAOI specifics
    //-------------------------------------------------------------
    @OpenApi(
        summary = "Get all OnlineConsultationDetails",
        operationId = "getAllOnlineConsultationDetails",
        tags = ["OnlineConsultationDetails"],
        path = "/api/onlineconsultationdetails",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<OnlineConsultation>::class)])]
    )
    fun getAllOnlineConsultationDetails(ctx: Context) {
        val onlineConsultationDetails = OnlineConsultationController.onlineConsultationDAO.getAll()
        if (onlineConsultationDetails.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(onlineConsultationDetails)
    }

    @OpenApi(
        summary = "Get OnlineConsultationDetails by User ID",
        operationId = "getOnlineConsultationDetailsByUserId",
        tags = ["OnlineConsultation"],
        path = "/api/users/{user-id}/onlineconsultationdetails",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(OnlineConsultation::class)])]
    )
    fun getOnlineConsultationDetailsByUserId(ctx: Context) {
        if (OnlineConsultationController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val onlineConsultationDetails = OnlineConsultationController.onlineConsultationDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (onlineConsultationDetails.isNotEmpty()) {
                ctx.json(onlineConsultationDetails)
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
        summary = "Get OnlineConsultationDetails by OnlineConsultation ID",
        operationId = "getOnlineConsultationDetailsByOnlineConsultationId",
        tags = ["OnlineConsultation"],
        path = "/api/onlineconsultationdetails/{onlineconsultation-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("onlineconsultation-id", Int::class, "The OnlineConsultation ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(OnlineConsultation::class)])]
    )
    fun getOnlineConsultationDetailsByOnlineConsultationId(ctx: Context) {
        val onlineConsultation = OnlineConsultationController.onlineConsultationDAO.findByOnlineConsultationId((ctx.pathParam("onlineconsultation-id").toInt()))
        if (onlineConsultation != null){
            ctx.json(onlineConsultation)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add OnlineConsultationDetails",
        operationId = "addOnlineConsultation",
        tags = ["OnlineConsultation"],
        path = "/api/onlineconsultationdetails",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("onlineconsultationdetails-id", Int::class, "The OnlineConsultation ID")],
        responses  = [OpenApiResponse("200")]
    )
    fun addOnlineConsultation(ctx: Context) {
        val onlineConsultation : OnlineConsultation = jsonToObject(ctx.body())
        val userId = OnlineConsultationController.userDao.findById(onlineConsultation.userId)
        if (userId != null) {
            val onlineConsultationId = OnlineConsultationController.onlineConsultationDAO.save(onlineConsultation)
            onlineConsultation.id = onlineConsultationId
            ctx.json(onlineConsultation)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete OnlineConsultationDetails by OnlineConsultation ID",
        operationId = "deleteOnlineConsultationByOnlineConsultationId",
        tags = ["OnlineConsultation"],
        path = "/api/onlineconsultationdetails/{onlineconsultation-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("onlineconsultation-id", Int::class, "The OnlineConsultation ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteOnlineConsultationByOnlineConsultationId(ctx: Context){
        if (OnlineConsultationController.onlineConsultationDAO.deleteByOnlineConsultationId(ctx.pathParam("onlineconsultation-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Delete OnlineConsultationDetails by User ID",
        operationId = "deleteOnlineConsultationByUserId",
        tags = ["User"],
        path = "/api/users/{user-id}/onlineconsultationdetails",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteOnlineConsultationByUserId(ctx: Context){
        if (OnlineConsultationController.onlineConsultationDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update OnlineConsultationDetails by OnlineConsultation ID",
        operationId = "updateOnlineConsultation",
        tags = ["OnlineConsultation"],
        path = "/api/onlineconsultationdetails/{onlineconsultationdetails-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("onlineconsultationdetails-id", Int::class, "The OnlineConsultation ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateOnlineConsultation(ctx: Context){
        val onlineConsultation : OnlineConsultation = jsonToObject(ctx.body())
        if (OnlineConsultationController.onlineConsultationDAO.updateByOnlineConsultationId(
                onlineConsultationId = ctx.pathParam("onlineconsultation-id").toInt(),
                onlineConsultationToUpdate =onlineConsultation) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}