package ie.setu.controllers

import ie.setu.domain.OnlineConsultation
import ie.setu.domain.repository.OnlineConsultationDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object OnlineConsultationController {

    private val userDao = UserDAO()
    private val onlineConsultationDAO = OnlineConsultationDAO()



    //--------------------------------------------------------------
    // OnlineConsultationDAOI specifics
    //-------------------------------------------------------------

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

    fun getOnlineConsultationDetailsByOnlineConsultationId(ctx: Context) {
        val onlineConsultation = OnlineConsultationController.onlineConsultationDAO.findByOnlineConsultationId((ctx.pathParam("OnlineConsultation-id").toInt()))
        if (onlineConsultation != null){
            ctx.json(onlineConsultation)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

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

    fun deleteOnlineConsultationByOnlineConsultationId(ctx: Context){
        if (OnlineConsultationController.onlineConsultationDAO.deleteByOnlineConsultationId(ctx.pathParam("OnlineConsultation-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteOnlineConsultationByUserId(ctx: Context){
        if (OnlineConsultationController.onlineConsultationDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateOnlineConsultation(ctx: Context){
        val onlineConsultation : OnlineConsultation = jsonToObject(ctx.body())
        if (OnlineConsultationController.onlineConsultationDAO.updateByOnlineConsultationId(
                onlineConsultationId = ctx.pathParam("OnlineConsultation-id").toInt(),
                onlineConsultationToUpdate =onlineConsultation) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}