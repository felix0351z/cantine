package de.felix0351.models.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime


/**
 *  Stores all the news of the app
 *
 * @property id Unique id of the news. Automatically generated
 * @property title Titel of the information
 * @property description Content of the information
 * @property picture Storage address to the picture of the news
 * @property time Creation time, will be automatically generated
 *
 */
object News : Table("news") {

    val id: Column<Int> = integer("id").autoIncrement()
    val title: Column<String> = varchar("title", 128)
    val description: Column<String> = varchar("description", 4096)
    val picture: Column<String> = varchar("picture", 128)
    val time: Column<LocalDateTime> = datetime("time").autoIncrement()

    override val primaryKey = PrimaryKey(id)



}