package dev.alpas.console.stubs

internal class Stubs {
    companion object {
        fun planStub(): String {
            return """
                package StubPackageName 

                import dev.alpas.console.Command
                
                class StubClazzName : Command(name = "command:name", help = "Command description.") {
                    override fun run() {
                        TODO("Run the command")
                    }
                }
            """.trimIndent()
        }

        fun generatorStub(): String {
            return """
                package StubPackageName 

                import dev.alpas.console.GeneratorCommand
                import dev.alpas.console.OutputFile
                import dev.alpas.extensions.toPascalCase
                import java.io.File

                class StubClazzName(srcPackage: String) : 
                GeneratorCommand(srcPackage, name = "command:name", help = "Command description.") {
                    
                    override fun populateOutputFile(filename: String, actualname: String, vararg parentDirs: String): OutputFile {
                        TODO("Return an instance of OutputFile(). Something like:")
                        return OutputFile()
                            .target(File(sourceOutputPath("console", *parentDirs), "$#{filename.toPascalCase()}.kt"))
                            .packageName(makePackageName("console", *parentDirs))
                            .stub("Contents of this generated file.")
                    }

                    override fun onCompleted(outputFile: OutputFile) {
                        withColors {
                            echo(green("StubClazzName CREATED 🙌"))
                            echo("${'$'}{brightGreen(outputFile.target.name)}: ${'$'}{dim(outputFile.target.path)}")
                        }
                    }
                }
            """.trimIndent().replace("$#", "$")
        }

        fun serviceProviderStub(): String {
            return """
                package StubPackageName 

                import dev.alpas.Application
                import dev.alpas.ServiceProvider

                class StubClazzName() : ServiceProvider {
                    override fun register(app: Application) {
                        // register your bindings here like so: container.bind(MyApiService())
                        // Do not ask for any dependencies here as they might not have been registered yet.
                    }

                    override fun boot(app: Application) {
                        // do some initial setup here
                        // Feel free to ask for any dependencies here as they should be all registered by now.
                    }
                }
            """.trimIndent()
        }
    }
}
