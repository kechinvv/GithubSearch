package com.kechinvv.ghsearch.checker.impl

import com.intellij.psi.util.PsiTreeUtil
import com.kechinvv.ghsearch.checker.Check
import org.jetbrains.kotlin.asJava.elements.KtLightMethod
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext

object CheckOverrideCounts: Check<KtNamedFunction> {

    override fun require(psi: List<KtFile>, context: BindingContext): List<KtNamedFunction> {
        val result: MutableList<KtNamedFunction> = mutableListOf()
        psi.forEach { tree ->

        //    println(a)
            val functions = PsiTreeUtil.collectElementsOfType(tree, KtNamedFunction::class.java)
            val func = PsiTreeUtil.collectElementsOfType(tree, KtLightMethod::class.java)
           // println(func)
           // val resolveFunction = functions.mapNotNull { it}
            //val resFunction = functions.mapNotNull { it.psiOrParent as PsiMethod}
        //   println(functions.map { it. })
           // println(resolveFunction)
          //  println(resFunction)
        }
        return result
    }

}