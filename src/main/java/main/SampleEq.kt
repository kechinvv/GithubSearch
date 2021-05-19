package main

import com.intellij.psi.util.PsiTreeUtil
import com.spbpu.mppconverter.kootstrap.PSICreator
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext

class SampleEq() {

    val s= PSICreator()
    var ps = s.getPSIForFile("C:/Users/valer/IdeaProjects/GithubSearch/pattern.kt")
    var bindingContext: BindingContext? = s.getBinding()
   // var pattern: KtFile = getPSI(path)


    fun equal(p: BindingContext): Boolean {
        eq(p)
        return true
    }

    private fun eq(p: BindingContext) {


        val list2 = PsiTreeUtil.collectElementsOfType(ps, KtExpression::class.java)
        //println(list2)
        val checks = Checks(bindingContext!!, list2, ps)
        checks.checkRecursion()


        //  println(head.node.getAllChildrenNodes())
        //   println(head.getAllChildren())
    }

}