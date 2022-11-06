package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

class DbConfig{

    private val logger = KotlinLogging.logger {}

    //NOTE: you need the ?sslmode=require otherwise you get an error complaining about the ssl certificate
    fun getDbConnection() :Database{

        logger.info{"Starting DB Connection..."}

        val dbConfig = Database.connect(
                "jdbc:postgresql://ec2-3-219-135-162.compute-1.amazonaws.com:5432/d7djd47d11uia4?sslmode=require",
                driver = "org.postgresql.Driver",
                user = "dofjlzeqlqimyk",
                password = "96e5d9d2beafbd241961c7a9ec09d822b7ce19ecfd6edf7503a5922884b869fc")
        logger.info{"DbConfig name = " + dbConfig.name}
        logger.info{"DbConfig url = " + dbConfig.url}

        return dbConfig
    }

}