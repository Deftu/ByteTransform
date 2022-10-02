package xyz.unifycraft.bytetransform.transformations.types

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Redirect(
    val target: Array<String>
)
