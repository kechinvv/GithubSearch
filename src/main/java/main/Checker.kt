package main


import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.KtNodeTypes.BINARY_EXPRESSION
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.lexer.KtSingleValueToken
import org.jetbrains.kotlin.lexer.KtTokens.*
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getPropertyResolvedCallWithAssert
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall


class Checker(bContext: BindingContext, tree: KtFile) {
    var ctx: BindingContext = bContext
    var psi: KtFile = tree


    fun checkRecursion(): Boolean {
        val functions = PsiTreeUtil.collectElementsOfType(psi, KtNamedFunction::class.java)
        functions.forEach { if (it.isRecursive(ctx)) return true }
        return false
    }


    private fun KtNamedFunction.isRecursive(ctx: BindingContext): Boolean {
        val nestedCalls = PsiTreeUtil.collectElementsOfType(this, KtCallExpression::class.java)
        //    println(nestedCalls)
        val resolvedCalls = nestedCalls.mapNotNull { it.getResolvedCall(ctx) }
        //    println(resolvedCalls)
        val functionDescriptors = resolvedCalls.map { it.resultingDescriptor }
        //     println(functionDescriptors)
        val functionsPsi = functionDescriptors.mapNotNull { it.findPsi() }
        //     println(functionsPsi)
        return functionsPsi.any { it == this }
        // return false
    }

    fun checkFieldChange(): Boolean {
        val functions = PsiTreeUtil.collectElementsOfType(psi, KtNamedFunction::class.java)
        functions.forEach { if (it.fieldChange()) return true }
        return false
    }

    private fun KtNamedFunction.fieldChange(): Boolean {
        val properties = PsiTreeUtil.collectElementsOfType(this, KtQualifiedExpression::class.java)
        val propRes = properties.mapNotNull {
            try {
                it.getPropertyResolvedCallWithAssert(ctx)
                it
            } catch (e: AssertionError) {
                null
            }
        }
        val propTypeFilter = propRes.mapNotNull {
            if (it.parent.node.elementType == BINARY_EXPRESSION) it
            else null
        }
        val propEqFilter = propTypeFilter.filter {
            (it.parent.node.psi as KtBinaryExpression).operationToken == EQ
        }
        val propLeft = propEqFilter.filter { (it.parent.node.psi as KtBinaryExpression).left == it }
        println(propLeft.map{ it.parent.node.chars })
        return (propLeft.isNotEmpty())
    }
}
