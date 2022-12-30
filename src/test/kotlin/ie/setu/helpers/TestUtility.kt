package ie.setu.helpers
import ie.setu.config.DbConfig
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime

private val db = DbConfig().getDbConnection()
private val app = ServerContainer.instance
private val origin = "http://localhost:" + app.port()


/* Users */

//helper function to add a test user to the database
fun addUser (name: String, email: String): HttpResponse<JsonNode> {
    return Unirest.post(origin + "/api/users")
        .body("{\"name\":\"$name\", \"email\":\"$email\"}")
        .asJson()
}

//helper function to delete a test user from the database
fun deleteUser (id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/users/$id").asString()
}

//helper function to retrieve a test user from the database by email
fun retrieveUserByEmail(email : String) : HttpResponse<String> {
    return Unirest.get(origin + "/api/users/email/${email}").asString()
}

//helper function to retrieve a test user from the database by id
fun retrieveUserById(id: Int) : HttpResponse<String> {
    return Unirest.get(origin + "/api/users/${id}").asString()
}

//helper function to add a test user to the database
fun updateUser (id: Int, name: String, email: String): HttpResponse<JsonNode> {
    return Unirest.patch(origin + "/api/users/$id")
        .body("{\"name\":\"$name\", \"email\":\"$email\"}")
        .asJson()
}


/* Activity */

//helper function to retrieve all activities
fun retrieveAllActivities(): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/activities").asJson()
}

//helper function to retrieve activities by user id
fun retrieveActivitiesByUserId(id: Int): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/users/${id}/activities").asJson()
}

//helper function to retrieve activity by activity id
fun retrieveActivityByActivityId(id: Int): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/activities/${id}").asJson()
}

//helper function to delete an activity by activity id
fun deleteActivityByActivityId(id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/activities/$id").asString()
}

//helper function to delete an activity by activity id
fun deleteActivitiesByUserId(id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/users/$id/activities").asString()
}

//helper function to update an activity by activity id
fun updateActivity(id: Int, description: String, duration: Double, calories: Int,
                           started: DateTime, userId: Int): HttpResponse<JsonNode> {
    return Unirest.patch(origin + "/api/activities/$id")
        .body("""
                {
                  "description":"$description",
                  "duration":$duration,
                  "calories":$calories,
                  "started":"$started",
                  "userId":$userId
                }
            """.trimIndent()).asJson()
}

//helper function to add an activity
fun addActivity(description: String, duration: Double, calories: Int,
                        started: DateTime, userId: Int): HttpResponse<JsonNode> {
    return Unirest.post(origin + "/api/activities")
        .body("""
                {
                   "description":"$description",
                   "duration":$duration,
                   "calories":$calories,
                   "started":"$started",
                   "userId":$userId
                }
            """.trimIndent())
        .asJson()
}


/* FoodTracker */

//helper function to retrieve all foodtrackerdetails
fun retrieveAllFoodTrackerDetails(): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/foodtrackerdetails").asJson()
}

//helper function to retrieve foodtrackerdetails by user id
fun retrieveFoodTrackerDetailsByUserId(id: Int): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/users/${id}/foodtrackerdetails").asJson()
}

//helper function to retrieve foodtrackerdetails by foodtracker id
fun retrieveFoodTrackerdetailsByFoodTrackerId(id: Int): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/foodtrackerdetails/${id}").asJson()
}

//helper function to delete an foodtrackerdetails by foodtracker id
fun deleteFoodTrackerdetailsByFoodTrackerId(id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/foodtrackerdetails/$id").asString()
}

//helper function to delete an foodtrackerdetails by user id
fun deleteFoodTrackerdetailsByUserId(id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/users/$id/foodtrackerdetails").asString()
}

//helper function to update foodtrackerdetails by foodtracker id
fun updateFoodTrackerDetails(id: Int, meal: String,caloriesIntake: Int,
                   date: DateTime, userId: Int): HttpResponse<JsonNode> {
    return Unirest.patch(origin + "/api/foodtrackerdetails/$id")
        .body("""
                {
                  "meal":"$meal",
                  "caloriesIntake":$caloriesIntake,
                  "date":"$date",
                  "userId":$userId
                }
            """.trimIndent()).asJson()
}

