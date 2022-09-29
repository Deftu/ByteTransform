package xyz.unifycraft.bytetransform.config

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import org.slf4j.LoggerFactory

internal object TransformationConfigParser {
    private val logger = LoggerFactory.getLogger("Transformation Config Parser")

    fun parse(input: String): TransformationConfig {
        return JsonReader(input.reader()).use { reader ->
            var required = false
            val globalTransformations = mutableListOf<TransformationData>()
            val clientTransformations = mutableListOf<TransformationData>()
            val serverTransformations = mutableListOf<TransformationData>()

            var currentName = ""
            while (reader.hasNext()) {
                val token = reader.peek()
                if (token == JsonToken.NAME) {
                    currentName = reader.nextName()
                    continue
                }

                when (currentName) {
                    "required" -> {
                        if (token != JsonToken.BOOLEAN)
                            throw IllegalArgumentException("Transformation config's 'required' property should be a boolean!")
                        required = reader.nextBoolean()
                    }
                    "transformations" -> globalTransformations.addAll(TransformationDataParser.parse(reader, token))
                    "client" -> clientTransformations.addAll(TransformationDataParser.parse(reader, token))
                    "server" -> serverTransformations.addAll(TransformationDataParser.parse(reader, token))
                    else -> {
                        logger.warn("Unknown property found? Skipping! ($currentName)")
                        reader.skipValue()
                        continue
                    }
                }
            }

            TransformationConfig(required, globalTransformations, clientTransformations, serverTransformations)
        }
    }
}

private object TransformationDataParser {
    private val logger = LoggerFactory.getLogger("Transformation Parser")

    fun parse(reader: JsonReader, token: JsonToken): List<TransformationData> {
        if (token != JsonToken.BEGIN_ARRAY)
            throw IllegalArgumentException("Transformations should be an array!")

        reader.beginArray()

        val values = mutableListOf<TransformationData>()
        while (reader.hasNext()) {
            val token = reader.peek()
            when (token) {
                JsonToken.STRING -> values.add(parseString(reader))
                JsonToken.BEGIN_OBJECT -> values.add(parseObject(reader))
                else -> {
                    logger.warn("Invalid property type for a transformation? Skipping! ($token)")
                    reader.skipValue()
                    continue
                }
            }
        }

        reader.endArray()
        return values
    }

    private fun parseString(reader: JsonReader): TransformationData {
        val string = reader.nextString()
        return TransformationData(string, TransformationData.DEFAULT_PRIORITY, TransformationData.DEFAULT_OPTIONAL)
    }

    private fun parseObject(reader: JsonReader): TransformationData {
        reader.beginObject()

        var name = ""
        var optional = TransformationData.DEFAULT_OPTIONAL

        var currentName = ""
        while (reader.hasNext()) {
            val token = reader.peek()
            if (token == JsonToken.NAME) {
                currentName = reader.nextName()
                continue
            }

            when (currentName) {
                "name" -> {
                    if (token != JsonToken.STRING)
                        throw IllegalArgumentException("Transformation's 'name' property should be a string!")
                    name = reader.nextString()
                }
                "optional" -> {
                    if (token != JsonToken.BOOLEAN)
                        throw IllegalArgumentException("Transformation's 'optional' property should be a boolean!")
                    optional = reader.nextBoolean()
                }
                else -> {
                    logger.warn("Unknown property found? Skipping! ($currentName)")
                    reader.skipValue()
                    continue
                }
            }
        }

        reader.endObject()
        return TransformationData(name, TransformationData.DEFAULT_PRIORITY, optional)
    }
}
