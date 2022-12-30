package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.FoodTracker
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FoodTrackerControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateFoodTrackerDetails {

        @Test
        fun `add an input for foodtracker when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated input for foodtracker that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addFoodTrackerResponse = addFoodTrackerDetails(
                foodTrackerDetails[0].meal, foodTrackerDetails[0].caloriesIntake,
                foodTrackerDetails[0].date, addedUser.id
            )
            assertEquals(201, addFoodTrackerResponse.status)

            //After - delete the user (FoodTracker will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add foodtrackerdetails when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addFoodTrackerResponse = addFoodTrackerDetails(
                foodTrackerDetails[0].meal, foodTrackerDetails[0].caloriesIntake,
                foodTrackerDetails[0].date, userId
            )
            assertEquals(404, addFoodTrackerResponse.status)
        }
    }

    @Nested
    inner class ReadFoodTrackerDetails {

        @Test
        fun `get all foodtrackerdetails from the database returns 200 or 404 response`() {
            val response = retrieveAllFoodTrackerDetails()
            if (response.status == 200){
                val retrievedFoodTrackerDetails = jsonNodeToObject<Array<FoodTracker>>(response)
                assertNotEquals(0, retrievedFoodTrackerDetails.size)
            }
            else{
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all foodtrackerdetails by user id when user and foodtrackerdetails exists returns 200 response`() {
            //Arrange - add a user and 3 associated foodtrackerdetails that we plan to retrieve
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            addFoodTrackerDetails(
                foodTrackerDetails[0].meal, foodTrackerDetails[0].caloriesIntake,
                foodTrackerDetails[0].date, addedUser.id
            )
            addFoodTrackerDetails(
                foodTrackerDetails[1].meal, foodTrackerDetails[1].caloriesIntake,
                foodTrackerDetails[1].date, addedUser.id
            )
            addFoodTrackerDetails(
                foodTrackerDetails[2].meal, foodTrackerDetails[2].caloriesIntake,
                foodTrackerDetails[2].date, addedUser.id
            )

            //Assert and Act - retrieve the three added foodtrackerdetails by user id
            val response = retrieveFoodTrackerDetailsByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedFoodTrackerDetails = jsonNodeToObject<Array<FoodTracker>>(response)
            assertEquals(3, retrievedFoodTrackerDetails.size)

            //After - delete the added user and assert a 204 is returned (foodtrackerdetails are cascade deleted)
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all foodtrackerdetails by user id when no foodtrackerdetails exist returns 404 response`() {
            //Arrange - add a user
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the foodtrackerdetails by user id
            val response = retrieveFoodTrackerDetailsByUserId(addedUser.id)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all foodtrackerdetails by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve foodtrackerdetails by user id
            val response = retrieveFoodTrackerDetailsByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get foodtrackerdetails by foodtracker id when no foodtrackerdetails exists returns 404 response`() {
            //Arrange
            val foodTrackerId = -1
            //Assert and Act - attempt to retrieve the foodtrackerdetails by foodtracker id
            val response = retrieveFoodTrackerdetailsByFoodTrackerId(foodTrackerId)
            assertEquals(404, response.status)
        }


        @Test
        fun `get foodtrackerdetails by foodtracker id when foodtrackerdetails exists returns 200 response`() {
            //Arrange - add a user and associated foodtrackerdetails
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addFoodTrackerResponse = addFoodTrackerDetails(
                foodTrackerDetails[0].meal, foodTrackerDetails[0].caloriesIntake,
                foodTrackerDetails[0].date, addedUser.id
            )
            assertEquals(201, addFoodTrackerResponse.status)
            val addedFoodTracker = jsonNodeToObject<FoodTracker>(addFoodTrackerResponse)

            //Act & Assert - retrieve the foodtrackerdetails by foodtracker id
            val response = retrieveFoodTrackerdetailsByFoodTrackerId(addedFoodTracker.id)
            assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class UpdateFoodTrackerDetails {

        @Test
        fun `updating an foodtrackerdetails by foodtracker id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val foodTrackerId = -1

            //Arrange - check there is no user for -1 id
            assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an foodtrackerdetails/user that doesn't exist
            assertEquals(
                404, updateFoodTrackerDetails(
                    foodTrackerId, updatedMeal, updatedCaloriesIntake,
                    updatedDate, userId
                ).status
            )
        }

        @Test
        fun `updating an foodtrackerdetails by foodtracker id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated foodtrackerdetails that we plan to do an update on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addFoodTrackerResponse = addFoodTrackerDetails(
                foodTrackerDetails[0].meal, foodTrackerDetails[0].caloriesIntake,
                foodTrackerDetails[0].date, addedUser.id
            )
            assertEquals(201, addFoodTrackerResponse.status)
            val addedFoodTracker = jsonNodeToObject<FoodTracker>(addFoodTrackerResponse)

            //Act & Assert - update the added foodtrackerdetails and assert a 204 is returned
            val updatedFoodTrackerResponse = updateFoodTrackerDetails(addedFoodTracker.id, updatedMeal, updatedCaloriesIntake,
                updatedDate, addedUser.id)
            assertEquals(204, updatedFoodTrackerResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedFoodTrackerResponse = retrieveFoodTrackerdetailsByFoodTrackerId(addedFoodTracker.id)
            val updatedFoodTrackerDetails = jsonNodeToObject<FoodTracker>(retrievedFoodTrackerResponse)
            assertEquals(updatedMeal, updatedFoodTrackerDetails.meal)
            assertEquals(updatedCaloriesIntake, updatedFoodTrackerDetails.caloriesIntake)
            assertEquals(updatedDate, updatedFoodTrackerDetails.date)

            //After - delete the user
            deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteFoodTrackerDetails {

        @Test
        fun `deleting foodtrackerdetails by foodtracker id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteFoodTrackerdetailsByFoodTrackerId(-1).status)
        }

        @Test
        fun `deleting foodtrackerdetails by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteFoodTrackerdetailsByUserId(-1).status)
        }

        @Test
        fun `deleting an foodtrackerdetails by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated foodtrackerdetails that we plan to do a delete on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addFoodTrackerResponse = addFoodTrackerDetails(
                foodTrackerDetails[0].meal, foodTrackerDetails[0].caloriesIntake,
                foodTrackerDetails[0].date, addedUser.id
            )
            assertEquals(201, addFoodTrackerResponse.status)

            //Act & Assert - delete the added foodtrackerdetails and assert a 204 is returned
            val addedFoodTracker = jsonNodeToObject<FoodTracker>(addFoodTrackerResponse)
            assertEquals(204, deleteFoodTrackerdetailsByFoodTrackerId(addedFoodTracker.id).status)

            //After - delete the user
            deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all foodtrackerdetails by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated foodtrackerdetails that we plan to do a cascade delete
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addFoodTrackerResponse1 = addFoodTrackerDetails(
                foodTrackerDetails[0].meal, foodTrackerDetails[0].caloriesIntake,
                foodTrackerDetails[0].date, addedUser.id
            )
            assertEquals(201, addFoodTrackerResponse1.status)
            val addFoodTrackerResponse2 = addFoodTrackerDetails(
                foodTrackerDetails[1].meal, foodTrackerDetails[1].caloriesIntake,
                foodTrackerDetails[1].date, addedUser.id
            )
            assertEquals(201, addFoodTrackerResponse2.status)
            val addFoodTrackerResponse3 = addFoodTrackerDetails(
                foodTrackerDetails[2].meal, foodTrackerDetails[2].caloriesIntake,
                foodTrackerDetails[2].date, addedUser.id
            )
            assertEquals(201, addFoodTrackerResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted foodtrackerdetails
            val addedFoodTracker1 = jsonNodeToObject<FoodTracker>(addFoodTrackerResponse1)
            val addedFoodTracker2 = jsonNodeToObject<FoodTracker>(addFoodTrackerResponse2)
            val addedFoodTracker3 = jsonNodeToObject<FoodTracker>(addFoodTrackerResponse3)
            assertEquals(404, retrieveFoodTrackerdetailsByFoodTrackerId(addedFoodTracker1.id).status)
            assertEquals(404, retrieveFoodTrackerdetailsByFoodTrackerId(addedFoodTracker2.id).status)
            assertEquals(404, retrieveFoodTrackerdetailsByFoodTrackerId(addedFoodTracker3.id).status)
        }
    }
}