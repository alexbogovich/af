package com.github.alexbogovich.af

import com.github.alexbogovich.af.AfFileTypeUtils.getUspnFileType
import com.github.alexbogovich.af.AfValidationUtils.validateDocument
import com.github.alexbogovich.af.ResourceInteraction.getFilesOverNegative
import com.github.alexbogovich.af.ResourceInteraction.getFilesOverPositive
import com.github.alexbogovich.af.ResourceInteraction.getFilesUspnFromAf
import com.github.alexbogovich.af.ResourceInteraction.getSchemaFolder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import java.nio.file.Paths
import java.util.*


internal class AfValidationUtilsTest {
    @TestFactory
    fun `Validate documents`() = getFilesUspnFromAf().map { filePath ->
        val type = getUspnFileType(filePath)
        dynamicTest("validate as $type file ${filePath.fileName}") {
            Assertions.assertEquals(Collections.EMPTY_LIST,
                    validateDocument(filePath, type, Paths.get(getSchemaFolder().toURI())))
        }
    }

    @TestFactory
    fun `Validate documents RNPF_A_COMPRESS positive`() = getFilesOverPositive().map { filePath ->
        val type = getUspnFileType(filePath)
        dynamicTest("validate as $type file ${filePath.fileName}") {
            Assertions.assertEquals(Collections.EMPTY_LIST,
                    validateDocument(filePath, type, Paths.get(getSchemaFolder().toURI())))
        }
    }

    @Nested
    inner class ErrorCases {
        @TestFactory
        fun `Validate documents RNPF_A_COMPRESS negative`() = getFilesOverNegative().map { filePath ->
            val type = getUspnFileType(filePath)
            dynamicTest("validate as $type file ${filePath.fileName}") {
                val validateErrors = validateDocument(filePath, type, Paths.get(getSchemaFolder().toURI()))
                Assertions.assertEquals(2, validateErrors.size)
                Assertions.assertTrue(validateErrors.toList()[0]!!.message!!.contains("'УТ:Номер'"))
                Assertions.assertTrue(validateErrors.toList()[1]!!.message!!.contains("'GUID'"))
            }
        }
    }
}