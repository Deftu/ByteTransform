package xyz.unifycraft.bytetransform.transformations.types

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Invoker(
    val target: String
)
