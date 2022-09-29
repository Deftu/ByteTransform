package xyz.unifycraft.bytetransform.config

data class TransformationConfig(
    val required: Boolean,
    val globalTransformations: List<TransformationData>,
    val clientTransformations: List<TransformationData>,
    val serverTransformations: List<TransformationData>
) {
    companion object {
        @JvmStatic
        fun parse(input: String) =
            TransformationConfigParser.parse(input)
    }
}
