package ie.setu.controllers

import ie.setu.domain.HeartRateMonitor
import ie.setu.domain.FoodTracker
import ie.setu.domain.repository.HeartRateMonitorDAO
import ie.setu.domain.repository.FoodTrackerDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context


object HeartRateMonitorController {

    private val userDao = UserDAO()
    private val heartRateMonitorDAO = HeartRateMonitorDAO()



    //--------------------------------------------------------------
    // HeartRateMonitorDAOI specifics
    //-------------------------------------------------------------

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

    fun getHeartRateMonitorDetailsByHeartRateMonitorId(ctx: Context) {
        val heartRateMonitor = HeartRateMonitorController.heartRateMonitorDAO.findByHeartRateMonitorId((ctx.pathParam("HeartRateMonitor-id").toInt()))
        if (heartRateMonitor != null){
            ctx.json(heartRateMonitor)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

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

    fun deleteHeartRateMonitorByHeartRateMonitorId(ctx: Context){
        if (HeartRateMonitorController.heartRateMonitorDAO.deleteByHeartRateMonitorId(ctx.pathParam("HeartRateMonitor-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteHeartRateMonitorByUserId(ctx: Context){
        if (HeartRateMonitorController.heartRateMonitorDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateHeartRateMonitor(ctx: Context){
        val heartRateMonitor : HeartRateMonitor = jsonToObject(ctx.body())
        if (HeartRateMonitorController.heartRateMonitorDAO.updateByHeartRateMonitorId(
                heartRateMonitorId = ctx.pathParam("HeartRateMonitor-id").toInt(),
                heartRateMonitorToUpdate =heartRateMonitor) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}