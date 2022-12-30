package ie.setu.domain

import org.joda.time.DateTime
import java.util.Date

data class FoodTracker (var id: Int,
                     var meal: String,
                     var caloriesIntake: Int,
                     var date: DateTime,
                     var userId: Int)