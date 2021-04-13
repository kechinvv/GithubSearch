package Main;

import com.spbpu.mppconverter.MainKt;
import com.spbpu.mppconverter.util.PSIUtilsKt;
import org.jetbrains.kotlin.psi.KtFile;

public class PSI {
    String path = "";
    KtFile pattern = getPSI(path);

    public static KtFile getPSI(String pathKt) {
        KtFile p = MainKt.main(pathKt);
        PSIUtilsKt.debugPrint(p);
        return p;
    }

    public boolean equal(KtFile file){
        PSIUtilsKt.debugPrint(pattern);
        PSIUtilsKt.debugPrint(file);
        return true;
    }

}
