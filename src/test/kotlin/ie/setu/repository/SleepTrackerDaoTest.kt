package ie.setu.repository

import ie.setu.domain.SleepTracker
import ie.setu.domain.db.SleepTracking
import ie.setu.domain.repository.SleepTrackerDAO
import ie.setu.helpers.sleepTrackerDetails
import ie.setu.helpers.populateSleepTrackingTable
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
private val sleepTracker1 = sleepTrackerDetails.get(0)
private val sleepTracker2 = sleepTrackerDetails.get(1)
private val sleepTracker3 = sleepTrackerDetails.get(2)

class SleepTrackerDaoTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateSleepTrackerDetails {

        @Test
        fun `multiple sleepTrackerDetails added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three sleepTrackerDetails
                val userDAO = populateUserTable()
                val sleepTrackerDAO = populateSleepTrackingTable()
                //Act & Assert
                assertEquals(3, sleepTrackerDAO.getAll().size)
                assertEquals(sleepTracker1, sleepTrackerDAO.findBySleepTrackerId(sleepTracker1.id))
                assertEquals(sleepTracker2, sleepTrackerDAO.findBySleepTrackerId(sleepTracker2.id))
                assertEquals(sleepTracker3, sleepTrackerDAO.findBySleepTrackerId(sleepTracker3.id))
            }
        }
    }

    @Nested
    inner class ReadSleepTrackerDetails {

        @Test
        fun `getting all sleepTrackerDetails from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three sleepTrackerDetails
                val userDAO = populateUserTable()
                val sleepTrackerDAO = populateSleepTrackingTable()
                //Act & Assert
                assertEquals(3, sleepTrackerDAO.getAll().size)
            }
        }

        @Test
        fun `get sleepTracker by user id that has no sleepTrackerDetails, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three sleepTrackerDetails
                val userDAO = populateUserTable()
                val sleepTrackerDAO = populateSleepTrackingTable()
                //Act & Assert
                assertEquals(0, sleepTrackerDAO.findByUserId(6).size)
            }
        }

        @Test
        fun `get sleepTracker by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three sleepTrackerDetails
                val userDAO = populateUserTable()
                val sleepTrackerDAO = populateSleepTrackingTable()
                //Act & Assert
                assertEquals(sleepTracker1, sleepTrackerDAO.findByUserId(1).get(0))
                assertEquals(sleepTracker2, sleepTrackerDAO.findByUserId(2).get(0))
                assertEquals(sleepTracker3, sleepTrackerDAO.findByUserId(3).get(0))
            }
        }

        @Test
        fun `get all sleepTrackerDetails over empty table returns none`() {
            transaction {

                //Arrange - create and setup sleepTrackerDAO object
                SchemaUtils.create(SleepTracking)
                val sleepTrackerDAO = SleepTrackerDAO()

                //Act & Assert
                assertEquals(0, sleepTrackerDAO.getAll().size)
            }
        }

        @Test
        fun `get sleepTracker by sleepTracker id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three sleepTrackerDetails
                val userDAO = populateUserTable()
                val sleepTrackerDAO = populateSleepTrackingTable()
                //Act & Assert
                assertEquals(null, sleepTrackerDAO.findBySleepTrackerId(4))
            }
        }

        @Test
        fun `get sleepTracker by sleepTracker id that exists, results in a correct sleepTracker returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three sleepTrackerDetails
                val userDAO = populateUserTable()
                val sleepTrackerDAO = populateSleepTrackingTable()
                //Act & Assert
                assertEquals(sleepTracker1, sleepTrackerDAO.findBySleepTrackerId(1))
                assertEquals(sleepTracker3, sleepTrackerDAO.findBySleepTrackerId(3))
            }
        }
    }

    @Nested
    inner class UpdateSleepTrackerDetails {

        @Test
        fun `updating existing sleepTracker in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three sleepTrackerDetails
                val userDAO = populateUserTable()
                val sleepTrackerDAO = populateSleepTrackingTable()

                //Act & Assert
                val sleepTracker3updated = SleepTracker(id = 3, hours = 8.0, date = DateTime.now(), userId = 2)
                sleepTrackerDAO.updateBySleepTrackerId(sleepTracker3updated.id, sleepTracker3updated)
                assertEquals(sleepTracker3updated, sleepTrackerDAO.findBySleepTrackerId(3))
            }
        }

        @Test
        fun `updating non-existant sleepTracker in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three sleepTrackerDetails
                val userDAO = populateUserTable()
                val sleepTrackerDAO = populateSleepTrackingTable()

                //Act & Assert
                val sleepTracker4updated = SleepTracker(id = 4, hours = 6.0, date = DateTime.now(), userId = 2)
                sleepTrackerDAO.updateBySleepTrackerId(4, sleepTracker4updated)
                assertEquals(null, sleepTrackerDAO.findBySleepTrackerId(4))
                assertEquals(3, sleepTrackerDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteSleepTrackerDetails {

        @Test
        fun `deleting a non-existant sleepTracker (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three sleepTrackerDetails
                val userDAO = populateUserTable()
                val sleepTrackerDAO = populateSleepTrackingTable()

                //Act & Assert
                assertEquals(3, sleepTrackerDAO.getAll().size)
                sleepTrackerDAO.deleteBySleepTrackerId(4)
                assertEquals(3, sleepTrackerDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing sleepTracker (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three sleepTrackerDetails
                val userDAO = populateUserTable()
                val sleepTrackerDAO = populateSleepTrackingTable()

                //Act & Assert
                assertEquals(3, sleepTrackerDAO.getAll().size)
                sleepTrackerDAO.deleteBySleepTrackerId(sleepTracker3.id)
                assertEquals(2, sleepTrackerDAO.getAll().size)
            }
        }


        @Test
        fun `deleting sleepTrackerDetails when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three sleepTrackerDetails
                val userDAO = populateUserTable()
                val sleepTrackerDAO = populateSleepTrackingTable()

                //Act & Assert
                assertEquals(3, sleepTrackerDAO.getAll().size)
                sleepTrackerDAO.deleteByUserId(6)
                assertEquals(3, sleepTrackerDAO.getAll().size)
            }
        }

        @Test
        fun `deleting sleepTrackerDetails when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three sleepTrackerDetails
                val userDAO = populateUserTable()
                val sleepTrackerDAO = populateSleepTrackingTable()

                //Act & Assert
                assertEquals(3, sleepTrackerDAO.getAll().size)
                sleepTrackerDAO.deleteByUserId(1)
                assertEquals(2, sleepTrackerDAO.getAll().size)
            }
        }
    }
}