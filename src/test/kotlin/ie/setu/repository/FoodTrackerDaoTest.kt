package ie.setu.repository

import ie.setu.domain.FoodTracker
import ie.setu.domain.db.FoodIntakeTracker
import ie.setu.domain.repository.FoodTrackerDAO
import ie.setu.helpers.foodTrackerDetails
import ie.setu.helpers.populateFoodIntakeTrackerTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val foodTracker1 = foodTrackerDetails.get(0)
private val foodTracker2 = foodTrackerDetails.get(1)
private val foodTracker3 = foodTrackerDetails.get(2)


class FoodTrackerDaoTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateFoodTrackerDetails {

        @Test
        fun `multiple foodTrackerDetails added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three foodTrackerDetails
                val userDAO = populateUserTable()
                val foodTrackerDAO = populateFoodIntakeTrackerTable()
                //Act & Assert
                assertEquals(3, foodTrackerDAO.getAll().size)
                assertEquals(foodTracker1, foodTrackerDAO.findByFoodTrackerId(foodTracker1.id))
                assertEquals(foodTracker2, foodTrackerDAO.findByFoodTrackerId(foodTracker2.id))
                assertEquals(foodTracker3, foodTrackerDAO.findByFoodTrackerId(foodTracker3.id))
            }
        }
    }

    @Nested
    inner class ReadFoodTrackerDetails {

        @Test
        fun `getting all activites from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three foodTrackerDetails
                val userDAO = populateUserTable()
                val foodTrackerDAO = populateFoodIntakeTrackerTable()
                //Act & Assert
                assertEquals(3, foodTrackerDAO.getAll().size)
            }
        }

        @Test
        fun `get foodTracker by user id that has no foodTrackerDetails, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three foodTrackerDetails
                val userDAO = populateUserTable()
                val foodTrackerDAO = populateFoodIntakeTrackerTable()
                //Act & Assert
                assertEquals(0, foodTrackerDAO.findByUserId(6).size)
            }
        }

        @Test
        fun `get foodTracker by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three foodTrackerDetails
                val userDAO = populateUserTable()
                val foodTrackerDAO = populateFoodIntakeTrackerTable()
                //Act & Assert
                assertEquals(foodTracker1, foodTrackerDAO.findByUserId(1).get(0))
                assertEquals(foodTracker2, foodTrackerDAO.findByUserId(2).get(0))
                assertEquals(foodTracker3, foodTrackerDAO.findByUserId(3).get(0))
            }
        }

        @Test
        fun `get all foodTrackerDetails over empty table returns none`() {
            transaction {

                //Arrange - create and setup foodTrackerDAO object
                SchemaUtils.create(FoodIntakeTracker)
                val foodTrackerDAO = FoodTrackerDAO()

                //Act & Assert
                assertEquals(0, foodTrackerDAO.getAll().size)
            }
        }

        @Test
        fun `get foodTracker by foodTracker id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three foodTrackerDetails
                val userDAO = populateUserTable()
                val foodTrackerDAO = populateFoodIntakeTrackerTable()
                //Act & Assert
                assertEquals(null, foodTrackerDAO.findByFoodTrackerId(4))
            }
        }

        @Test
        fun `get foodTracker by foodTracker id that exists, results in a correct foodTracker returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three foodTrackerDetails
                val userDAO = populateUserTable()
                val foodTrackerDAO = populateFoodIntakeTrackerTable()
                //Act & Assert
                assertEquals(foodTracker1, foodTrackerDAO.findByFoodTrackerId(1))
                assertEquals(foodTracker3, foodTrackerDAO.findByFoodTrackerId(3))
            }
        }
    }

    @Nested
    inner class UpdateFoodTrackerDetails {

        @Test
        fun `updating existing foodTracker in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three foodTrackerDetails
                val userDAO = populateUserTable()
                val foodTrackerDAO = populateFoodIntakeTrackerTable()

                //Act & Assert
                val foodTracker3updated = FoodTracker(id = 3, meal = "Dinner", caloriesIntake = 120, date = DateTime.now(),
                    userId = 2)
                foodTrackerDAO.updateByFoodTrackerId(foodTracker3updated.id, foodTracker3updated)
                assertEquals(foodTracker3updated, foodTrackerDAO.findByFoodTrackerId(3))
            }
        }

        @Test
        fun `updating non-existant foodTracker in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three foodTrackerDetails
                val userDAO = populateUserTable()
                val foodTrackerDAO = populateFoodIntakeTrackerTable()

                //Act & Assert
                val foodTracker4updated = FoodTracker(id = 4, meal = "Lunch", caloriesIntake = 80, date = DateTime.now(),
                    userId = 2)
                foodTrackerDAO.updateByFoodTrackerId(4, foodTracker4updated)
                assertEquals(null, foodTrackerDAO.findByFoodTrackerId(4))
                assertEquals(3, foodTrackerDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteFoodTrackerDetails {

        @Test
        fun `deleting a non-existant foodTracker (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three foodTrackerDetails
                val userDAO = populateUserTable()
                val foodTrackerDAO = populateFoodIntakeTrackerTable()

                //Act & Assert
                assertEquals(3, foodTrackerDAO.getAll().size)
                foodTrackerDAO.deleteByFoodTrackerId(4)
                assertEquals(3, foodTrackerDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing foodTracker (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three foodTrackerDetails
                val userDAO = populateUserTable()
                val foodTrackerDAO = populateFoodIntakeTrackerTable()

                //Act & Assert
                assertEquals(3, foodTrackerDAO.getAll().size)
                foodTrackerDAO.deleteByFoodTrackerId(foodTracker3.id)
                assertEquals(2, foodTrackerDAO.getAll().size)
            }
        }


        @Test
        fun `deleting foodTrackerDetails when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three foodTrackerDetails
                val userDAO = populateUserTable()
                val foodTrackerDAO = populateFoodIntakeTrackerTable()

                //Act & Assert
                assertEquals(3, foodTrackerDAO.getAll().size)
                foodTrackerDAO.deleteByUserId(6)
                assertEquals(3, foodTrackerDAO.getAll().size)
            }
        }

        @Test
        fun `deleting foodTrackerDetails when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three foodTrackerDetails
                val userDAO = populateUserTable()
                val foodTrackerDAO = populateFoodIntakeTrackerTable()

                //Act & Assert
                assertEquals(3, foodTrackerDAO.getAll().size)
                foodTrackerDAO.deleteByUserId(1)
                assertEquals(2, foodTrackerDAO.getAll().size)
            }
        }
    }
}