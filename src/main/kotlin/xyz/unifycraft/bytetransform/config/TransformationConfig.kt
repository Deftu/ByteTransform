package xyz.unifycraft.bytetransform.config

data class TransformationConfig(
    val packageName: String,
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



    fun hasTransformationFor(className: String) =
        globalTransformations.any {
            it.target.contains(className)
        } || clientTransformations.any {
            it.target.contains(className)
        } || serverTransformations.any {
            it.target.contains(className)
        }
}
