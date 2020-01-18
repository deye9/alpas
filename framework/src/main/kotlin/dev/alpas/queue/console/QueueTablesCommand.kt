package dev.alpas.queue.console

import dev.alpas.console.GeneratorCommand
import dev.alpas.console.OutputFile
import dev.alpas.ozone.console.MIGRATION_FILE_DATE_FORMAT
import dev.alpas.queue.console.stubs.Stubs
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class QueueTablesCommand(srcPackage: String) :
    GeneratorCommand(
        srcPackage,
        name = "queue:tables",
        help = "Create all the required migrations for a database queue."
    ) {

    override val names = listOf("create_queue_jobs_tables")

    override fun populateOutputFile(filename: String, actualname: String, vararg parentDirs: String): OutputFile {
        val packageName = makePackageName("database", "migrations", *parentDirs)
        val outputPath = sourceOutputPath("database", "migrations", *parentDirs)
        val datePrefix = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern(MIGRATION_FILE_DATE_FORMAT))

        return OutputFile()
            .target(File(outputPath, "${datePrefix}_$filename.kt"))
            .packageName(packageName)
            .className(filename)
            .stub(Stubs.queueTablesStub())
    }

    override fun onCompleted(outputFile: OutputFile) {
        withColors {
            echo(green("MIGRATIONS CREATED 🙌"))
            echo("${brightGreen(outputFile.target.name)}: ${dim(outputFile.target.path)}")
        }
    }
}
