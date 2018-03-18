package com.github.alexbogovich.af

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import java.nio.file.Path


internal class AfValidationUtilsPathExtTest {
    private val dirPath: Path = mock(Path::class.java)
    private val filePath: Path = mock(Path::class.java)

    @BeforeEach
    fun `parent path return file path`() {
        given(dirPath.fileName).willReturn(filePath)
    }

    @TestFactory
    fun `it's xsd and contain regex`() = listOf("schema_2018_04_04.xsd", "schema_2017-01-01.xsd")
            .map { path ->
                given(filePath.toString()).willReturn(path)
                DynamicTest.dynamicTest("check $path for xsd and '.*(\\d{4}-\\d{2}-\\d{2}).*'") {
                    assert(dirPath.isXsdAndNameContain(".*(\\d{4}-\\d{2}-\\d{2}).*".toRegex()))
                }
            }

    @TestFactory
    fun `it's xsd and contain substring`() = listOf("schemasub_2018_04_04.xsd",
            "schema_sub_2017-01-01.xsd",
            "sub.xsd")
            .map { path ->
                given(filePath.toString()).willReturn(path)
                DynamicTest.dynamicTest("check $path for xsd and contain 'sub'") {
                    assert(dirPath.isXsdAndNameContain("sub"))
                }
            }


    @TestFactory
    fun `it's xsd and start with string`() = listOf("schema.xsd", "schema_2017-01-01.xsd")
            .map { path ->
                given(filePath.toString()).willReturn(path)
                DynamicTest.dynamicTest("$path is xsd and start with 'schema'") {
                    assert(dirPath.isXsdAndNameStartWith("schema"))
                }
            }

    @Nested
    inner class ErrorCases {
        @TestFactory
        fun `it's not xsd or not start with string`() = listOf(
                "schema.xs",
                "schema_2017-01-01sd",
                "schem.xs",
                "abc_schema_2017-01-01sd")
                .map { path ->
                    given(filePath.toString()).willReturn(path)
                    DynamicTest.dynamicTest("check $path for xsd and start with 'schema'") {
                        assertFalse(dirPath.isXsdAndNameStartWith("schema"))
                    }
                }

        @TestFactory
        fun `it's not xsd or not contain regex`() = listOf(
                "schema.xs",
                "schema_2017-01-01sd",
                "schem.xs",
                "abc_schema_2017-01-01sd",
                "schema_20189_04_04.xsd",
                "schema_2017-01-1.xsd")
                .map { path ->
                    given(filePath.toString()).willReturn(path)
                    DynamicTest.dynamicTest("check $path for xsd and '.*(\\d{4}-\\d{2}-\\d{2}).*'") {
                        assertFalse(dirPath.isXsdAndNameContain(".*(\\d{4}-\\d{2}-\\d{2}).*".toRegex()))
                    }
                }

        @TestFactory
        fun `it's not xsd or not contain sub`() = listOf(
                "schema_sub.xs",
                "schema_sub_2017-01-01sd",
                "schem.xsd",
                "sub_schema_2017-01-01sd",
                "schema_2017-01-1.xsd")
                .map { path ->
                    given(filePath.toString()).willReturn(path)
                    DynamicTest.dynamicTest("check $path for xsd and 'sub'") {
                        assertFalse(dirPath.isXsdAndNameContain("sub"))
                    }
                }

    }
}