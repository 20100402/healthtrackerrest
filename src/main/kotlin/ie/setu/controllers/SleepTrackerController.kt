package ie.setu.controllers

import ie.setu.domain.SleepTracker
import ie.setu.domain.repository.SleepTrackerDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object SleepTrackerController {

    private val userDao = UserDAO()
    private val sleepTrackerDAO = SleepTrackerDAO()



    //--------------------------------------------------------------
    // SleepTrackerDAOI specifics
    //-------------------------------------------------------------

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

    fun getSleepTrackerDetailsBySleepTrackerId(ctx: Context) {
        val sleepTracker = SleepTrackerController.sleepTrackerDAO.findBySleepTrackerId((ctx.pathParam("SleepTracker-id").toInt()))
        if (sleepTracker != null){
            ctx.json(sleepTracker)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

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

    fun deleteSleepTrackerBySleepTrackerId(ctx: Context){
        if (SleepTrackerController.sleepTrackerDAO.deleteBySleepTrackerId(ctx.pathParam("SleepTracker-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteSleepTrackerByUserId(ctx: Context){
        if (SleepTrackerController.sleepTrackerDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateSleepTracker(ctx: Context){
        val sleepTracker : SleepTracker = jsonToObject(ctx.body())
        if (SleepTrackerController.sleepTrackerDAO.updateBySleepTrackerId(
                sleepTrackerId = ctx.pathParam("SleepTracker-id").toInt(),
                sleepTrackerToUpdate =sleepTracker) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}