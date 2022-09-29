package xyz.unifycraft.bytetransform.annotaions

import xyz.unifycraft.bytetransform.config.TransformationData

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Invoker(
    val priority: Int = TransformationData.DEFAULT_PRIORITY,
    val optional: Boolean = TransformationData.DEFAULT_OPTIONAL
)
