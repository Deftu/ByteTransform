package xyz.unifycraft.bytetransform.config

import org.objectweb.asm.tree.ClassNode

data class TransformationData(
    val name: String,
    var priority: Int,
    var optional: Boolean
) {
    companion object {
        const val DEFAULT_PRIORITY = 1000
        const val DEFAULT_OPTIONAL = true
    }

    fun handle(node: ClassNode) {
        
    }
}
