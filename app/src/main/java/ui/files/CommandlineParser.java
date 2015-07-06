package ui.files;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by sylar on 06.07.15.
 */
public class CommandlineParser {
    private int argc = 0;
    private String[] argv;
    private String data = "";

    public CommandlineParser(String data) {
        this.data = data;
        this.data = this.data.replace(" ", "");
    }

    public void parseCommandLine() {
        if (data.contains("--")) {
            argv = createArgv(data.split("\\-\\-"));
            argc = argv.length;
        } else {
            argv = new String[1];
            argv[0] = "";
            argc = 1;
        }
    }

    private String[] createArgv(String[] argv) {
        argv = removeElements(argv, 0);
        for (int i = 0; i < argv.length; i++)
            argv[i] = "--" + argv[i];
        return argv;
    }

    private String[] removeElements(String[] input, int pos) {
        List result = new LinkedList();

        for (int i = 0; i < input.length; i++)
            if (i != pos)
                result.add(input[i]);

        return (String[]) result.toArray(input);
    }

    public int getArgc() {
        return argc;
    }

    public String[] getArgv() {
        return argv;
    }
}
