package ie.setu.controllers

import ie.setu.domain.GoalSetting
import ie.setu.domain.repository.GoalSettingDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context


object GoalSettingController {

    private val userDao = UserDAO()
    private val goalSettingDAO = GoalSettingDAO()



    //--------------------------------------------------------------
    // GoalSettingDAOI specifics
    //-------------------------------------------------------------

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

    fun getGoalSettingDetailsByGoalSettingId(ctx: Context) {
        val goalSetting = GoalSettingController.goalSettingDAO.findByGoalSettingId((ctx.pathParam("GoalSetting-id").toInt()))
        if (goalSetting != null){
            ctx.json(goalSetting)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

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

    fun deleteGoalSettingByGoalSettingId(ctx: Context){
        if (GoalSettingController.goalSettingDAO.deleteByGoalSettingId(ctx.pathParam("GoalSetting-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteGoalSettingByUserId(ctx: Context){
        if (GoalSettingController.goalSettingDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateGoalSetting(ctx: Context){
        val goalSetting : GoalSetting = jsonToObject(ctx.body())
        if (GoalSettingController.goalSettingDAO.updateByGoalSettingId(
                goalSettingId = ctx.pathParam("GoalSetting-id").toInt(),
                goalSettingToUpdate = goalSetting) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}