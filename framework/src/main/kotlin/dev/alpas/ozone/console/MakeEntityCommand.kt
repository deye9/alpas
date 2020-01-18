package dev.alpas.ozone.console

import com.cesarferreira.pluralize.pluralize
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import dev.alpas.console.GeneratorCommand
import dev.alpas.console.OutputFile
import dev.alpas.extensions.toPascalCase
import dev.alpas.extensions.toSnakeCase
import dev.alpas.ozone.console.stubs.EntityStubs
import java.io.File

class MakeEntityCommand(srcPackage: String) :
    GeneratorCommand(
        srcPackage,
        name = "make:entity",
        help = "Create an entity class with a corresponding entity table."
    ) {

    private val tableName by option("--table", help = "Name of the table. e.g. --table=users")
    private val simple by option("--simple", help = "Create a simple data based entity.").flag()
    private val migration by option("--migration", "-m", help = "Create a migration for the entity.").flag()

    override fun populateOutputFile(filename: String, actualname: String, vararg parentDirs: String): OutputFile {
        val table = tableName ?: filename.pluralize()
        return OutputFile()
            .target(File(sourceOutputPath("entities", *parentDirs), "${filename.toPascalCase()}.kt"))
            .packageName(makePackageName("entities", *parentDirs))
            .stub(entityStub())
            .replacements(
                mapOf(
                    "StubTableClazzName" to table.toPascalCase(),
                    "StubTableName" to table.toSnakeCase().toLowerCase()
                )
            )
    }

    private fun entityStub(): String {
        return if (simple) EntityStubs.simpleStub() else EntityStubs.stub()
    }

    override fun onCompleted(outputFile: OutputFile) {
        withColors {
            echo(green("ENTITY CREATED 🙌"))
            echo("${brightGreen(outputFile.target.name)}: ${dim(outputFile.target.path)}")
        }
        if (migration) {
            val table = tableName ?: outputFile.target.nameWithoutExtension.pluralize()
            MakeMigrationCommand(srcPackage).main(arrayOf("create_${table.toSnakeCase()}_table", "--create=${table}"))
        }
    }
}
