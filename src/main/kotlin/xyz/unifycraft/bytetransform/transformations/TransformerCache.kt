package xyz.unifycraft.bytetransform.transformations

import xyz.unifycraft.bytetransform.transformations.types.Accessor
import xyz.unifycraft.bytetransform.transformations.types.AccessorTransformationHandle

internal object TransformerCache {
    private val classCache = mutableMapOf<String, MutableList<TransformationHandle>>()

    fun initialize(name: String, clz: Class<*>) {
        methodLoop@ for (method in clz.declaredMethods) {
            if (!method.trySetAccessible())
                continue

            annotationLoop@ for (annotation in method.annotations) {
                val handle = when (annotation) {
                    is Accessor -> {
                        if (!clz.isInterface)
                            throw IllegalStateException("Class containing accessors isn't an interface!")

                        AccessorTransformationHandle(clz)
                        break@methodLoop
                    }
                    else -> null
                } ?: continue
                val list = classCache.computeIfAbsent(name) { mutableListOf() }
                list.add(handle)
                classCache[name] = list
            }
        }
    }

    fun getHandles(name: String) =
        classCache[name]?.toList()
}
