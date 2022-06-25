import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9].*");
        String text = "azzz中文字符asd草";
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            System.out.println(matcher.group());
        }
    }

}
