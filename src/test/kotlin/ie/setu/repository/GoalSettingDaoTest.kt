package ie.setu.repository

import ie.setu.domain.GoalSetting
import ie.setu.domain.db.GoalSetters
import ie.setu.domain.repository.GoalSettingDAO
import ie.setu.helpers.goalSettingDetails
import ie.setu.helpers.populateGoalSettersTable
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
private val goalSetting1 = goalSettingDetails.get(0)
private val goalSetting2 = goalSettingDetails.get(1)
private val goalSetting3 = goalSettingDetails.get(2)

class GoalSettingDaoTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateGoalSettingDetails {

        @Test
        fun `multiple goalSettingDetails added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three goalSettingDetails
                val userDAO = populateUserTable()
                val goalSettingDAO = populateGoalSettersTable()
                //Act & Assert
                assertEquals(3, goalSettingDAO.getAll().size)
                assertEquals(goalSetting1, goalSettingDAO.findByGoalSettingId(goalSetting1.id))
                assertEquals(goalSetting2, goalSettingDAO.findByGoalSettingId(goalSetting2.id))
                assertEquals(goalSetting3, goalSettingDAO.findByGoalSettingId(goalSetting3.id))
            }
        }
    }

    @Nested
    inner class ReadGoalSettingDetails {

        @Test
        fun `getting all GoalSettingDetails from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three goalSettingDetails
                val userDAO = populateUserTable()
                val goalSettingDAO = populateGoalSettersTable()
                //Act & Assert
                assertEquals(3, goalSettingDAO.getAll().size)
            }
        }

        @Test
        fun `get goalSetting by user id that has no goalSettingDetails, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three goalSettingDetails
                val userDAO = populateUserTable()
                val goalSettingDAO = populateGoalSettersTable()
                //Act & Assert
                assertEquals(0, goalSettingDAO.findByUserId(6).size)
            }
        }

        @Test
        fun `get goalSetting by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three goalSettingDetails
                val userDAO = populateUserTable()
                val goalSettingDAO = populateGoalSettersTable()
                //Act & Assert
                assertEquals(goalSetting1, goalSettingDAO.findByUserId(1).get(0))
                assertEquals(goalSetting2, goalSettingDAO.findByUserId(2).get(0))
                assertEquals(goalSetting3, goalSettingDAO.findByUserId(3).get(0))
            }
        }

        @Test
        fun `get all goalSettingDetails over empty table returns none`() {
            transaction {

                //Arrange - create and setup goalSettingDAO object
                SchemaUtils.create(GoalSetters)
                val goalSettingDAO = GoalSettingDAO()

                //Act & Assert
                assertEquals(0, goalSettingDAO.getAll().size)
            }
        }

        @Test
        fun `get goalSetting by goalSetting id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three goalSettingDetails
                val userDAO = populateUserTable()
                val goalSettingDAO = populateGoalSettersTable()
                //Act & Assert
                assertEquals(null, goalSettingDAO.findByGoalSettingId(4))
            }
        }

        @Test
        fun `get goalSetting by goalSetting id that exists, results in a correct goalSetting returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three goalSettingDetails
                val userDAO = populateUserTable()
                val goalSettingDAO = populateGoalSettersTable()
                //Act & Assert
                assertEquals(goalSetting1, goalSettingDAO.findByGoalSettingId(1))
                assertEquals(goalSetting3, goalSettingDAO.findByGoalSettingId(3))
            }
        }
    }

    @Nested
    inner class UpdateGoalSettingDetails {

        @Test
        fun `updating existing goalSetting in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three goalSettingDetails
                val userDAO = populateUserTable()
                val goalSettingDAO = populateGoalSettersTable()

                //Act & Assert
                val goalSetting3updated = GoalSetting(id = 3, month = "June", bodyFatPercentage = 33.0, kilosReducedPerMonth = 8, date = DateTime.now(),
                    userId = 2)
                goalSettingDAO.updateByGoalSettingId(goalSetting3updated.id, goalSetting3updated)
                assertEquals(goalSetting3updated, goalSettingDAO.findByGoalSettingId(3))
            }
        }

        @Test
        fun `updating non-existant goalSetting in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three goalSettingDetails
                val userDAO = populateUserTable()
                val goalSettingDAO = populateGoalSettersTable()

                //Act & Assert
                val goalSetting4updated = GoalSetting(id = 4, month = "July", bodyFatPercentage = 44.0, kilosReducedPerMonth = 12, date = DateTime.now(),
                    userId = 2)
                goalSettingDAO.updateByGoalSettingId(4, goalSetting4updated)
                assertEquals(null, goalSettingDAO.findByGoalSettingId(4))
                assertEquals(3, goalSettingDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteGoalSettingDetails {

        @Test
        fun `deleting a non-existant goalSetting (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three goalSettingDetails
                val userDAO = populateUserTable()
                val goalSettingDAO = populateGoalSettersTable()

                //Act & Assert
                assertEquals(3, goalSettingDAO.getAll().size)
                goalSettingDAO.deleteByGoalSettingId(4)
                assertEquals(3, goalSettingDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing goalSetting (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three goalSettingDetails
                val userDAO = populateUserTable()
                val goalSettingDAO = populateGoalSettersTable()

                //Act & Assert
                assertEquals(3, goalSettingDAO.getAll().size)
                goalSettingDAO.deleteByGoalSettingId(goalSetting3.id)
                assertEquals(2, goalSettingDAO.getAll().size)
            }
        }


        @Test
        fun `deleting goalSettingDetails when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three goalSettingDetails
                val userDAO = populateUserTable()
                val goalSettingDAO = populateGoalSettersTable()

                //Act & Assert
                assertEquals(3, goalSettingDAO.getAll().size)
                goalSettingDAO.deleteByUserId(6)
                assertEquals(3, goalSettingDAO.getAll().size)
            }
        }

        @Test
        fun `deleting goalSettingDetails when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three goalSettingDetails
                val userDAO = populateUserTable()
                val goalSettingDAO = populateGoalSettersTable()

                //Act & Assert
                assertEquals(3, goalSettingDAO.getAll().size)
                goalSettingDAO.deleteByUserId(1)
                assertEquals(2, goalSettingDAO.getAll().size)
            }
        }
    }
}