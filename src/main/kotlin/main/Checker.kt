package main


import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.KtNodeTypes.BINARY_EXPRESSION
import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.lexer.KtTokens.EQ
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getPropertyResolvedCallWithAssert
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall


class Checker(bContext: BindingContext, tree: KtFile) {
    private var ctx: BindingContext = bContext
    private var psi: KtFile = tree

    fun getContext(): BindingContext {
        return ctx
    }

    fun getPSI(): KtFile {
        return psi
    }

    fun checkRecursion(): MutableList<CallableDescriptor> {
        val functions = PsiTreeUtil.collectElementsOfType(psi, KtNamedFunction::class.java)
        val result: MutableList<CallableDescriptor> = mutableListOf()
        functions.forEach { x ->
            val a = x.isRecursive()
            if (a.isNotEmpty()) {
                result += a
            }
        }
        println(result)
        return result
    }


    private fun KtNamedFunction.isRecursive(): List<CallableDescriptor> {
        val nestedCalls = PsiTreeUtil.collectElementsOfType(this, KtCallExpression::class.java)
        val resolvedCalls = nestedCalls.mapNotNull { it.getResolvedCall(ctx) }
        val functionDescriptors = resolvedCalls.map { it.resultingDescriptor }
        return functionDescriptors.filter { it.findPsi() == this }
    }

    fun checkFieldChange(): MutableList<PropertyDescriptor> {
        val functions = PsiTreeUtil.collectElementsOfType(psi, KtNamedFunction::class.java)
        val result: MutableList<PropertyDescriptor> = mutableListOf()
        functions.forEach { x ->
            val a = x.fieldChange()
            if (a.isNotEmpty()) {
                a.forEach { result.add(it) }
            }
        }
        println(result)
        return result
    }

    private fun KtNamedFunction.fieldChange(): List<PropertyDescriptor> {
        val properties = PsiTreeUtil.collectElementsOfType(this, KtQualifiedExpression::class.java)
        val propRes = properties.mapNotNull {
            try {
                it.getPropertyResolvedCallWithAssert(ctx)
                it
            } catch (e: AssertionError) {
                null
            }
        }
        val propFilter = propRes.mapNotNull {
            var a = it.parent
            var quit = false
            var c = a
            for (i in 1..3) {
                if (a is KtQualifiedExpression) break
                if (a.node.elementType == BINARY_EXPRESSION &&
                    (a.node.psi as KtBinaryExpression).operationToken == EQ &&
                    ((a.node.psi as KtBinaryExpression).left == it || (a.node.psi as KtBinaryExpression).left == c)
                ) {
                    quit = true
                    break
                }
                c = a
                a = a.parent
            }
            if (quit) it
            else null
        }
        return propFilter.map { it.getPropertyResolvedCallWithAssert(ctx).resultingDescriptor }
    }
}
