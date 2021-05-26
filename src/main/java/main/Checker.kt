package main

import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.backend.common.descriptors.explicitParameters
import org.jetbrains.kotlin.backend.common.descriptors.isSuspend
import org.jetbrains.kotlin.builtins.isBuiltinFunctionalClassDescriptor
import org.jetbrains.kotlin.cfg.pseudocode.containingDeclarationForPseudocode
import org.jetbrains.kotlin.codegen.arity
import org.jetbrains.kotlin.codegen.coroutines.isSuspensionPoint
import org.jetbrains.kotlin.codegen.coroutines.replaceSuspensionFunctionWithRealDescriptor
import org.jetbrains.kotlin.descriptors.impl.referencedProperty
import org.jetbrains.kotlin.ir.expressions.typeParametersCount
import org.jetbrains.kotlin.js.resolve.diagnostics.JsCallChecker.Companion.isJsCall
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.*
import org.jetbrains.kotlin.psi.stubs.elements.KtDotQualifiedExpressionElementType
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getReferenceTargets
import org.jetbrains.kotlin.resolve.bindingContextUtil.isUsedAsExpression
import org.jetbrains.kotlin.resolve.calls.callResolverUtil.hasInferredReturnType
import org.jetbrains.kotlin.resolve.calls.callUtil.*
import org.jetbrains.kotlin.resolve.calls.inference.ConstraintSystem
import org.jetbrains.kotlin.resolve.calls.resolvedCallUtil.hasBothReceivers
import org.jetbrains.kotlin.resolve.calls.smartcasts.getKotlinTypeWithPossibleSmartCastToFP
import org.jetbrains.kotlin.resolve.calls.tower.isNewNotCompleted
import org.jetbrains.kotlin.resolve.checkers.ExperimentalUsageChecker.Companion.isExperimentalityAccepted
import org.jetbrains.kotlin.resolve.descriptorUtil.*
import org.jetbrains.kotlin.resolve.isGetterOfUnderlyingPropertyOfInlineClass
import org.jetbrains.kotlin.resolve.jvm.annotations.hasJvmFieldAnnotation
import org.jetbrains.kotlin.resolve.jvm.annotations.isCallableMemberCompiledToJvmDefault
import org.jetbrains.kotlin.resolve.multiplatform.couldHaveASource
import org.jetbrains.kotlin.synthetic.isResolvedWithSamConversions
import org.jetbrains.kotlin.util.containingNonLocalDeclaration


class Checker(bContext: BindingContext, tree: KtFile) {
    var ctx: BindingContext = bContext
    var psi: KtFile = tree


    fun checkRecursion(): Boolean {
        val functions = PsiTreeUtil.collectElementsOfType(psi, KtNamedFunction::class.java)
        functions.forEach { if (it.isRecursive(ctx)) return true  }
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

    fun checkFieldChange(): Boolean{
        val functions = PsiTreeUtil.collectElementsOfType(psi, KtNamedFunction::class.java)
        functions.forEach {if (it.fieldChange()) return true}
        return false
    }

    private fun KtNamedFunction.fieldChange(): Boolean {
        val properties = PsiTreeUtil.collectElementsOfType(this, KtQualifiedExpression::class.java)
        println(properties)
        val propResolve = properties.mapNotNull { it.getParentCall(ctx)}
        println(propResolve)
        val propDiscr = propResolve.mapNotNull { it}
        println(propDiscr)
       // val test = propResolve.mapNotNull { it. }
       // val propFinal = propResolve.map { it.getResolvedCall(ctx)}
      //  println(propFinal)
      //  return propFinal.any { it != null }
        return false

    }
}
