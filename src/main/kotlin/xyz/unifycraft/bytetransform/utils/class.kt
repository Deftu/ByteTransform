package xyz.unifycraft.bytetransform.utils

val Class<*>.internalName: String
    get() = name.replace(".", "/")
