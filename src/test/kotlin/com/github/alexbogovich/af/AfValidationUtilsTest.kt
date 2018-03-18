package com.github.alexbogovich.af

import com.github.alexbogovich.af.AfFileTypeUtils.getUspnFileType
import com.github.alexbogovich.af.AfValidationUtils.getSource
import com.github.alexbogovich.af.AfValidationUtils.validateDocument
import com.github.alexbogovich.af.ResourceIntraction.exampleFolder
import com.github.alexbogovich.af.ResourceIntraction.schemaFolder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors


internal class AfValidationUtilsTest {


    @TestFactory
    fun `validate documents`() = getFiles().map { filePath ->
        val type = getUspnFileType(filePath)
        dynamicTest("validate as $type file ${filePath.fileName}") {
            Assertions.assertEquals(Collections.EMPTY_LIST, validateDocument(filePath.toFile(), type, Paths.get(getSchemaFolder().toURI()).toAbsolutePath().toString()))
        }
    }

    private fun getSchemaFolder() = this.javaClass.classLoader.getResource(schemaFolder)

    private fun getFiles(): List<Path> {
        val folder = this.javaClass.classLoader.getResource(exampleFolder)
        return Files.walk(Paths.get(folder.toURI()))
                .filter { it.toFile().isFile }
                .collect(Collectors.toList())
    }
}