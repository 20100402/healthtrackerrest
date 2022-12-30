package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.SleepTracker
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SleepTrackerControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateSleepTrackerDetails {

        @Test
        fun `add an input for sleeptrackerdetails when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated input for sleeptracker that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addSleepTrackerResponse = addSleepTrackerDetails(
                sleepTrackerDetails[0].hours, sleepTrackerDetails[0].date, addedUser.id
            )
            Assertions.assertEquals(201, addSleepTrackerResponse.status)

            //After - delete the user (sleeptracker will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add sleeptrackerdetails when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            Assertions.assertEquals(404, retrieveUserById(userId).status)

            val addSleepTrackerResponse = addSleepTrackerDetails(
                sleepTrackerDetails[0].hours, sleepTrackerDetails[0].date, userId
            )
            Assertions.assertEquals(404, addSleepTrackerResponse.status)
        }
    }

    @Nested
    inner class ReadSleepTrackerDetails {

        @Test
        fun `get all sleeptrackerdetails from the database returns 200 or 404 response`() {
            val response = retrieveAllSleepTrackerDetails()
            if (response.status == 200){
                val retrievedSleepTrackerDetails = jsonNodeToObject<Array<SleepTracker>>(response)
                Assertions.assertNotEquals(0, retrievedSleepTrackerDetails.size)
            }
            else{
                Assertions.assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all sleeptrackerdetails by user id when user and sleeptrackerdetails exists returns 200 response`() {
            //Arrange - add a user and 3 associated sleeptrackerdetails that we plan to retrieve
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            addSleepTrackerDetails(
                sleepTrackerDetails[0].hours, sleepTrackerDetails[0].date, addedUser.id
            )
            addSleepTrackerDetails(
                sleepTrackerDetails[1].hours, sleepTrackerDetails[1].date, addedUser.id
            )
            addSleepTrackerDetails(
                sleepTrackerDetails[2].hours, sleepTrackerDetails[2].date, addedUser.id
            )

            //Assert and Act - retrieve the three added sleeptrackerdetails by user id
            val response = retrieveSleepTrackerDetailsByUserId(addedUser.id)
            Assertions.assertEquals(200, response.status)
            val retrievedSleepTrackerDetails = jsonNodeToObject<Array<SleepTracker>>(response)
            Assertions.assertEquals(3, retrievedSleepTrackerDetails.size)

            //After - delete the added user and assert a 204 is returned (sleeptrackerdetails are cascade deleted)
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all sleeptrackerdetails by user id when no sleeptrackerdetails exist returns 404 response`() {
            //Arrange - add a user
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the sleeptrackerdetails by user id
            val response = retrieveSleepTrackerDetailsByUserId(addedUser.id)
            Assertions.assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all sleeptrackerdetails by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve sleeptrackerdetails by user id
            val response = retrieveSleepTrackerDetailsByUserId(userId)
            Assertions.assertEquals(404, response.status)
        }

        @Test
        fun `get sleeptrackerdetails by sleeptracker id when no sleeptrackerdetails exists returns 404 response`() {
            //Arrange
            val sleepTrackerId = -1
            //Assert and Act - attempt to retrieve the sleeptrackerdetails by sleeptracker id
            val response = retrieveSleepTrackerDetailsBySleepTrackerId(sleepTrackerId)
            Assertions.assertEquals(404, response.status)
        }


        @Test
        fun `get sleeptrackerdetails by sleeptracker id when sleeptrackerdetails exists returns 200 response`() {
            //Arrange - add a user and associated sleeptrackerdetails
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addSleepTrackerResponse = addSleepTrackerDetails(
                sleepTrackerDetails[0].hours, sleepTrackerDetails[0].date, addedUser.id
            )
            Assertions.assertEquals(201, addSleepTrackerResponse.status)
            val addedSleepTracker = jsonNodeToObject<SleepTracker>(addSleepTrackerResponse)

            //Act & Assert - retrieve the sleeptrackerdetails by sleeptracker id
            val response = retrieveSleepTrackerDetailsBySleepTrackerId(addedSleepTracker.id)
            Assertions.assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class UpdateSleepTrackerDetails {

        @Test
        fun `updating an sleeptrackerdetails by sleeptracker id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val sleepTrackerId = -1

            //Arrange - check there is no user for -1 id
            Assertions.assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an sleeptrackerdetails/user that doesn't exist
            Assertions.assertEquals(
                404, updateSleepTrackerDetails(
                    sleepTrackerId, updatedHours, updatedDate, userId
                ).status
            )
        }

        @Test
        fun `updating an sleeptrackerdetails by sleeptrackerdetails id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated sleeptrackerdetails that we plan to do an update on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addSleepTrackerResponse = addSleepTrackerDetails(
                sleepTrackerDetails[0].hours, sleepTrackerDetails[0].date, addedUser.id
            )
            Assertions.assertEquals(201, addSleepTrackerResponse.status)
            val addedSleepTracker = jsonNodeToObject<SleepTracker>(addSleepTrackerResponse)

            //Act & Assert - update the added sleeptrackerdetails and assert a 204 is returned
            val updatedSleepTrackerResponse = updateSleepTrackerDetails(addedSleepTracker.id, updatedHours, updatedDate, addedUser.id)
            Assertions.assertEquals(204, updatedSleepTrackerResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedSleepTrackerResponse = retrieveSleepTrackerDetailsBySleepTrackerId(addedSleepTracker.id)
            val updatedSleepTrackerDetails = jsonNodeToObject<SleepTracker>(retrievedSleepTrackerResponse)
            Assertions.assertEquals(updatedHours, updatedSleepTrackerDetails.hours)
            Assertions.assertEquals(updatedDate, updatedSleepTrackerDetails.date)

            //After - delete the user
            deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteSleepTrackerDetails {

        @Test
        fun `deleting sleeptrackerdetails by sleeptracker id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, deleteSleepTrackerDetailsBySleepTrackerId(-1).status)
        }

        @Test
        fun `deleting sleeptrackerdetails by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, deleteSleepTrackerDetailsByUserId(-1).status)
        }

        @Test
        fun `deleting an sleeptrackerdetails by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated sleeptrackerdetails that we plan to do a delete on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addSleepTrackerResponse = addSleepTrackerDetails(
                sleepTrackerDetails[0].hours, sleepTrackerDetails[0].date, addedUser.id
            )
            Assertions.assertEquals(201, addSleepTrackerResponse.status)

            //Act & Assert - delete the added sleeptrackerdetails and assert a 204 is returned
            val addedSleepTracker = jsonNodeToObject<SleepTracker>(addSleepTrackerResponse)
            Assertions.assertEquals(204, deleteSleepTrackerDetailsBySleepTrackerId(addedSleepTracker.id).status)

            //After - delete the user
            deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all sleeptrackerdetails by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated sleeptrackerdetails that we plan to do a cascade delete
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addSleepTrackerResponse1 = addSleepTrackerDetails(
                sleepTrackerDetails[0].hours, sleepTrackerDetails[0].date, addedUser.id
            )
            Assertions.assertEquals(201, addSleepTrackerResponse1.status)
            val addSleepTrackerResponse2 = addSleepTrackerDetails(
                sleepTrackerDetails[1].hours, sleepTrackerDetails[1].date, addedUser.id
            )
            Assertions.assertEquals(201, addSleepTrackerResponse2.status)
            val addSleepTrackerResponse3 = addSleepTrackerDetails(
                sleepTrackerDetails[2].hours, sleepTrackerDetails[2].date, addedUser.id
            )
            Assertions.assertEquals(201, addSleepTrackerResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted sleeptrackerdetails
            val addedSleepTracker1 = jsonNodeToObject<SleepTracker>(addSleepTrackerResponse1)
            val addedSleepTracker2 = jsonNodeToObject<SleepTracker>(addSleepTrackerResponse2)
            val addedSleepTracker3 = jsonNodeToObject<SleepTracker>(addSleepTrackerResponse3)
            Assertions.assertEquals(404, retrieveSleepTrackerDetailsBySleepTrackerId(addedSleepTracker1.id).status)
            Assertions.assertEquals(404, retrieveSleepTrackerDetailsBySleepTrackerId(addedSleepTracker2.id).status)
            Assertions.assertEquals(404, retrieveSleepTrackerDetailsBySleepTrackerId(addedSleepTracker3.id).status)
        }
    }
}