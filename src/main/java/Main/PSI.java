package Main;

import com.spbpu.mppconverter.MainKt;
import com.spbpu.mppconverter.kootstrap.PSICreator;
import com.spbpu.mppconverter.util.PSIUtilsKt;
import org.jetbrains.kotlin.psi.KtElement;
import org.jetbrains.kotlin.psi.KtFile;
import org.jetbrains.kotlin.resolve.BindingContext;

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
        System.out.println(head.getLastChild().getPrevSibling().getChildren()[0]
                .getChildren()[0].getChildren()[0].getChildren()[0].getChildren()[0].getFirstChild());
    }

}
