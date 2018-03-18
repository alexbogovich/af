package com.github.alexbogovich.af

import com.github.alexbogovich.af.AfFileTypeUtils.getUspnFileType
import com.github.alexbogovich.af.ResourceInteraction.getFilesOverPositive
import com.github.alexbogovich.af.ResourceInteraction.getFilesUspnFromAf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

internal class AfFileTypeUtilsTest {
    @TestFactory
    fun `Get correct type for USPN and VsVO NPF`() = getFilesUspnFromAf().map { filePath ->
        val fileType = getUspnFileType(filePath)
        dynamicTest("$fileType is ${filePath.fileName}") {
            assertTrue(filePath.fileName.toString().contains("_${fileType}_"))
        }
    }

    @TestFactory
    fun `Get correct type for RNPF_A_COMPRESS case`() = getFilesOverPositive().map { filePath ->
        val fileType = getUspnFileType(filePath)
        dynamicTest("$fileType is ${filePath.fileName}") {
            assertTrue(filePath.fileName.toString().contains("_${fileType}_"))
        }
    }
}