//helper function to add foodtrackerdetails
fun addFoodTrackerDetails(meal: String, caloriesIntake: Int,
                date: DateTime, userId: Int): HttpResponse<JsonNode> {
    return Unirest.post(origin + "/api/foodtrackerdetails")
        .body("""
                {
                   "meal":"$meal",
                   "caloriesIntake":$caloriesIntake,
                   "date":"$date",
                   "userId":$userId
                }
            """.trimIndent())
        .asJson()
}


/* GoalSetting */

//helper function to retrieve all goalsettingdetails
fun retrieveAllGoalSettingDetails(): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/goalsettingdetails").asJson()
}

//helper function to retrieve goalsettingdetails by user id
fun retrieveGoalSettingDetailsByUserId(id: Int): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/users/${id}/goalsettingdetails").asJson()
}

//helper function to retrieve goalsettingdetails by goalsetting id
fun retrieveGoalSettingDetailsByGoalSettingId(id: Int): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/goalsettingdetails/${id}").asJson()
}

//helper function to delete an goalsettingdetails by goalsetting id
fun deleteGoalSettingDetailsByGoalSettingId(id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/goalsettingdetails/$id").asString()
}

//helper function to delete an goalsettingdetails by user id
fun deleteGoalSettingDetailsByUserId(id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/users/$id/goalsettingdetails").asString()
}

//helper function to update goalsettingdetails by goalsetting id
fun updateGoalSettingDetails(id: Int, month: String, bodyFatPercentage: Double, kilosReducedPerMonth: Int,
                             date: DateTime, userId: Int): HttpResponse<JsonNode> {
    return Unirest.patch(origin + "/api/goalsettingdetails/$id")
        .body("""
               {
                   "month":"$month",
                   "bodyFatPercentage": $bodyFatPercentage,
                   "kilosReducedPerMonth":$kilosReducedPerMonth,
                   "date":"$date",
                   "userId":$userId
                }
            """.trimIndent()).asJson()
}

//helper function to add goalsettingdetails
fun addGoalSettingDetails(month: String, bodyFatPercentage: Double, kilosReducedPerMonth: Int,
                          date: DateTime, userId: Int): HttpResponse<JsonNode> {
    return Unirest.post(origin + "/api/goalsettingdetails")
        .body("""
                {
                   "month":"$month",
                   "bodyFatPercentage": $bodyFatPercentage,
                   "kilosReducedPerMonth":$kilosReducedPerMonth,
                   "date":"$date",
                   "userId":$userId
                }
            """.trimIndent())
        .asJson()
}

/* HeartRateMonitor */

//helper function to retrieve all heartratemonitordetails
fun retrieveAllHeartRateMonitorDetails(): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/heartratemonitordetails").asJson()
}

//helper function to retrieve heartratemonitordetails by user id
fun retrieveHeartRateMonitorDetailsByUserId(id: Int): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/users/${id}/heartratemonitordetails").asJson()
}

//helper function to retrieve heartratemonitordetails by heartratemonitor id
fun retrieveHeartRateMonitorDetailsByHeartRateMonitorId(id: Int): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/heartratemonitordetails/${id}").asJson()
}

//helper function to delete an heartratemonitordetails by heartratemonitor id
fun deleteHeartRateMonitorDetailsByHeartRateMonitorId(id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/heartratemonitordetails/$id").asString()
}

//helper function to delete an heartratemonitordetails by user id
fun deleteHeartRateMonitorDetailsByUserId(id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/users/$id/heartratemonitordetails").asString()
}

//helper function to update heartratemonitordetails by heartratemonitor id
fun updateHeartRateMonitorDetails(id: Int, pulse: Int,
                                  date: DateTime, userId: Int): HttpResponse<JsonNode> {
    return Unirest.patch(origin + "/api/heartratemonitordetails/$id")
        .body("""
               {
                   "pulse":$pulse,
                   "date":"$date",
                   "userId":$userId
                }
            """.trimIndent()).asJson()
}

//helper function to add heartratemonitordetails
fun addHeartRateMonitorDetails(pulse: Int,
                          date: DateTime, userId: Int): HttpResponse<JsonNode> {
    return Unirest.post(origin + "/api/heartratemonitordetails")
        .body("""
                {
                   "pulse":$pulse,
                   "date":"$date",
                   "userId":$userId
                }
            """.trimIndent())
        .asJson()
}


