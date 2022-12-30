package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.GoalSetting
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GoalSettingControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateGoalSettingDetails {

        @Test
        fun `add an input for goalsettingdetails when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated input for goalsetting that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addGoalSettingResponse = addGoalSettingDetails(
                goalSettingDetails[0].month, goalSettingDetails[0].bodyFatPercentage, goalSettingDetails[0].kilosReducedPerMonth,
                goalSettingDetails[0].date, addedUser.id
            )
            assertEquals(201, addGoalSettingResponse.status)

            //After - delete the user (goalsetting will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add goalsettingdetails when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addGoalSettingResponse = addGoalSettingDetails(
                goalSettingDetails[0].month, goalSettingDetails[0].bodyFatPercentage, goalSettingDetails[0].kilosReducedPerMonth,
                goalSettingDetails[0].date, userId
            )
            assertEquals(404, addGoalSettingResponse.status)
        }
    }

    @Nested
    inner class ReadGoalSettingDetails {

        @Test
        fun `get all goalsettingdetails from the database returns 200 or 404 response`() {
            val response = retrieveAllGoalSettingDetails()
            if (response.status == 200){
                val retrievedGoalSettingDetails = jsonNodeToObject<Array<GoalSetting>>(response)
                assertNotEquals(0, retrievedGoalSettingDetails.size)
            }
            else{
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all goalsettingdetails by user id when user and goalsettingdetails exists returns 200 response`() {
            //Arrange - add a user and 3 associated goalsettingdetails that we plan to retrieve
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            addGoalSettingDetails(
                goalSettingDetails[0].month, goalSettingDetails[0].bodyFatPercentage, goalSettingDetails[0].kilosReducedPerMonth,
                goalSettingDetails[0].date, addedUser.id
            )
            addGoalSettingDetails(
                goalSettingDetails[1].month, goalSettingDetails[1].bodyFatPercentage, goalSettingDetails[1].kilosReducedPerMonth,
                goalSettingDetails[1].date, addedUser.id
            )
            addGoalSettingDetails(
                goalSettingDetails[2].month, goalSettingDetails[2].bodyFatPercentage, goalSettingDetails[2].kilosReducedPerMonth,
                goalSettingDetails[2].date, addedUser.id
            )

            //Assert and Act - retrieve the three added goalsettingdetails by user id
            val response = retrieveGoalSettingDetailsByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedGoalSettingDetails = jsonNodeToObject<Array<GoalSetting>>(response)
            assertEquals(3, retrievedGoalSettingDetails.size)

            //After - delete the added user and assert a 204 is returned (goalsettingdetails are cascade deleted)
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all goalsettingdetails by user id when no goalsettingdetails exist returns 404 response`() {
            //Arrange - add a user
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the goalsettingdetails by user id
            val response = retrieveGoalSettingDetailsByUserId(addedUser.id)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all goalsettingdetails by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve goalsettingdetails by user id
            val response = retrieveGoalSettingDetailsByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get goalsettingdetails by goalsetting id when no goalsettingdetails exists returns 404 response`() {
            //Arrange
            val goalSettingId = -1
            //Assert and Act - attempt to retrieve the goalsettingdetails by goalsetting id
            val response = retrieveGoalSettingDetailsByGoalSettingId(goalSettingId)
            assertEquals(404, response.status)
        }


        @Test
        fun `get goalsettingdetails by goalsetting id when goalsettingdetails exists returns 200 response`() {
            //Arrange - add a user and associated goalsettingdetails
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addGoalSettingResponse = addGoalSettingDetails(
                goalSettingDetails[0].month, goalSettingDetails[0].bodyFatPercentage, goalSettingDetails[0].kilosReducedPerMonth,
                goalSettingDetails[0].date, addedUser.id
            )
            assertEquals(201, addGoalSettingResponse.status)
            val addedGoalSetting = jsonNodeToObject<GoalSetting>(addGoalSettingResponse)

            //Act & Assert - retrieve the goalsettingdetails by goalsetting id
            val response = retrieveGoalSettingDetailsByGoalSettingId(addedGoalSetting.id)
            assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class UpdateGoalSettingDetails {

        @Test
        fun `updating an goalsettingdetails by goalsetting id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val goalSettingId = -1

            //Arrange - check there is no user for -1 id
            assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an goalsettingdetails/user that doesn't exist
            assertEquals(
                404, updateGoalSettingDetails(
                    goalSettingId, updatedMonth, updatedBodyFatPercentage, updatedKilosReducedPerMonth,
                    updatedGoalSettingDate, userId
                ).status
            )
        }

        @Test
        fun `updating an goalsettingdetails by goalsettingdetails id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated goalsettingdetails that we plan to do an update on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addGoalSettingResponse = addGoalSettingDetails(
                goalSettingDetails[0].month, goalSettingDetails[0].bodyFatPercentage, goalSettingDetails[0].kilosReducedPerMonth,
                goalSettingDetails[0].date, addedUser.id
            )
            assertEquals(201, addGoalSettingResponse.status)
            val addedGoalSetting = jsonNodeToObject<GoalSetting>(addGoalSettingResponse)

            //Act & Assert - update the added goalsettingdetails and assert a 204 is returned
            val updatedGoalSettingResponse = updateGoalSettingDetails(addedGoalSetting.id, updatedMonth, updatedBodyFatPercentage, updatedKilosReducedPerMonth,
                updatedGoalSettingDate, addedUser.id)
            assertEquals(204, updatedGoalSettingResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedGoalSettingResponse = retrieveGoalSettingDetailsByGoalSettingId(addedGoalSetting.id)
            val updatedGoalSettingDetails = jsonNodeToObject<GoalSetting>(retrievedGoalSettingResponse)
            assertEquals(updatedMonth, updatedGoalSettingDetails.month)
            assertEquals(updatedBodyFatPercentage, updatedGoalSettingDetails.bodyFatPercentage)
            assertEquals(updatedKilosReducedPerMonth, updatedGoalSettingDetails.kilosReducedPerMonth)
            assertEquals(updatedGoalSettingDate, updatedGoalSettingDetails.date)

            //After - delete the user
            deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteGoalSettingDetails {

        @Test
        fun `deleting goalsettingdetails by goalsetting id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteGoalSettingDetailsByGoalSettingId(-1).status)
        }

        @Test
        fun `deleting goalsettingdetails by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteGoalSettingDetailsByUserId(-1).status)
        }

        @Test
        fun `deleting an goalsettingdetails by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated goalsettingdetails that we plan to do a delete on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addGoalSettingResponse = addGoalSettingDetails(
                goalSettingDetails[0].month, goalSettingDetails[0].bodyFatPercentage, goalSettingDetails[0].kilosReducedPerMonth,
                goalSettingDetails[0].date, addedUser.id
            )
            assertEquals(201, addGoalSettingResponse.status)

            //Act & Assert - delete the added goalsettingdetails and assert a 204 is returned
            val addedGoalSetting = jsonNodeToObject<GoalSetting>(addGoalSettingResponse)
            assertEquals(204, deleteGoalSettingDetailsByGoalSettingId(addedGoalSetting.id).status)

            //After - delete the user
            deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all goalsettingdetails by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated goalsettingdetails that we plan to do a cascade delete
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addGoalSettingResponse1 = addGoalSettingDetails(
                goalSettingDetails[0].month, goalSettingDetails[0].bodyFatPercentage, goalSettingDetails[0].kilosReducedPerMonth,
                goalSettingDetails[0].date, addedUser.id
            )
            assertEquals(201, addGoalSettingResponse1.status)
            val addGoalSettingResponse2 = addGoalSettingDetails(
                goalSettingDetails[1].month, goalSettingDetails[1].bodyFatPercentage, goalSettingDetails[1].kilosReducedPerMonth,
                goalSettingDetails[1].date, addedUser.id
            )
            assertEquals(201, addGoalSettingResponse2.status)
            val addGoalSettingResponse3 = addGoalSettingDetails(
                goalSettingDetails[2].month, goalSettingDetails[2].bodyFatPercentage, goalSettingDetails[2].kilosReducedPerMonth,
                goalSettingDetails[2].date, addedUser.id
            )
            assertEquals(201, addGoalSettingResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted goalsettingdetails
            val addedGoalSetting1 = jsonNodeToObject<GoalSetting>(addGoalSettingResponse1)
            val addedGoalSetting2 = jsonNodeToObject<GoalSetting>(addGoalSettingResponse2)
            val addedGoalSetting3 = jsonNodeToObject<GoalSetting>(addGoalSettingResponse3)
            assertEquals(404, retrieveGoalSettingDetailsByGoalSettingId(addedGoalSetting1.id).status)
            assertEquals(404, retrieveGoalSettingDetailsByGoalSettingId(addedGoalSetting2.id).status)
            assertEquals(404, retrieveGoalSettingDetailsByGoalSettingId(addedGoalSetting3.id).status)
        }
    }
}