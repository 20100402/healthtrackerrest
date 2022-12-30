package ie.setu.repository

import ie.setu.domain.OnlineConsultation
import ie.setu.domain.db.OnlineConsultations
import ie.setu.domain.repository.OnlineConsultationDAO
import ie.setu.helpers.onlineConsultationDetails
import ie.setu.helpers.populateOnlineConsultationsTable
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
private val onlineConsultation1 = onlineConsultationDetails.get(0)
private val onlineConsultation2 = onlineConsultationDetails.get(1)
private val onlineConsultation3 = onlineConsultationDetails.get(2)

class OnlineConsultationDaoTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateOnlineConsultationDetails {

        @Test
        fun `multiple onlineConsultationDetails added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three onlineConsultationDetails
                val userDAO = populateUserTable()
                val onlineConsultationDAO = populateOnlineConsultationsTable()
                //Act & Assert
                assertEquals(3, onlineConsultationDAO.getAll().size)
                assertEquals(onlineConsultation1, onlineConsultationDAO.findByOnlineConsultationId(onlineConsultation1.id))
                assertEquals(onlineConsultation2, onlineConsultationDAO.findByOnlineConsultationId(onlineConsultation2.id))
                assertEquals(onlineConsultation3, onlineConsultationDAO.findByOnlineConsultationId(onlineConsultation3.id))
            }
        }
    }

    @Nested
    inner class ReadOnlineConsultationDetails {

        @Test
        fun `getting all OnlineConsultationDetails from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three onlineConsultationDetails
                val userDAO = populateUserTable()
                val onlineConsultationDAO = populateOnlineConsultationsTable()
                //Act & Assert
                assertEquals(3, onlineConsultationDAO.getAll().size)
            }
        }

        @Test
        fun `get onlineConsultation by user id that has no onlineConsultationDetails, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three onlineConsultationDetails
                val userDAO = populateUserTable()
                val onlineConsultationDAO = populateOnlineConsultationsTable()
                //Act & Assert
                assertEquals(0, onlineConsultationDAO.findByUserId(6).size)
            }
        }

        @Test
        fun `get onlineConsultation by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three onlineConsultationDetails
                val userDAO = populateUserTable()
                val onlineConsultationDAO = populateOnlineConsultationsTable()
                //Act & Assert
                assertEquals(onlineConsultation1, onlineConsultationDAO.findByUserId(1).get(0))
                assertEquals(onlineConsultation2, onlineConsultationDAO.findByUserId(2).get(0))
                assertEquals(onlineConsultation3, onlineConsultationDAO.findByUserId(3).get(0))
            }
        }

        @Test
        fun `get all onlineConsultationDetails over empty table returns none`() {
            transaction {

                //Arrange - create and setup onlineConsultationDAO object
                SchemaUtils.create(OnlineConsultations)
                val onlineConsultationDAO = OnlineConsultationDAO()

                //Act & Assert
                assertEquals(0, onlineConsultationDAO.getAll().size)
            }
        }

        @Test
        fun `get onlineConsultation by onlineConsultation id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three onlineConsultationDetails
                val userDAO = populateUserTable()
                val onlineConsultationDAO = populateOnlineConsultationsTable()
                //Act & Assert
                assertEquals(null, onlineConsultationDAO.findByOnlineConsultationId(4))
            }
        }

        @Test
        fun `get onlineConsultation by onlineConsultation id that exists, results in a correct onlineConsultation returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three onlineConsultationDetails
                val userDAO = populateUserTable()
                val onlineConsultationDAO = populateOnlineConsultationsTable()
                //Act & Assert
                assertEquals(onlineConsultation1, onlineConsultationDAO.findByOnlineConsultationId(1))
                assertEquals(onlineConsultation3, onlineConsultationDAO.findByOnlineConsultationId(3))
            }
        }
    }

    @Nested
    inner class UpdateOnlineConsultationDetails {

        @Test
        fun `updating existing onlineConsultation in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three onlineConsultationDetails
                val userDAO = populateUserTable()
                val onlineConsultationDAO = populateOnlineConsultationsTable()

                //Act & Assert
                val onlineConsultation3updated = OnlineConsultation(id = 3, doctorName = "Dick", appointmentDate = DateTime.now(), remarks ="Average",
                    userId = 2)
                onlineConsultationDAO.updateByOnlineConsultationId(onlineConsultation3updated.id, onlineConsultation3updated)
                assertEquals(onlineConsultation3updated, onlineConsultationDAO.findByOnlineConsultationId(3))
            }
        }

        @Test
        fun `updating non-existant onlineConsultation in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three onlineConsultationDetails
                val userDAO = populateUserTable()
                val onlineConsultationDAO = populateOnlineConsultationsTable()

                //Act & Assert
                val onlineConsultation4updated = OnlineConsultation(id = 4, doctorName = "Dick", appointmentDate = DateTime.now(), remarks ="Average",
                    userId = 2)
                onlineConsultationDAO.updateByOnlineConsultationId(4, onlineConsultation4updated)
                assertEquals(null, onlineConsultationDAO.findByOnlineConsultationId(4))
                assertEquals(3, onlineConsultationDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteOnlineConsultationDetails {

        @Test
        fun `deleting a non-existant onlineConsultation (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three onlineConsultationDetails
                val userDAO = populateUserTable()
                val onlineConsultationDAO = populateOnlineConsultationsTable()

                //Act & Assert
                assertEquals(3, onlineConsultationDAO.getAll().size)
                onlineConsultationDAO.deleteByOnlineConsultationId(4)
                assertEquals(3, onlineConsultationDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing onlineConsultation (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three onlineConsultationDetails
                val userDAO = populateUserTable()
                val onlineConsultationDAO = populateOnlineConsultationsTable()

                //Act & Assert
                assertEquals(3, onlineConsultationDAO.getAll().size)
                onlineConsultationDAO.deleteByOnlineConsultationId(onlineConsultation3.id)
                assertEquals(2, onlineConsultationDAO.getAll().size)
            }
        }


        @Test
        fun `deleting onlineConsultationDetails when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three onlineConsultationDetails
                val userDAO = populateUserTable()
                val onlineConsultationDAO = populateOnlineConsultationsTable()

                //Act & Assert
                assertEquals(3, onlineConsultationDAO.getAll().size)
                onlineConsultationDAO.deleteByUserId(6)
                assertEquals(3, onlineConsultationDAO.getAll().size)
            }
        }

        @Test
        fun `deleting onlineConsultationDetails when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three onlineConsultationDetails
                val userDAO = populateUserTable()
                val onlineConsultationDAO = populateOnlineConsultationsTable()

                //Act & Assert
                assertEquals(3, onlineConsultationDAO.getAll().size)
                onlineConsultationDAO.deleteByUserId(1)
                assertEquals(2, onlineConsultationDAO.getAll().size)
            }
        }
    }
}