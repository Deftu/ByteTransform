package xyz.unifycraft.bytetransform.transformations.types

import xyz.unifycraft.bytetransform.config.TransformationData

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Transformer(
    val target: Array<String>,
    val priority: Int = TransformationData.DEFAULT_PRIORITY,
    val optional: Boolean = TransformationData.DEFAULT_OPTIONAL
)