/* OnlineConsultation */

//helper function to retrieve all onlineconsultationdetails
fun retrieveAllOnlineConsultationDetails(): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/onlineconsultationdetails").asJson()
}

//helper function to retrieve onlineconsultationdetails by user id
fun retrieveOnlineConsultationDetailsByUserId(id: Int): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/users/${id}/onlineconsultationdetails").asJson()
}

//helper function to retrieve onlineconsultationdetails by onlineconsultationdetails id
fun retrieveOnlineConsultationDetailsByOnlineConsultationId(id: Int): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/onlineconsultationdetails/${id}").asJson()
}

//helper function to delete an onlineconsultationdetails by onlineconsultationdetails id
fun deleteOnlineConsultationDetailsByOnlineConsultationId(id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/onlineconsultationdetails/$id").asString()
}

//helper function to delete an onlineconsultationdetails by user id
fun deleteOnlineConsultationDetailsByUserId(id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/users/$id/onlineconsultationdetails").asString()
}

//helper function to update onlineconsultationdetails by onlineconsultationdetails id
fun updateOnlineConsultationDetails(id: Int, doctorName: String,
                                    appointmentDate: DateTime, remarks: String, userId: Int): HttpResponse<JsonNode> {
    return Unirest.patch(origin + "/api/onlineconsultationdetails/$id")
        .body("""
               {
                   "doctorName":"$doctorName",
                   "appointmentDate":"$appointmentDate",
                   "remarks":"$remarks",
                   "userId":$userId
                }
            """.trimIndent()).asJson()
}

//helper function to add onlineconsultationdetails
fun addOnlineConsultationDetails(doctorName: String,
                                 appointmentDate: DateTime, remarks: String, userId: Int): HttpResponse<JsonNode> {
    return Unirest.post(origin + "/api/onlineconsultationdetails")
        .body("""
                {
                   "doctorName":"$doctorName",
                   "appointmentDate":"$appointmentDate",
                   "remarks":"$remarks",
                   "userId":$userId
                }
            """.trimIndent())
        .asJson()
}


/* SleepTracker */

//helper function to retrieve all sleeptrackerdetails
fun retrieveAllSleepTrackerDetails(): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/sleeptrackerdetails").asJson()
}

//helper function to retrieve sleeptrackerdetails by user id
fun retrieveSleepTrackerDetailsByUserId(id: Int): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/users/${id}/sleeptrackerdetails").asJson()
}

//helper function to retrieve sleeptrackerdetails by sleeptrackerdetails id
fun retrieveSleepTrackerDetailsBySleepTrackerId(id: Int): HttpResponse<JsonNode> {
    return Unirest.get(origin + "/api/sleeptrackerdetails/${id}").asJson()
}

//helper function to delete an sleeptrackerdetails by sleeptrackerdetails id
fun deleteSleepTrackerDetailsBySleepTrackerId(id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/sleeptrackerdetails/$id").asString()
}

//helper function to delete an sleeptrackerdetails by user id
fun deleteSleepTrackerDetailsByUserId(id: Int): HttpResponse<String> {
    return Unirest.delete(origin + "/api/users/$id/sleeptrackerdetails").asString()
}

//helper function to update sleeptrackerdetails by sleeptrackerdetails id
fun updateSleepTrackerDetails(id: Int, hours: Double,
                              date: DateTime, userId: Int): HttpResponse<JsonNode> {
    return Unirest.patch(origin + "/api/sleeptrackerdetails/$id")
        .body("""
               {
                   "hours":$hours,
                   "date":"$date",
                   "userId":$userId
                }
            """.trimIndent()).asJson()
}

//helper function to add sleeptrackerdetails
fun addSleepTrackerDetails(hours: Double,
                                 date: DateTime, userId: Int): HttpResponse<JsonNode> {
    return Unirest.post(origin + "/api/sleeptrackerdetails")
        .body("""
                {
                   "hours":$hours,
                   "date":"$date",
                   "userId":$userId
                }
            """.trimIndent())
        .asJson()
}