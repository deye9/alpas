package dev.alpas.queue

import dev.alpas.Application
import dev.alpas.ServiceProvider
import dev.alpas.config
import dev.alpas.console.Command
import dev.alpas.queue.console.MakeJobCommand
import dev.alpas.queue.console.QueueTablesCommand
import dev.alpas.queue.console.QueueWorkCommand
import dev.alpas.queue.job.JobSerializer
import dev.alpas.queue.job.JobSerializerImpl
import dev.alpas.tryMake

@Suppress("unused")
open class QueueServiceProvider : ServiceProvider {
    override fun register(app: Application) {
        app.bind(JobSerializer::class, JobSerializerImpl())
        val queueConfig = app.config { QueueConfig(app.env) }
        app.bind(QueueDispatcher(app, queueConfig))
    }

    override fun stop(app: Application) {
        app.logger.debug { "Stopping $this" }
        app.tryMake<Queue>()?.close()
    }

    override fun commands(app: Application): List<Command> {
        return listOf(QueueWorkCommand(app), MakeJobCommand(app.srcPackage), QueueTablesCommand(app.srcPackage))
    }
}
