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
    id = it[FoodIntakeTracker.id],
    meal = it[FoodIntakeTracker.meal],
    caloriesIntake = it[FoodIntakeTracker.caloriesIntake],
    date = it[FoodIntakeTracker.date],
    userId = it[FoodIntakeTracker.userId]
)

fun mapToGoalSetting(it: ResultRow) = GoalSetting(
    id = it[GoalSetters.id],
    month = it[GoalSetters.month],
    bodyFatPercentage = it[GoalSetters.bodyFatPercentage],
    kilosReducedPerMonth = it[GoalSetters.kilosReducedPerMonth],
    date = it[GoalSetters.date],
    userId = it[GoalSetters.userId]
)

fun mapToHeartRateMonitor(it: ResultRow) = HeartRateMonitor(
    id = it[HeartRateMonitoring.id],
    pulse = it[HeartRateMonitoring.pulse],
    date = it[HeartRateMonitoring.date],
    userId = it[HeartRateMonitoring.userId]
)

fun mapToOnlineConsultation(it: ResultRow) = OnlineConsultation(
    id = it[OnlineConsultations.id],
    doctorName = it[OnlineConsultations.doctorName],
    appointmentDate = it[OnlineConsultations.appointmentDate],
    remarks = it[OnlineConsultations.remarks],
    userId = it[OnlineConsultations.userId]
)

fun mapToSleepTracker(it: ResultRow) = SleepTracker(
    id = it[SleepTracking.id],
    hours = it[SleepTracking.hours],
    date = it[SleepTracking.date],
    userId = it[SleepTracking.userId]
)




