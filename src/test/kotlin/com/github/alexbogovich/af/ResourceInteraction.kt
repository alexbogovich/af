package com.github.alexbogovich.af

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

internal object ResourceInteraction {

    private const val schemaFolder = "АФ 2.21.3д/Схемы"
    private const val exampleFolder = "АФ 2.21.3д/Примеры"
    private const val customPositive = "RNPF_A_COMPRESS/positive"
    private const val customNegative= "RNPF_A_COMPRESS/negative"

    fun getSchemaFolder() = this.javaClass.classLoader.getResource(schemaFolder)!!

    fun getFilesUspnFromAf() = getFiles(exampleFolder)

    fun getFilesOverPositive() = getFiles(customPositive)

    fun getFilesOverNegative() = getFiles(customNegative)

    fun getFiles(resourcePath: String): List<Path> {
        val folder = this.javaClass.classLoader.getResource(resourcePath)
        return Files.walk(Paths.get(folder.toURI()))
                .filter { Files.isRegularFile(it) }
                .collect(Collectors.toList())
    }
}