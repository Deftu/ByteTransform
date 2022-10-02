package xyz.unifycraft.bytetransform.config

import org.objectweb.asm.tree.ClassNode
import xyz.unifycraft.bytetransform.transformations.TransformerCache
import xyz.unifycraft.bytetransform.transformations.types.Transformer

data class TransformationData(
    val name: String,
    var target: Array<String>,
    var priority: Int,
    var optional: Boolean
) {
    companion object {
        const val DEFAULT_PRIORITY = 1000
        const val DEFAULT_OPTIONAL = true
    }

    private var initialized = false

    fun initialize(packageName: String) {
        if (initialized)
            return
        val fullName = "${packageName}.$name"

        val clz = Class.forName(fullName)
        val annotation = clz.annotations.firstOrNull { annotation ->
            annotation is Transformer
        } as? Transformer ?: throw IllegalArgumentException("$name does not have a transformer annotation!")
        this.target = annotation.target
        this.priority = annotation.priority
        this.optional = annotation.optional
        TransformerCache.initialize(fullName, clz)

        initialized = true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TransformationData

        if (name != other.name) return false
        if (!target.contentEquals(other.target)) return false
        if (priority != other.priority) return false
        if (optional != other.optional) return false
        if (initialized != other.initialized) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + target.contentHashCode()
        result = 31 * result + priority
        result = 31 * result + optional.hashCode()
        result = 31 * result + initialized.hashCode()
        return result
    }
}
