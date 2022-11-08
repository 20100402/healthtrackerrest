package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToFoodTracker(it: ResultRow) = FoodTracker(
    id = it[FoodTrackerDetails.id],
    meal = it[FoodTrackerDetails.meal],
    caloriesIntake = it[FoodTrackerDetails.caloriesIntake],
    userId = it[FoodTrackerDetails.userId]
)

fun mapToGoalSetting(it: ResultRow) = GoalSetting(
    id = it[GoalSettingDetails.id],
    month = it[GoalSettingDetails.month],
    bodyFatPercentage = it[GoalSettingDetails.bodyFatPercentage],
    kilosReducedPerMonth = it[GoalSettingDetails.kilosReducedPerMonth],
    userId = it[GoalSettingDetails.userId]
)

fun mapToHeartRateMonitor(it: ResultRow) = HeartRateMonitor(
    id = it[HeartRateMonitorDetails.id],
    heartRate = it[HeartRateMonitorDetails.heartRate],
    pulse = it[HeartRateMonitorDetails.pulse],
    userId = it[HeartRateMonitorDetails.userId]
)

fun mapToOnlineConsultation(it: ResultRow) = OnlineConsultation(
    id = it[OnlineConsultationDetails.id],
    doctorName = it[OnlineConsultationDetails.doctorName],
    appointmentDate = it[OnlineConsultationDetails.appointmentDate],
    userId = it[OnlineConsultationDetails.userId]
)

fun mapToSleepTracker(it: ResultRow) = SleepTracker(
    id = it[SleepTrackerDetails.id],
    hoursPerDay = it[SleepTrackerDetails.hoursPerDay],
    totalHoursPerWeek = it[SleepTrackerDetails.totalHoursPerWeek],
    userId = it[SleepTrackerDetails.userId]
)




