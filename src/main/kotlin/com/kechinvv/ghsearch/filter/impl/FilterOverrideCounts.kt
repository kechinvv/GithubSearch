package com.kechinvv.ghsearch.filter.impl

import com.intellij.psi.util.PsiTreeUtil
import com.kechinvv.ghsearch.filter.Filter
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getFunctionResolvedCallWithAssert
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.util.isAnnotated

object FilterOverrideCounts: Filter<KtNamedFunction> {

    override fun require(psi: List<KtFile>, context: BindingContext): List<KtNamedFunction> {
        val result: MutableList<KtNamedFunction> = mutableListOf()
        psi.forEach { tree ->
            val functions = PsiTreeUtil.collectElementsOfType(tree, KtNamedFunction::class.java)
            val resolveFunction = functions.mapNotNull { it.isTopLevel}
            println(functions.map { it.name })
            println(resolveFunction)
        }
        return result
    }

}