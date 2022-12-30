package ie.setu.domain

import org.joda.time.DateTime
import java.util.*

data class HeartRateMonitor (var id: Int,
                             var pulse:Int,
                             var date: DateTime,
                             var userId: Int)

