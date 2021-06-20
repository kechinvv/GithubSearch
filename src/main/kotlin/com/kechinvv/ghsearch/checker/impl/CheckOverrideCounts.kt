package com.kechinvv.ghsearch.checker.impl

import com.intellij.psi.util.PsiTreeUtil
import com.kechinvv.ghsearch.checker.Check
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.resolve.BindingContext

object CheckOverrideCounts : Check<KtNamedFunction> {

    override fun require(psi: List<KtFile>, context: BindingContext): List<KtNamedFunction> {
        val result: MutableList<KtNamedFunction> = mutableListOf()
        val res: MutableList<Any> = mutableListOf()
        psi.forEach { tree ->
            val functions = PsiTreeUtil.collectElementsOfType(tree, KtClassOrObject::class.java)

            functions.forEach { println(it.getSuperTypeList()?.text) }
            res.addAll(functions)
        }

        return result
    }

}