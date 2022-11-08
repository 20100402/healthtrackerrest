package ie.setu.domain


data class SleepTracker (var id: Int,
                     var hoursPerDay:Double,
                     var totalHoursPerWeek: Double,
                     var userId: Int)