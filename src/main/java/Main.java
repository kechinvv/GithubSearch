import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("data.txt"), StandardCharsets.UTF_8);
        String user = lines.get(0);
        String password = lines.get(1);
        Parser parser = new Parser();
        parser.auth(user, password);
    }
}
