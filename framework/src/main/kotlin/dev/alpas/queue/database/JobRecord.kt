package dev.alpas.queue.database

import dev.alpas.ozone.MigratingTable
import dev.alpas.ozone.bigIncrements
import dev.alpas.ozone.longText
import dev.alpas.ozone.tinyInt
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.timestamp
import me.liuwj.ktorm.schema.varchar
import java.time.Instant

interface JobRecord : Entity<JobRecord> {
    companion object : Entity.Factory<JobRecord>()

    var id: Long
    var queue: String
    var payload: String
    var tries: Int
    var reservedAt: Long?
    var availableAt: Long
    var createdAt: Long
}

interface FailedJobRecord : Entity<FailedJobRecord> {
    var id: Long
    var connection: String
    var queue: String
    var payload: String
    var exception: String
    val failedAt: Instant
}

object JobRecords : MigratingTable<JobRecord>("jobs") {
    val id by bigIncrements("id").bindTo { it.id }
    val queue by varchar("queue").index().bindTo { it.queue }
    val payload by longText("payload").bindTo { it.payload }
    val tries by tinyInt("tries").unsigned().bindTo { it.tries }
    val reservedAt by long("reserved_at").nullable().unsigned().bindTo { it.reservedAt }
    val availableAt by long("available_at").unsigned().bindTo { it.availableAt }
    val createdAt by long("created_at").unsigned().bindTo { it.createdAt }
}

object FailedJobRecords : MigratingTable<FailedJobRecord>("failed_jobs") {
    val id by bigIncrements("id").bindTo { it.id }
    val connection by varchar("connection").index().bindTo { it.connection }
    val queue by varchar("queue").index().bindTo { it.queue }
    val payload by longText("payload").bindTo { it.payload }
    val exception by longText("exception").bindTo { it.exception }
    val failedAt by timestamp("failed_at").useCurrent().bindTo { it.failedAt }
}
