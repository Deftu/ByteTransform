package xyz.unifycraft.bytetransform.processor

import com.google.auto.service.AutoService
import xyz.unifycraft.bytetransform.annotaions.*
import xyz.unifycraft.bytetransform.transformations.types.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_16)
class ByteTransformProcessor : AbstractProcessor() {
    override fun getSupportedAnnotationTypes() = setOf(
        Transformer::class.java.name,
        Injection::class.java.name,
        Redirect::class.java.name,
        Accessor::class.java.name,
        Invoker::class.java.name
    )

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        return false
    }
}
