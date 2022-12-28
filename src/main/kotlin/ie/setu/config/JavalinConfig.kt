package ie.setu.config

import ie.setu.controllers.*
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.json.JavalinJackson
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.rendering.vue.VueComponent
import io.swagger.v3.oas.models.info.Info
class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create {
            it.registerPlugin(getConfiguredOpenApiPlugin())
            it.defaultContentType = "application/json"
            //added this jsonMapper for our integration tests - serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.enableWebjars()
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun getRemoteAssignedPort(): Int {
        val railwayPort = System.getenv("PORT")
        return if (railwayPort != null) {
            Integer.parseInt(railwayPort)
        } else 7001
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {
            path("/api/users") {
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}") {
                    get(UserController::getUserByUserId)
                    delete(UserController::deleteUser)
                    patch(UserController::updateUser)
                    path("activities") {
                        get(ActivityController::getActivitiesByUserId)
                        delete(ActivityController::deleteActivityByUserId)
                    }
                    path("foodtrackerdetails") {
                        get(FoodTrackerController::getFoodTrackerDetailsByUserId)
                        delete(FoodTrackerController::deleteFoodTrackerDetailsByUserId)
                    }
                    path("goalsettingdetails") {
                        get(GoalSettingController::getGoalSettingDetailsByUserId)
                        delete(GoalSettingController::deleteGoalSettingByUserId)
                    }
                    path("heartratemonitordetails") {
                        get(HeartRateMonitorController::getHeartRateMonitorDetailsByUserId)
                        delete(HeartRateMonitorController::deleteHeartRateMonitorByUserId)
                    }
                    path("onlineconsultationdetails") {
                        get(OnlineConsultationController::getOnlineConsultationDetailsByUserId)
                        delete(OnlineConsultationController::deleteOnlineConsultationByUserId)
                    }
                    path("sleeptrackerdetails") {
                        get(SleepTrackerController::getSleepTrackerDetailsByUserId)
                        delete(SleepTrackerController::deleteSleepTrackerByUserId)
                    }
                }
                path("/email/{email}") {
                    get(UserController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)
                path("{activity-id}") {
                    get(ActivityController::getActivitiesByActivityId)
                    delete(ActivityController::deleteActivityByActivityId)
                    patch(ActivityController::updateActivity)
                }
            }
            path("/api/foodtrackerdetails") {
                get(FoodTrackerController::getAllFoodTrackerDetails)
                post(FoodTrackerController::addFoodTracker)
                path("{foodtracker-id}") {
                    get(FoodTrackerController::getFoodTrackerDetailsByFoodTrackerId)
                    delete(FoodTrackerController::deleteFoodTrackerDetailsByFoodTrackerDetailsId)
                    patch(FoodTrackerController::updateFoodTrackerDetails)
                }
            }
            path("/api/goalsettingdetails") {
                get(GoalSettingController::getAllGoalSettingDetails)
                post(GoalSettingController::addGoalSetting)
                path("{goalsetting-id}") {
                    get(GoalSettingController::getGoalSettingDetailsByGoalSettingId)
                    delete(GoalSettingController::deleteGoalSettingByGoalSettingId)
                    patch(GoalSettingController::updateGoalSetting)
                }
            }
            path("/api/heartratemonitordetails") {
                get(HeartRateMonitorController::getAllHeartRateMonitorDetails)
                post(HeartRateMonitorController::addHeartRateMonitor)
                path("{heartratemonitor-id}") {
                    get(HeartRateMonitorController::getHeartRateMonitorDetailsByHeartRateMonitorId)
                    delete(HeartRateMonitorController::deleteHeartRateMonitorByHeartRateMonitorId)
                    patch(HeartRateMonitorController::updateHeartRateMonitor)
                }
            }
            path("/api/onlineconsultationdetails") {
                get(OnlineConsultationController::getAllOnlineConsultationDetails)
                post(OnlineConsultationController::addOnlineConsultation)
                path("{onlineconsultation-id}") {
                    get(OnlineConsultationController::getOnlineConsultationDetailsByOnlineConsultationId)
                    delete(OnlineConsultationController::deleteOnlineConsultationByOnlineConsultationId)
                    patch(OnlineConsultationController::updateOnlineConsultation)
                }
            }
            path("/api/sleeptrackerdetails") {
                get(SleepTrackerController::getAllSleepTrackerDetails)
                post(SleepTrackerController::addSleepTracker)
                path("{sleeptracker-id}") {
                    get(SleepTrackerController::getSleepTrackerDetailsBySleepTrackerId)
                    delete(SleepTrackerController::deleteSleepTrackerBySleepTrackerId)
                    patch(SleepTrackerController::updateSleepTracker)
                }
            }
            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside of VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))

        }
    }

    fun getConfiguredOpenApiPlugin() = OpenApiPlugin(
        OpenApiOptions(
            Info().apply {
                title("Health Tracker App")
                version("1.0")
                description("Health Tracker API")
            }
        ).apply {
            path("/swagger-docs") // endpoint for OpenAPI json
            swagger(SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
            reDoc(ReDocOptions("/redoc")) // endpoint for redoc
        }
    )

}