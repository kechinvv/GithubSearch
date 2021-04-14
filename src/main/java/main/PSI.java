package main;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import com.spbpu.mppconverter.MainKt;
import com.spbpu.mppconverter.kootstrap.PSICreator;
import com.spbpu.mppconverter.util.PSIUtilsKt;
import org.jetbrains.kotlin.psi.KtElement;
import org.jetbrains.kotlin.psi.KtFile;
import org.jetbrains.kotlin.resolve.BindingContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PSI {
    BindingContext bindingContext;
    String path = "C:/Users/valer/IdeaProjects/GithubSearch/pattern.kt";
    KtFile pattern = getPSI(path);

    public static KtFile getPSI(String pathKt) {
        KtFile p = MainKt.main(pathKt);
        //  PSIUtilsKt.debugPrint(p);
        return p;
    }

    public boolean equal(KtFile file) {
        PSIUtilsKt.debugPrint(pattern);
        //  PSIUtilsKt.debugPrint(file);
        eq();
        return true;
    }

    public void eq() {
        bindingContext = PSICreator.Companion.analyze(pattern);

        KtElement head = pattern.getPsiOrParent();

        //   System.out.println(head.getLastChild().getPrevSibling().getChildren()[0]
        //         .getChildren()[0].getChildren()[0].getChildren()[0].getChildren()[0].getFirstChild())
      //  System.out.println(Arrays.toString(PsiTreeUtil.collectElements(head, null)));
        System.out.println(Arrays.toString(PsiTreeUtil.collectElements(head, new PsiElementFilter() {
            @Override
            public boolean isAccepted(PsiElement element) {
                return element != null;
            }
        })));
        List<PsiElement> list = PSIUtilsKt.getAllChildren(head);
        List<PsiReference> ref = new ArrayList<PsiReference>();
        for (PsiElement i: list){
            if (i.getReference() != null) ref.add(i.getReference());
        }
        System.out.println(ref);
        System.out.println(PSIUtilsKt.getAllChildrenNodes(head.getNode()));
        System.out.println(PSIUtilsKt.getAllChildren(head));
    }

}
