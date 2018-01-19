package parser;

import java.util.ArrayList;
import java.util.Collections;

public class CommandlineParser {
    private ArrayList<String> args = new ArrayList<>();
    private String[] argv;

    public CommandlineParser(String data) {
        args.clear();
        args.add("openmw");
        if (data.contains("--")) {
            Collections.addAll(args, data.split(" "));
        }
        argv = args.toArray(new String[args.size()]);
    }

    public int getArgc() {
        return args.size();
    }

    public String[] getArgv() {
        return argv;
    }
}
