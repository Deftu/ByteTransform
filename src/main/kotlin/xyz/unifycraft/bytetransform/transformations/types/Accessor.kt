package xyz.unifycraft.bytetransform.transformations.types

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Accessor(
    val target: String,
    val type: AccessorType
) {
    enum class AccessorType {
        GETTER,
        SETTER
    }
}
