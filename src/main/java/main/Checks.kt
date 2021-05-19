package main

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getDataFlowInfoAfter
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getValueArgumentsInParentheses
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameUnsafe
import org.jetbrains.kotlin.resolve.jvm.annotations.findJvmFieldAnnotation

class Checks(bContext: BindingContext, elements: Collection<PsiElement>, Ptree: KtFile) {
    var bindingContext: BindingContext? = bContext
    var psi: Collection<PsiElement>? = null
    var tree: KtFile? = null

    init {
        bindingContext = bContext
        psi = elements
        tree = Ptree
    }

    fun checkRecursion() {
       println(bindingContext)
        for (i in psi!!) {
                val x = bindingContext?.get(BindingContext.FUNCTION, i)
                val y = bindingContext?.get(BindingContext.CALL, i as KtElement?)
                if (x != null) println(x)
                if (y!=null) println(y)
        }
    }
}