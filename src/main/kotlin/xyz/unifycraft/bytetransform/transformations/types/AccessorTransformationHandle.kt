package xyz.unifycraft.bytetransform.transformations.types

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode
import xyz.unifycraft.bytetransform.transformations.TransformationHandle
import xyz.unifycraft.bytetransform.utils.internalName

class AccessorTransformationHandle(
    private val clz: Class<*>
) : TransformationHandle {
    override fun handle(node: ClassNode) {
        node.interfaces.add(clz.internalName)
        for (method in clz.methods) {
            if (node.methods.any {
                it.name.equals(method.name)
            }) continue

            method.annotations.firstOrNull {
                it is Accessor
            }?.let { annotation ->
                val accessor = annotation as Accessor
                node.methods.add(MethodNode(Opcodes.ACC_PUBLIC, method.name, when (accessor.type) {
                    Accessor.AccessorType.GETTER -> "()L"
                    Accessor.AccessorType.SETTER -> ""
                }, null, emptyArray()))
            }
        }
    }
}
