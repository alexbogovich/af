package com.github.alexbogovich.af

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

internal object ResourceIntraction {

    val schemaFolder = "АФ 2.21.3д/Схемы"
    val exampleFolder = "АФ 2.21.3д/Примеры"

    fun getSchemaFolder() = this.javaClass.classLoader.getResource(schemaFolder)

    fun getFiles(): List<Path> {
        val folder = this.javaClass.classLoader.getResource(exampleFolder)
        return Files.walk(Paths.get(folder.toURI()))
                .filter { it.toFile().isFile }
                .collect(Collectors.toList())
    }
}