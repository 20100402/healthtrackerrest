package ie.setu.repository

import ie.setu.domain.HeartRateMonitor
import ie.setu.domain.db.HeartRateMonitoring
import ie.setu.domain.repository.HeartRateMonitorDAO
import ie.setu.helpers.heartRateMonitorDetails
import ie.setu.helpers.populateHeartRateMonitoringTable
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
private val heartRateMonitor1 = heartRateMonitorDetails.get(0)
private val heartRateMonitor2 = heartRateMonitorDetails.get(1)
private val heartRateMonitor3 = heartRateMonitorDetails.get(2)

class HeartRateMonitorDaoTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateHeartRateMonitorDetails {

        @Test
        fun `multiple heartRateMonitorDetails added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three heartRateMonitorDetails
                val userDAO = populateUserTable()
                val heartRateMonitorDAO = populateHeartRateMonitoringTable()
                //Act & Assert
                assertEquals(3, heartRateMonitorDAO.getAll().size)
                assertEquals(heartRateMonitor1, heartRateMonitorDAO.findByHeartRateMonitorId(heartRateMonitor1.id))
                assertEquals(heartRateMonitor2, heartRateMonitorDAO.findByHeartRateMonitorId(heartRateMonitor2.id))
                assertEquals(heartRateMonitor3, heartRateMonitorDAO.findByHeartRateMonitorId(heartRateMonitor3.id))
            }
        }
    }

    @Nested
    inner class ReadHeartRateMonitorDetails {

        @Test
        fun `getting all heartratemonitordetails from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three heartRateMonitorDetails
                val userDAO = populateUserTable()
                val heartRateMonitorDAO = populateHeartRateMonitoringTable()
                //Act & Assert
                assertEquals(3, heartRateMonitorDAO.getAll().size)
            }
        }

        @Test
        fun `get heartRateMonitor by user id that has no heartRateMonitorDetails, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three heartRateMonitorDetails
                val userDAO = populateUserTable()
                val heartRateMonitorDAO = populateHeartRateMonitoringTable()
                //Act & Assert
                assertEquals(0, heartRateMonitorDAO.findByUserId(6).size)
            }
        }

        @Test
        fun `get heartRateMonitor by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three heartRateMonitorDetails
                val userDAO = populateUserTable()
                val heartRateMonitorDAO = populateHeartRateMonitoringTable()
                //Act & Assert
                assertEquals(heartRateMonitor1, heartRateMonitorDAO.findByUserId(1).get(0))
                assertEquals(heartRateMonitor2, heartRateMonitorDAO.findByUserId(2).get(0))
                assertEquals(heartRateMonitor3, heartRateMonitorDAO.findByUserId(3).get(0))
            }
        }

        @Test
        fun `get all heartRateMonitorDetails over empty table returns none`() {
            transaction {

                //Arrange - create and setup heartRateMonitorDAO object
                SchemaUtils.create(HeartRateMonitoring)
                val heartRateMonitorDAO = HeartRateMonitorDAO()

                //Act & Assert
                assertEquals(0, heartRateMonitorDAO.getAll().size)
            }
        }

        @Test
        fun `get heartRateMonitor by heartRateMonitor id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three heartRateMonitorDetails
                val userDAO = populateUserTable()
                val heartRateMonitorDAO = populateHeartRateMonitoringTable()
                //Act & Assert
                assertEquals(null, heartRateMonitorDAO.findByHeartRateMonitorId(4))
            }
        }

        @Test
        fun `get heartRateMonitor by heartRateMonitor id that exists, results in a correct heartRateMonitor returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three heartRateMonitorDetails
                val userDAO = populateUserTable()
                val heartRateMonitorDAO = populateHeartRateMonitoringTable()
                //Act & Assert
                assertEquals(heartRateMonitor1, heartRateMonitorDAO.findByHeartRateMonitorId(1))
                assertEquals(heartRateMonitor3, heartRateMonitorDAO.findByHeartRateMonitorId(3))
            }
        }
    }

    @Nested
    inner class UpdateHeartRateMonitorDetails {

        @Test
        fun `updating existing heartRateMonitor in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three heartRateMonitorDetails
                val userDAO = populateUserTable()
                val heartRateMonitorDAO = populateHeartRateMonitoringTable()

                //Act & Assert
                val heartRateMonitor3updated = HeartRateMonitor(id = 3, pulse = 80, date = DateTime.now(), userId = 2)
                heartRateMonitorDAO.updateByHeartRateMonitorId(heartRateMonitor3updated.id, heartRateMonitor3updated)
                assertEquals(heartRateMonitor3updated, heartRateMonitorDAO.findByHeartRateMonitorId(3))
            }
        }

        @Test
        fun `updating non-existant heartRateMonitor in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three heartRateMonitorDetails
                val userDAO = populateUserTable()
                val heartRateMonitorDAO = populateHeartRateMonitoringTable()

                //Act & Assert
                val heartRateMonitor4updated = HeartRateMonitor(id = 4, pulse = 120, date = DateTime.now(), userId = 2)
                heartRateMonitorDAO.updateByHeartRateMonitorId(4, heartRateMonitor4updated)
                assertEquals(null, heartRateMonitorDAO.findByHeartRateMonitorId(4))
                assertEquals(3, heartRateMonitorDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteHeartRateMonitorDetails {

        @Test
        fun `deleting a non-existant heartRateMonitor (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three heartRateMonitorDetails
                val userDAO = populateUserTable()
                val heartRateMonitorDAO = populateHeartRateMonitoringTable()

                //Act & Assert
                assertEquals(3, heartRateMonitorDAO.getAll().size)
                heartRateMonitorDAO.deleteByHeartRateMonitorId(4)
                assertEquals(3, heartRateMonitorDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing heartRateMonitor (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three heartRateMonitorDetails
                val userDAO = populateUserTable()
                val heartRateMonitorDAO = populateHeartRateMonitoringTable()

                //Act & Assert
                assertEquals(3, heartRateMonitorDAO.getAll().size)
                heartRateMonitorDAO.deleteByHeartRateMonitorId(heartRateMonitor3.id)
                assertEquals(2, heartRateMonitorDAO.getAll().size)
            }
        }


        @Test
        fun `deleting heartRateMonitorDetails when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three heartRateMonitorDetails
                val userDAO = populateUserTable()
                val heartRateMonitorDAO = populateHeartRateMonitoringTable()

                //Act & Assert
                assertEquals(3, heartRateMonitorDAO.getAll().size)
                heartRateMonitorDAO.deleteByUserId(6)
                assertEquals(3, heartRateMonitorDAO.getAll().size)
            }
        }

        @Test
        fun `deleting heartRateMonitorDetails when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three heartRateMonitorDetails
                val userDAO = populateUserTable()
                val heartRateMonitorDAO = populateHeartRateMonitoringTable()

                //Act & Assert
                assertEquals(3, heartRateMonitorDAO.getAll().size)
                heartRateMonitorDAO.deleteByUserId(1)
                assertEquals(2, heartRateMonitorDAO.getAll().size)
            }
        }
    }
}