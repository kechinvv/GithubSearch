package main.filter.impl

import com.intellij.psi.util.PsiTreeUtil
import main.filter.Filter
import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall

object FilterRecursion : Filter<KtNamedFunction> {

    override fun require(psi: KtFile, context: BindingContext): List<KtNamedFunction> {
        val functions = PsiTreeUtil.collectElementsOfType(psi, KtNamedFunction::class.java)
        val result: MutableList<KtNamedFunction> = mutableListOf()
        functions.forEach { x ->
            val a = x.isRecursive(context)
            if (a.isNotEmpty()) {
                result += x
            }
        }
        println(result)
        return result
    }

    private fun KtNamedFunction.isRecursive(ctx: BindingContext): List<CallableDescriptor> {
        val nestedCalls = PsiTreeUtil.collectElementsOfType(this, KtCallExpression::class.java)
        val resolvedCalls = nestedCalls.mapNotNull { it.getResolvedCall(ctx) }
        val functionDescriptors = resolvedCalls.map { it.resultingDescriptor }
        return functionDescriptors.filter { it.findPsi() == this }
    }
}