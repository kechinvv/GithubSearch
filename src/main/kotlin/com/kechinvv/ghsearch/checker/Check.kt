package com.kechinvv.ghsearch.checker

import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext

interface Check<T> {

    fun require(psi: List<KtFile>, context: BindingContext): List<T>

}