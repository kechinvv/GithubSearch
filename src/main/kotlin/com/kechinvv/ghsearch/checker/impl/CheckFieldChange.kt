package com.kechinvv.ghsearch.checker.impl

import com.intellij.psi.util.PsiTreeUtil
import com.kechinvv.ghsearch.checker.Check
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtQualifiedExpression
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getPropertyResolvedCallWithAssert

object CheckFieldChange : Check<PropertyDescriptor> {

    override fun require(psi: List<KtFile>, context: BindingContext): List<PropertyDescriptor> {
        val result: MutableList<PropertyDescriptor> = mutableListOf()
        psi.forEach {
            val functions = PsiTreeUtil.collectElementsOfType(it, KtNamedFunction::class.java)

            functions.forEach { x ->
                val a = x.fieldChange(context)
                if (a.isNotEmpty()) {
                    a.forEach { result.add(it) }
                }
            }
        }
        return result
    }

    fun KtNamedFunction.fieldChange(ctx: BindingContext): List<PropertyDescriptor> {
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
                if (a.node.elementType == KtNodeTypes.BINARY_EXPRESSION &&
                    (a.node.psi as KtBinaryExpression).operationToken == KtTokens.EQ &&
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