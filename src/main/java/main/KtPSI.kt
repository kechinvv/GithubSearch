package main

import com.intellij.psi.util.PsiTreeUtil
import com.spbpu.mppconverter.kootstrap.PSICreator
import com.spbpu.mppconverter.main
import com.spbpu.mppconverter.util.debugPrint
import com.spbpu.mppconverter.util.getAllChildren
import com.spbpu.mppconverter.util.getAllChildrenNodes
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext

class KtPSI {
    var bindingContext: BindingContext? = null
    private var path = "C:/Users/valer/IdeaProjects/GithubSearch/pattern.kt"
    var pattern: KtFile = getPSI(path)


    fun equal(file: KtFile?): Boolean {
        pattern.debugPrint()
        file?.debugPrint()
        eq()
        return true
    }

    fun eq() {
        bindingContext = PSICreator.analyze(pattern)
        val head = pattern.psiOrParent
        val list2 = PsiTreeUtil.collectElementsOfType(head, KtExpression::class.java)
        //println(list2)
        val checks: Checks = Checks(bindingContext!!, list2)
        checks.checkRecursion()


        println(head.node.getAllChildrenNodes())
        println(head.getAllChildren())
    }

    companion object {
        fun getPSI(pathKt: String): KtFile {
            val p: KtFile = main(pathKt)
            p.debugPrint()
            return p
        }
    }
}