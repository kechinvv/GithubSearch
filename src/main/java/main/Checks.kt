package main

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getValueArgumentsInParentheses
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameUnsafe

class Checks(bContext: BindingContext, elements: Collection<PsiElement>) {
    var bindingContext: BindingContext? = null
    var psi: Collection<PsiElement>? = null

    init {
        bindingContext = bContext
        psi = elements
    }

    fun checkRecursion() {
        for (i in psi!!) {
                val x = bindingContext?.get(BindingContext.FUNCTION, i)
                val y = bindingContext?.get(BindingContext.CALL, i as KtElement?)
                if (x != null) println(x)
                if (y!=null) println(y)
        }
    }
}