package main


import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.KtNodeTypes.BINARY_EXPRESSION
import org.jetbrains.kotlin.asJava.namedUnwrappedElement
import org.jetbrains.kotlin.asJava.unwrapped
import org.jetbrains.kotlin.fir.builder.toFirOperation
import org.jetbrains.kotlin.fir.builder.toUnaryName
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType
import org.jetbrains.kotlin.psi.psiUtil.parentSubstitute
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.*
import org.jetbrains.kotlin.utils.addToStdlib.cast
import java.beans.Expression


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
        val propResolve = properties.mapNotNull {
            try {
                if (it.parent.node.elementType == BINARY_EXPRESSION) it.parent
                else null
            } catch (e: AssertionError) {
                null
            }
        }
        println(propResolve)
        val propDiscr = propResolve.mapNotNull { println((it.node.psi as KtBinaryExpression).operationToken)}
        println(propDiscr)
        return false

    }
}