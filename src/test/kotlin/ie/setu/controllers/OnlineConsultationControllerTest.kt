package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.OnlineConsultation
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OnlineConsultationControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateOnlineConsultationDetails {

        @Test
        fun `add an input for onlineconsultationdetails when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated input for onlineconsultation that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addOnlineConsultationResponse = addOnlineConsultationDetails(
                onlineConsultationDetails[0].doctorName, onlineConsultationDetails[0].appointmentDate,
                onlineConsultationDetails[0].remarks, addedUser.id
            )
            Assertions.assertEquals(201, addOnlineConsultationResponse.status)

            //After - delete the user (onlineconsultation will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add onlineconsultationdetails when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            Assertions.assertEquals(404, retrieveUserById(userId).status)

            val addOnlineConsultationResponse = addOnlineConsultationDetails(
                onlineConsultationDetails[0].doctorName, onlineConsultationDetails[0].appointmentDate,
                onlineConsultationDetails[0].remarks, userId
            )
            Assertions.assertEquals(404, addOnlineConsultationResponse.status)
        }
    }

    @Nested
    inner class ReadOnlineConsultationDetails {

        @Test
        fun `get all onlineconsultationdetails from the database returns 200 or 404 response`() {
            val response = retrieveAllOnlineConsultationDetails()
            if (response.status == 200){
                val retrievedOnlineConsultationDetails = jsonNodeToObject<Array<OnlineConsultation>>(response)
                Assertions.assertNotEquals(0, retrievedOnlineConsultationDetails.size)
            }
            else{
                Assertions.assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all onlineconsultationdetails by user id when user and onlineconsultationdetails exists returns 200 response`() {
            //Arrange - add a user and 3 associated onlineconsultationdetails that we plan to retrieve
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            addOnlineConsultationDetails(
                onlineConsultationDetails[0].doctorName, onlineConsultationDetails[0].appointmentDate,
                onlineConsultationDetails[0].remarks, addedUser.id
            )
            addOnlineConsultationDetails(
                onlineConsultationDetails[1].doctorName, onlineConsultationDetails[1].appointmentDate,
                onlineConsultationDetails[1].remarks, addedUser.id
            )
            addOnlineConsultationDetails(
                onlineConsultationDetails[2].doctorName, onlineConsultationDetails[2].appointmentDate,
                onlineConsultationDetails[2].remarks, addedUser.id
            )

            //Assert and Act - retrieve the three added onlineconsultationdetails by user id
            val response = retrieveOnlineConsultationDetailsByUserId(addedUser.id)
            Assertions.assertEquals(200, response.status)
            val retrievedOnlineConsultationDetails = jsonNodeToObject<Array<OnlineConsultation>>(response)
            Assertions.assertEquals(3, retrievedOnlineConsultationDetails.size)

            //After - delete the added user and assert a 204 is returned (onlineconsultationdetails are cascade deleted)
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all onlineconsultationdetails by user id when no onlineconsultationdetails exist returns 404 response`() {
            //Arrange - add a user
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the onlineconsultationdetails by user id
            val response = retrieveOnlineConsultationDetailsByUserId(addedUser.id)
            Assertions.assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all onlineconsultationdetails by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve onlineconsultationdetails by user id
            val response = retrieveOnlineConsultationDetailsByUserId(userId)
            Assertions.assertEquals(404, response.status)
        }

        @Test
        fun `get onlineconsultationdetails by onlineconsultation id when no onlineconsultationdetails exists returns 404 response`() {
            //Arrange
            val onlineConsultationId = -1
            //Assert and Act - attempt to retrieve the onlineconsultationdetails by onlineconsultation id
            val response = retrieveOnlineConsultationDetailsByOnlineConsultationId(onlineConsultationId)
            Assertions.assertEquals(404, response.status)
        }


        @Test
        fun `get onlineconsultationdetails by onlineconsultation id when onlineconsultationdetails exists returns 200 response`() {
            //Arrange - add a user and associated onlineconsultationdetails
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addOnlineConsultationResponse = addOnlineConsultationDetails(
                onlineConsultationDetails[0].doctorName, onlineConsultationDetails[0].appointmentDate,
                onlineConsultationDetails[0].remarks, addedUser.id
            )
            Assertions.assertEquals(201, addOnlineConsultationResponse.status)
            val addedOnlineConsultation = jsonNodeToObject<OnlineConsultation>(addOnlineConsultationResponse)

            //Act & Assert - retrieve the onlineconsultationdetails by onlineconsultation id
            val response = retrieveOnlineConsultationDetailsByOnlineConsultationId(addedOnlineConsultation.id)
            Assertions.assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class UpdateOnlineConsultationDetails {

        @Test
        fun `updating an onlineconsultationdetails by onlineconsultation id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val onlineConsultationId = -1

            //Arrange - check there is no user for -1 id
            Assertions.assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an onlineconsultationdetails/user that doesn't exist
            Assertions.assertEquals(
                404, updateOnlineConsultationDetails(
                    onlineConsultationId, updatedDoctorName, updatedAppointmentDate, updatedRemarks,
                    userId
                ).status
            )
        }

        @Test
        fun `updating an onlineconsultationdetails by onlineconsultationdetails id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated onlineconsultationdetails that we plan to do an update on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addOnlineConsultationResponse = addOnlineConsultationDetails(
                onlineConsultationDetails[0].doctorName, onlineConsultationDetails[0].appointmentDate,
                onlineConsultationDetails[0].remarks, addedUser.id
            )
            Assertions.assertEquals(201, addOnlineConsultationResponse.status)
            val addedOnlineConsultation = jsonNodeToObject<OnlineConsultation>(addOnlineConsultationResponse)

            //Act & Assert - update the added onlineconsultationdetails and assert a 204 is returned
            val updatedOnlineConsultationResponse = updateOnlineConsultationDetails(addedOnlineConsultation.id, updatedDoctorName, updatedAppointmentDate,
                updatedRemarks, addedUser.id)
            Assertions.assertEquals(204, updatedOnlineConsultationResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedOnlineConsultationResponse = retrieveOnlineConsultationDetailsByOnlineConsultationId(addedOnlineConsultation.id)
            val updatedOnlineConsultationDetails = jsonNodeToObject<OnlineConsultation>(retrievedOnlineConsultationResponse)
            Assertions.assertEquals(updatedDoctorName, updatedOnlineConsultationDetails.doctorName)
            Assertions.assertEquals(updatedRemarks, updatedOnlineConsultationDetails.remarks)
            Assertions.assertEquals(updatedAppointmentDate, updatedOnlineConsultationDetails.appointmentDate)

            //After - delete the user
            deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteOnlineConsultationDetails {

        @Test
        fun `deleting onlineconsultationdetails by onlineconsultation id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, deleteOnlineConsultationDetailsByOnlineConsultationId(-1).status)
        }

        @Test
        fun `deleting onlineconsultationdetails by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, deleteOnlineConsultationDetailsByUserId(-1).status)
        }

        @Test
        fun `deleting an onlineconsultationdetails by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated onlineconsultationdetails that we plan to do a delete on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addOnlineConsultationResponse = addOnlineConsultationDetails(
                onlineConsultationDetails[0].doctorName, onlineConsultationDetails[0].appointmentDate,
                onlineConsultationDetails[0].remarks, addedUser.id
            )
            Assertions.assertEquals(201, addOnlineConsultationResponse.status)

            //Act & Assert - delete the added onlineconsultationdetails and assert a 204 is returned
            val addedOnlineConsultation = jsonNodeToObject<OnlineConsultation>(addOnlineConsultationResponse)
            Assertions.assertEquals(204, deleteOnlineConsultationDetailsByOnlineConsultationId(addedOnlineConsultation.id).status)

            //After - delete the user
            deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all onlineconsultationdetails by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated onlineconsultationdetails that we plan to do a cascade delete
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addOnlineConsultationResponse1 = addOnlineConsultationDetails(
                onlineConsultationDetails[0].doctorName, onlineConsultationDetails[0].appointmentDate,
                onlineConsultationDetails[0].remarks, addedUser.id
            )
            Assertions.assertEquals(201, addOnlineConsultationResponse1.status)
            val addOnlineConsultationResponse2 = addOnlineConsultationDetails(
                onlineConsultationDetails[0].doctorName, onlineConsultationDetails[0].appointmentDate,
                onlineConsultationDetails[0].remarks, addedUser.id
            )
            Assertions.assertEquals(201, addOnlineConsultationResponse2.status)
            val addOnlineConsultationResponse3 = addOnlineConsultationDetails(
                onlineConsultationDetails[0].doctorName, onlineConsultationDetails[0].appointmentDate,
                onlineConsultationDetails[0].remarks, addedUser.id
            )
            Assertions.assertEquals(201, addOnlineConsultationResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted onlineconsultationdetails
            val addedOnlineConsultation1 = jsonNodeToObject<OnlineConsultation>(addOnlineConsultationResponse1)
            val addedOnlineConsultation2 = jsonNodeToObject<OnlineConsultation>(addOnlineConsultationResponse2)
            val addedOnlineConsultation3 = jsonNodeToObject<OnlineConsultation>(addOnlineConsultationResponse3)
            Assertions.assertEquals(404, retrieveOnlineConsultationDetailsByOnlineConsultationId(addedOnlineConsultation1.id).status)
            Assertions.assertEquals(404, retrieveOnlineConsultationDetailsByOnlineConsultationId(addedOnlineConsultation2.id).status)
            Assertions.assertEquals(404, retrieveOnlineConsultationDetailsByOnlineConsultationId(addedOnlineConsultation3.id).status)
        }
    }
}