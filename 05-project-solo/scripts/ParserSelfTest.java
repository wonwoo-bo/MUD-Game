import com.mud.game.CommandParser;
import com.mud.game.ParsedCommand;

public class ParserSelfTest {
    public static void main(String[] args) {
        CommandParser parser = new CommandParser();
        ParsedCommand a = parser.parse("look");
        ParsedCommand b = parser.parse("go north");
        ParsedCommand c = parser.parse("help");
        System.out.println(a.getVerb());
        System.out.println(b.getVerb() + ":" + b.getArgs().length + ":" + b.getArgs()[0]);
        System.out.println(c.getVerb());
    }
}

