package main

import com.intellij.psi.util.PsiTreeUtil
import com.spbpu.mppconverter.kootstrap.PSICreator
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext

class SampleEq() {



    fun equal(path: String): Boolean {
        val creator = PSICreator()
        val tree = creator.getPSIForFile(path) //"C:/Users/valer/IdeaProjects/GithubSearch/pattern.kt"
        val bindingContext = creator.getBinding()
        if (bindingContext != null) {
            eq(bindingContext, tree)
        }
        return true
    }

    private fun eq(ctx: BindingContext, psi: KtFile) {

        val checks = Checker(ctx, psi)
        checks.checkRecursion()

    }

}