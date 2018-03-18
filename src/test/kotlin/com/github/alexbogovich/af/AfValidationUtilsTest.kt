package com.github.alexbogovich.af

import com.github.alexbogovich.af.AfFileTypeUtils.getUspnFileType
import com.github.alexbogovich.af.AfValidationUtils.validateDocument
import com.github.alexbogovich.af.ResourceIntraction.getFiles
import com.github.alexbogovich.af.ResourceIntraction.getSchemaFolder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import java.nio.file.Paths
import java.util.*


internal class AfValidationUtilsTest {
    @TestFactory
    fun `Validate documents`() = getFiles().map { filePath ->
        val type = getUspnFileType(filePath)
        dynamicTest("validate as $type file ${filePath.fileName}") {
            Assertions.assertEquals(Collections.EMPTY_LIST, validateDocument(filePath.toFile(), type, Paths.get(getSchemaFolder().toURI()).toAbsolutePath().toString()))
        }
    }
}