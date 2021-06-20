package com.kechinvv.ghsearch.checker.impl

import com.intellij.psi.util.PsiTreeUtil
import com.kechinvv.ghsearch.checker.Check
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.resolve.BindingContext

object CheckOverrideFun : Check<KtNamedFunction> {

    override fun require(psi: List<KtFile>, context: BindingContext): List<KtNamedFunction> {
        val result: MutableList<KtNamedFunction> = mutableListOf()
        psi.forEach { tree ->
            val func = PsiTreeUtil.collectElementsOfType(tree, KtNamedFunction::class.java)
            func.forEach { if (it.modifierList?.text == "override") result.add(it) }
        }
        return result
    }

}