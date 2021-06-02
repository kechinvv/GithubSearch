package main



import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.KtNodeTypes.BINARY_EXPRESSION
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.lexer.KtTokens.EQ
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
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
        println(properties)
        val propTypeFilter = properties.mapNotNull {
            try {
                if (it.parent.node.elementType == BINARY_EXPRESSION) it.parent
                else null
            } catch (e: AssertionError) {
                null
            }
        }
        //PLUSEQ      EQ      MINUSEQ      MULTEQ     DIVEQ
        println(propTypeFilter)
        val propEqFilter = propTypeFilter.mapNotNull {
            println(((it.node.psi as KtBinaryExpression).operationToken) == EQ) }
        println(propEqFilter)
        return false

    }
}
