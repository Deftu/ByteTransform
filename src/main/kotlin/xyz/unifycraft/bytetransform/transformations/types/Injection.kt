package xyz.unifycraft.bytetransform.transformations.types

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Injection(
    val target: Array<String>
)
