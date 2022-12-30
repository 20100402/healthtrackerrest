package ie.setu.helpers


import ie.setu.domain.*
import ie.setu.domain.db.*
import ie.setu.domain.repository.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime


val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"
val updatedName = "Updated Name"
val updatedEmail = "Updated Email"

val updatedDescription = "Updated Description"
val updatedDuration = 30.0
val updatedCalories = 945
val updatedStarted = DateTime.parse("2020-06-11T05:59:27.258Z")

val updatedMeal = "Lunch"
val updatedCaloriesIntake = 945
val updatedDate = DateTime.parse("2020-06-11T05:59:27.258Z")

val updatedMonth = "May"
val updatedBodyFatPercentage = 26.0
val updatedKilosReducedPerMonth = 7
val updatedGoalSettingDate = DateTime.parse("2020-06-11T05:59:27.258Z")

val updatedPulse = 77

val updatedDoctorName = "May"
val updatedAppointmentDate = DateTime.parse("2020-06-11T05:59:27.258Z")
val updatedRemarks = "Excellent"

val updatedHours = 10.0

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf<Activity>(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), userId = 2),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), userId = 3)
)

val foodTrackerDetails = arrayListOf<FoodTracker>(
    FoodTracker(id = 1, meal = "Breakfast", caloriesIntake = 230, date = DateTime.now(), userId = 1),
    FoodTracker(id = 2, meal = "Lunch", caloriesIntake = 80, date = DateTime.now(), userId = 2),
    FoodTracker(id = 3, meal = "Dinner", caloriesIntake = 120, date = DateTime.now(), userId = 3)
)

val goalSettingDetails = arrayListOf<GoalSetting>(
    GoalSetting(id = 1, month = "January", bodyFatPercentage = 22.0, kilosReducedPerMonth = 6, date = DateTime.now(), userId = 1),
    GoalSetting(id = 2, month = "June", bodyFatPercentage = 33.0, kilosReducedPerMonth = 8, date = DateTime.now(), userId = 2),
    GoalSetting(id = 3, month = "July", bodyFatPercentage = 44.0, kilosReducedPerMonth = 12, date = DateTime.now(), userId = 3)
)

val heartRateMonitorDetails = arrayListOf<HeartRateMonitor>(
    HeartRateMonitor(id = 1, pulse = 60, date = DateTime.now(), userId = 1),
    HeartRateMonitor(id = 2, pulse = 80, date = DateTime.now(), userId = 2),
    HeartRateMonitor(id = 3, pulse = 120, date = DateTime.now(), userId = 3)
)

val onlineConsultationDetails = arrayListOf<OnlineConsultation>(
    OnlineConsultation(id = 1, doctorName = "Tom", appointmentDate = DateTime.now(), remarks = "Good", userId = 1),
    OnlineConsultation(id = 2, doctorName = "Dick", appointmentDate = DateTime.now(), remarks ="Average",userId = 2),
    OnlineConsultation(id = 3, doctorName = "Harry", appointmentDate = DateTime.now(), remarks ="Bad", userId = 3)
)

val sleepTrackerDetails = arrayListOf<SleepTracker>(
    SleepTracker(id = 1, hours = 12.0, date = DateTime.now(), userId = 1),
    SleepTracker(id = 2, hours = 8.0, date = DateTime.now(), userId = 2),
    SleepTracker(id = 3, hours = 6.0, date = DateTime.now(), userId = 3)
)

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users[0])
    userDAO.save(users[1])
    userDAO.save(users[2])
    return userDAO
}

fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities[0])
    activityDAO.save(activities[1])
    activityDAO.save(activities[2])
    return activityDAO
}

fun populateFoodIntakeTrackerTable(): FoodTrackerDAO {
    SchemaUtils.create(FoodIntakeTracker)
    val foodTrackerDAO = FoodTrackerDAO()
    foodTrackerDAO.save(foodTrackerDetails[0])
    foodTrackerDAO.save(foodTrackerDetails[1])
    foodTrackerDAO.save(foodTrackerDetails[2])
    return foodTrackerDAO
}

fun populateGoalSettersTable(): GoalSettingDAO {
    SchemaUtils.create(GoalSetters)
    val goalSettingDAO = GoalSettingDAO()
    goalSettingDAO.save(goalSettingDetails[0])
    goalSettingDAO.save(goalSettingDetails[1])
    goalSettingDAO.save(goalSettingDetails[2])
    return goalSettingDAO
}

fun populateHeartRateMonitoringTable(): HeartRateMonitorDAO {
    SchemaUtils.create(HeartRateMonitoring)
    val heartRateMonitorDAO = HeartRateMonitorDAO()
    heartRateMonitorDAO.save(heartRateMonitorDetails[0])
    heartRateMonitorDAO.save(heartRateMonitorDetails[1])
    heartRateMonitorDAO.save(heartRateMonitorDetails[2])
    return heartRateMonitorDAO
}

fun populateOnlineConsultationsTable(): OnlineConsultationDAO {
    SchemaUtils.create(OnlineConsultations)
    val onlineConsultationDAO = OnlineConsultationDAO()
    onlineConsultationDAO.save(onlineConsultationDetails[0])
    onlineConsultationDAO.save(onlineConsultationDetails[1])
    onlineConsultationDAO.save(onlineConsultationDetails[2])
    return onlineConsultationDAO
}

fun populateSleepTrackingTable(): SleepTrackerDAO {
    SchemaUtils.create(SleepTracking)
    val sleepTrackerDAO = SleepTrackerDAO()
    sleepTrackerDAO.save(sleepTrackerDetails[0])
    sleepTrackerDAO.save(sleepTrackerDetails[1])
    sleepTrackerDAO.save(sleepTrackerDetails[2])
    return sleepTrackerDAO
}
