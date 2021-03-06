package com.kechinvv.ghsearch.checker.impl

import com.intellij.psi.util.PsiTreeUtil
import com.kechinvv.ghsearch.checker.Check
import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall

object CheckRecursion : Check<KtNamedFunction> {

    override fun require(psi: List<KtFile>, context: BindingContext): List<KtNamedFunction> {
        val result: MutableList<KtNamedFunction> = mutableListOf()
        psi.forEach {
            val functions = PsiTreeUtil.collectElementsOfType(it, KtNamedFunction::class.java)
            functions.forEach { x ->
                val a = x.isRecursive(context)
                if (a.isNotEmpty()) {
                    result += x
                }
            }
        }
        return result
    }

    private fun KtNamedFunction.isRecursive(ctx: BindingContext): List<CallableDescriptor> {
        val nestedCalls = PsiTreeUtil.collectElementsOfType(this, KtCallExpression::class.java)
        val resolvedCalls = nestedCalls.mapNotNull { it.getResolvedCall(ctx) }
        val functionDescriptors = resolvedCalls.map { it.resultingDescriptor }
        return functionDescriptors.filter { it.findPsi() == this }
    }
}