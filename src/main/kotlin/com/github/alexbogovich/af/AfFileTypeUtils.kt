package com.github.alexbogovich.af

import com.github.alexbogovich.af.AfValidationUtils.getSource
import java.nio.file.Path
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.transform.stream.StreamSource

object AfFileTypeUtils {
    @JvmOverloads fun getUspnFileType(fileSource: StreamSource, typeTagPos: Int = 2): String {
        val factory = XMLInputFactory.newInstance()
        val reader = factory.createXMLStreamReader(fileSource)

        var type = "unknown"
        var tagCount = 0
        try {
            while (reader.hasNext() && tagCount < typeTagPos) {
                val event = reader.next()
                when (event) {
                    XMLStreamConstants.START_ELEMENT -> {
                        if (++tagCount == typeTagPos) {
                            type = reader.localName
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw e
        } finally {
            reader.close()
        }
        return type
    }

    @JvmOverloads fun getUspnFileType(filePath: Path, typeTagPos: Int = 2): String {
        return getUspnFileType(getSource(filePath), typeTagPos)
    }
}