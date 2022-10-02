package xyz.unifycraft.bytetransform.transformations

import org.objectweb.asm.tree.ClassNode

interface TransformationHandle {
    fun handle(node: ClassNode)
}
