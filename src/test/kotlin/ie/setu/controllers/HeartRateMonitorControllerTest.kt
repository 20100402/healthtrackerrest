package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.HeartRateMonitor
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HeartRateMonitorControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateHeartRateMonitorDetails {

        @Test
        fun `add an input for heartratemonitordetails when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated input for heartratemonitor that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addHeartRateMonitorResponse = addHeartRateMonitorDetails(
                heartRateMonitorDetails[0].pulse, heartRateMonitorDetails[0].date, addedUser.id
            )
            Assertions.assertEquals(201, addHeartRateMonitorResponse.status)

            //After - delete the user (heartratemonitor will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add heartratemonitordetails when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            Assertions.assertEquals(404, retrieveUserById(userId).status)

            val addHeartRateMonitorResponse = addHeartRateMonitorDetails(
                heartRateMonitorDetails[0].pulse, heartRateMonitorDetails[0].date, userId
            )
            Assertions.assertEquals(404, addHeartRateMonitorResponse.status)
        }
    }

    @Nested
    inner class ReadHeartRateMonitorDetails {

        @Test
        fun `get all heartratemonitordetails from the database returns 200 or 404 response`() {
            val response = retrieveAllHeartRateMonitorDetails()
            if (response.status == 200){
                val retrievedHeartRateMonitorDetails = jsonNodeToObject<Array<HeartRateMonitor>>(response)
                Assertions.assertNotEquals(0, retrievedHeartRateMonitorDetails.size)
            }
            else{
                Assertions.assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all heartratemonitordetails by user id when user and heartratemonitordetails exists returns 200 response`() {
            //Arrange - add a user and 3 associated heartratemonitordetails that we plan to retrieve
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            addHeartRateMonitorDetails(
                heartRateMonitorDetails[0].pulse, heartRateMonitorDetails[0].date,  addedUser.id
            )
            addHeartRateMonitorDetails(
                heartRateMonitorDetails[1].pulse, heartRateMonitorDetails[1].date,  addedUser.id
            )
            addHeartRateMonitorDetails(
                heartRateMonitorDetails[2].pulse, heartRateMonitorDetails[2].date,  addedUser.id
            )

            //Assert and Act - retrieve the three added heartratemonitordetails by user id
            val response = retrieveHeartRateMonitorDetailsByUserId(addedUser.id)
            Assertions.assertEquals(200, response.status)
            val retrievedHeartRateMonitorDetails = jsonNodeToObject<Array<HeartRateMonitor>>(response)
            Assertions.assertEquals(3, retrievedHeartRateMonitorDetails.size)

            //After - delete the added user and assert a 204 is returned (heartratemonitordetails are cascade deleted)
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all heartratemonitordetails by user id when no heartratemonitordetails exist returns 404 response`() {
            //Arrange - add a user
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the heartratemonitordetails by user id
            val response = retrieveHeartRateMonitorDetailsByUserId(addedUser.id)
            Assertions.assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all heartratemonitordetails by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve heartratemonitordetails by user id
            val response = retrieveHeartRateMonitorDetailsByUserId(userId)
            Assertions.assertEquals(404, response.status)
        }

        @Test
        fun `get heartratemonitordetails by heartratemonitor id when no heartratemonitordetails exists returns 404 response`() {
            //Arrange
            val heartRateMonitorId = -1
            //Assert and Act - attempt to retrieve the heartratemonitordetails by heartratemonitor id
            val response = retrieveHeartRateMonitorDetailsByHeartRateMonitorId(heartRateMonitorId)
            Assertions.assertEquals(404, response.status)
        }


        @Test
        fun `get heartratemonitordetails by heartratemonitor id when heartratemonitordetails exists returns 200 response`() {
            //Arrange - add a user and associated heartratemonitordetails
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addHeartRateMonitorResponse = addHeartRateMonitorDetails(
                heartRateMonitorDetails[0].pulse, heartRateMonitorDetails[0].date,  addedUser.id
            )
            Assertions.assertEquals(201, addHeartRateMonitorResponse.status)
            val addedHeartRateMonitor = jsonNodeToObject<HeartRateMonitor>(addHeartRateMonitorResponse)

            //Act & Assert - retrieve the heartratemonitordetails by heartratemonitor id
            val response = retrieveHeartRateMonitorDetailsByHeartRateMonitorId(addedHeartRateMonitor.id)
            Assertions.assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class UpdateHeartRateMonitorDetails {

        @Test
        fun `updating an heartratemonitordetails by heartratemonitor id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val heartRateMonitorId = -1

            //Arrange - check there is no user for -1 id
            Assertions.assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an heartratemonitordetails/user that doesn't exist
            Assertions.assertEquals(
                404, updateHeartRateMonitorDetails(
                    heartRateMonitorId, updatedPulse,
                    updatedDate, userId
                ).status
            )
        }

        @Test
        fun `updating an heartratemonitordetails by heartratemonitordetails id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated heartratemonitordetails that we plan to do an update on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addHeartRateMonitorResponse = addHeartRateMonitorDetails(
                heartRateMonitorDetails[0].pulse, heartRateMonitorDetails[0].date,  addedUser.id
            )
            Assertions.assertEquals(201, addHeartRateMonitorResponse.status)
            val addedHeartRateMonitor = jsonNodeToObject<HeartRateMonitor>(addHeartRateMonitorResponse)

            //Act & Assert - update the added heartratemonitordetails and assert a 204 is returned
            val updatedHeartRateMonitorResponse = updateHeartRateMonitorDetails(addedHeartRateMonitor.id, updatedPulse, updatedDate, addedUser.id)
            Assertions.assertEquals(204, updatedHeartRateMonitorResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedHeartRateMonitorResponse = retrieveHeartRateMonitorDetailsByHeartRateMonitorId(addedHeartRateMonitor.id)
            val updatedHeartRateMonitorDetails = jsonNodeToObject<HeartRateMonitor>(retrievedHeartRateMonitorResponse)
            Assertions.assertEquals(updatedPulse, updatedHeartRateMonitorDetails.pulse)
            Assertions.assertEquals(updatedDate, updatedHeartRateMonitorDetails.date)

            //After - delete the user
            deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteHeartRateMonitorDetails {

        @Test
        fun `deleting heartratemonitordetails by heartratemonitor id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, deleteHeartRateMonitorDetailsByHeartRateMonitorId(-1).status)
        }

        @Test
        fun `deleting heartratemonitordetails by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, deleteHeartRateMonitorDetailsByUserId(-1).status)
        }

        @Test
        fun `deleting an heartratemonitordetails by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated heartratemonitordetails that we plan to do a delete on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addHeartRateMonitorResponse = addHeartRateMonitorDetails(
                heartRateMonitorDetails[0].pulse, heartRateMonitorDetails[0].date,  addedUser.id
            )
            Assertions.assertEquals(201, addHeartRateMonitorResponse.status)

            //Act & Assert - delete the added heartratemonitordetails and assert a 204 is returned
            val addedHeartRateMonitor = jsonNodeToObject<HeartRateMonitor>(addHeartRateMonitorResponse)
            Assertions.assertEquals(204, deleteHeartRateMonitorDetailsByHeartRateMonitorId(addedHeartRateMonitor.id).status)

            //After - delete the user
            deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all heartratemonitordetails by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated heartratemonitordetails that we plan to do a cascade delete
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addHeartRateMonitorResponse1 = addHeartRateMonitorDetails(
                heartRateMonitorDetails[0].pulse, heartRateMonitorDetails[0].date,  addedUser.id
            )
            Assertions.assertEquals(201, addHeartRateMonitorResponse1.status)
            val addHeartRateMonitorResponse2 = addHeartRateMonitorDetails(
                heartRateMonitorDetails[1].pulse, heartRateMonitorDetails[1].date,  addedUser.id
            )
            Assertions.assertEquals(201, addHeartRateMonitorResponse2.status)
            val addHeartRateMonitorResponse3 = addHeartRateMonitorDetails(
                heartRateMonitorDetails[2].pulse, heartRateMonitorDetails[2].date,  addedUser.id
            )
            Assertions.assertEquals(201, addHeartRateMonitorResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted heartratemonitordetails
            val addedHeartRateMonitor1 = jsonNodeToObject<HeartRateMonitor>(addHeartRateMonitorResponse1)
            val addedHeartRateMonitor2 = jsonNodeToObject<HeartRateMonitor>(addHeartRateMonitorResponse2)
            val addedHeartRateMonitor3 = jsonNodeToObject<HeartRateMonitor>(addHeartRateMonitorResponse3)
            Assertions.assertEquals(404, retrieveHeartRateMonitorDetailsByHeartRateMonitorId(addedHeartRateMonitor1.id).status)
            Assertions.assertEquals(404, retrieveHeartRateMonitorDetailsByHeartRateMonitorId(addedHeartRateMonitor2.id).status)
            Assertions.assertEquals(404, retrieveHeartRateMonitorDetailsByHeartRateMonitorId(addedHeartRateMonitor3.id).status)
        }
    }
}