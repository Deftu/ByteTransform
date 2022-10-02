package xyz.unifycraft.bytetransform

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import xyz.unifycraft.bytetransform.config.TransformationConfig
import xyz.unifycraft.bytetransform.config.TransformationData
import xyz.unifycraft.bytetransform.transformations.TransformationHandler
import java.util.concurrent.CopyOnWriteArrayList

object ByteTransform {
    private val configs = CopyOnWriteArrayList<TransformationConfig>()
    private lateinit var environment: Environment
    var debug: Boolean
        get() = System.getProperty("bt.debug", "false").toBoolean()
        set(value) {
            System.setProperty("bt.debug", value.toString())
        }

    @JvmStatic
    fun setEnvironment(environment: Environment) {
        if (::environment.isInitialized)
            throw IllegalStateException("The environment has already been set!")
        this.environment = environment
    }

    @JvmStatic
    fun addConfig(config: TransformationConfig) {
        configs.add(config)
    }

    @JvmStatic
    fun addConfigRaw(input: String) =
        addConfig(TransformationConfig.parse(input))
    @JvmStatic
    fun addConfigFile(file: String, classLoader: ClassLoader) =
        addConfigRaw(classLoader.getResourceAsStream(file)?.readBytes()?.decodeToString() ?: throw IllegalArgumentException("File was not valid!"))
    @JvmStatic
    fun addConfigFile(file: String) =
        addConfigFile(file, javaClass.classLoader)

    @JvmStatic
    fun initialize() {
        for (config in configs) {
            for (transformation in config.globalTransformations) {
                transformation.initialize(config.packageName)
            }

            for (transformation in when (environment) {
                Environment.CLIENT -> config.clientTransformations
                Environment.SERVER -> config.serverTransformations
            }) {
                transformation.initialize(config.packageName)
            }
        }
    }

    @JvmStatic
    fun handle(className: String, classBytes: ByteArray): ByteArray {
        if (configs.none { config ->
            config.hasTransformationFor(className)
        }) return classBytes
        repeat(10) { println("This class ($className) has transformations") }

        val node = ClassNode()
        val reader = ClassReader(classBytes)
        reader.accept(node, ClassReader.EXPAND_FRAMES)

        for (config in configs) {
            val transformations = config.globalTransformations.toMutableList()
            transformations.addAll(when (environment) {
                Environment.CLIENT -> config.clientTransformations
                Environment.SERVER -> config.serverTransformations
            })
            transformations.sortBy(TransformationData::priority)
            transformations.reverse()

            for (transformation in transformations) {
                println("transforming $className using ${transformation.name}")
                TransformationHandler.handle("${config.packageName}.${transformation.name}", node)
            }
        }

        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
        node.accept(writer)
        return writer.toByteArray()
    }
}
