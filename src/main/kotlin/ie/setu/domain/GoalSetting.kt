package ie.setu.domain

import org.joda.time.DateTime
import java.util.*


data class GoalSetting (var id: Int,
                        var month:String,
                        var bodyFatPercentage: Double,
                        var kilosReducedPerMonth: Int,
                        var date: DateTime,
                        var userId: Int)

