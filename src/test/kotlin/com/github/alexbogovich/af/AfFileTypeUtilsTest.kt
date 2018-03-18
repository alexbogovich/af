package com.github.alexbogovich.af

import com.github.alexbogovich.af.AfFileTypeUtils.getUspnFileType
import com.github.alexbogovich.af.ResourceIntraction.getFiles
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

internal class AfFileTypeUtilsTest {
    @TestFactory
    fun `Get correct type for USPN and VsVO NPF`() = getFiles().map { filePath ->
        val fileType = getUspnFileType(filePath)
        dynamicTest("$fileType is ${filePath.fileName}") {
            assertTrue(filePath.fileName.toString().contains(fileType))
        }
    }
}