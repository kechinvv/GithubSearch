package main.filter

import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext

interface Filter <T> {

    fun require(psi: KtFile, context: BindingContext): List<T>

}