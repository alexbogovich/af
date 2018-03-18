package com.github.alexbogovich.af

import org.apache.xerces.dom.DOMInputImpl
import org.w3c.dom.ls.LSResourceResolver
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.zip.GZIPInputStream
import java.util.zip.ZipInputStream
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator


object AfValidationUtils {
    fun getSchemaFactory(): SchemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).run {
        resourceResolver = LSResourceResolver { type, namespaceURI, publicId, systemId, baseURI ->
            val xsdFile = Paths.get(URI(baseURI)).resolveSibling(systemId)
            val inputStream = Files.newInputStream(xsdFile)
            val childSystemId = xsdFile.toUri().toString()
            DOMInputImpl(publicId, childSystemId, systemId, inputStream, "UTF-8")
        }
        this
    }

    fun getAllSchemas(schemaFolder: Path): Map<String, List<Path>> {
        val containDateRegex = ".*(\\d{4}-\\d{2}-\\d{2}).*".toRegex()
        return Files.walk(schemaFolder)
                .filter { Files.isRegularFile(it) }
                .filter { it.isXsdAndNameContain(containDateRegex) }
                .collect(Collectors.groupingBy { path: Path ->
                    path.fileName.toString().split("_").first()
                })
    }

    fun getAllSchemas(schemaFolder: String) = getAllSchemas(Paths.get(schemaFolder))

    fun getAllSchemas(schemaFolder: URL) = getAllSchemas(Paths.get(schemaFolder.toURI()))

    fun getSchema(schemaFolder: Path, type: String) = Files.walk(schemaFolder)
            .filter { Files.isRegularFile(it) }
            .filter { it.isXsdAndNameStartWith(type + '_') }
            .findFirst()

    fun getSchema(schemaFolder: String, type: String) = getSchema(Paths.get(schemaFolder), type)

    fun getSchema(schemaFolder: URL, type: String) = getSchema(Paths.get(schemaFolder.toURI()), type)

    fun getValidator(afFileType: String, pathToSchemaFolder: Path, errorTargetCollection:
    MutableCollection<SAXParseException?>): Validator {
        val file = getSchema(pathToSchemaFolder, afFileType)
                .orElseThrow { RuntimeException("Schema for $afFileType not found") }
                .toFile()
        /**
         * exception in LSResourceResolver if use nio for schema file
         */
        val validator = getSchemaFactory().newSchema(file).newValidator()
        validator.errorHandler = object : ErrorHandler {
            override fun warning(exception: SAXParseException?) {
                errorTargetCollection.add(exception)
            }

            override fun error(exception: SAXParseException?) {
                errorTargetCollection.add(exception)
            }

            override fun fatalError(exception: SAXParseException?) {
                errorTargetCollection.add(exception)
            }
        }
        return validator
    }

    fun validateDocument(documentFile: String,
                         afFileType: String,
                         pathToSchemaFolder: String,
                         errorTargetCollection: MutableCollection<SAXParseException?> = getNewErrorList()
    ): Collection<SAXParseException?> {
        return validateDocument(Paths.get(documentFile), afFileType, Paths.get(pathToSchemaFolder), errorTargetCollection)
    }

    fun validateDocument(documentFile: Path,
                         afFileType: String,
                         pathToSchemaFolder: Path,
                         errorTargetCollection: MutableCollection<SAXParseException?> = getNewErrorList()
    ): Collection<SAXParseException?> {
        getValidator(afFileType, pathToSchemaFolder, errorTargetCollection).validate(getSource(documentFile))
        return errorTargetCollection
    }

    fun getSource(file: Path) : StreamSource {
        return when (file.fileName.toString().toLowerCase().split('.').last()) {
            "gz" -> StreamSource(GZIPInputStream(Files.newInputStream(file)))
            "zip" -> StreamSource(ZipInputStream(Files.newInputStream(file)))
            else -> StreamSource(Files.newInputStream(file))
        }
    }

    fun getNewErrorList() = mutableListOf<SAXParseException?>()
}

fun Path.isXsdAndNameContain(pattern: Regex) = fileName.toString().run {
    endsWith(".xsd") && contains(pattern)
}

fun Path.isXsdAndNameContain(type: String) = fileName.toString().run {
    endsWith(".xsd") && contains(type)
}

fun Path.isXsdAndNameStartWith(type: String) = fileName.toString().run {
    endsWith(".xsd") && startsWith(type)
}