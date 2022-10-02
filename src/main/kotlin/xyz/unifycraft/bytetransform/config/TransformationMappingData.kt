package xyz.unifycraft.bytetransform.config

data class TransformationMappingsData(
    val transformerName: String,
    val transformationNames: List<Pair<String, List<String>>> // First is transformation method's name, second is mapped name(s)
)
