package ie.setu.domain

import org.joda.time.DateTime
import java.util.Date


data class SleepTracker (var id: Int,
                     var hours:Double,
                     var date: DateTime,
                     var userId: Int)