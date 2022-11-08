package ie.setu.domain

import org.joda.time.DateTime


data class OnlineConsultation (var id: Int,
                     var doctorName:String,
                     var appointmentDate: DateTime,
                     var userId: Int)