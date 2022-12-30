package ie.setu.domain

import org.joda.time.DateTime
import java.util.*


data class OnlineConsultation (var id: Int,
                               var doctorName:String,
                               var appointmentDate: DateTime,
                               var remarks: String,
                               var userId: Int)