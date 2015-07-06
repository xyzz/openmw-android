package ui.files;

import constants.Constants;

/**
 * Created by sylar on 06.07.15.
 */
public class CommandlineParser {
    private int argc = 0;
    private String[] argv;
    private String data = "";

    public CommandlineParser(String data) {
        this.data = data;
        this.data.trim();
    }

    public void parseCommandLine() {
        if (data.contains(" ")) {
            argv = data.split(" ");
            argc = argv.length;
        } else {
            argv = new String[1];
            argv[0] = data;
            argc = 1;
        }
    }

    public int getArgc() {
        return argc;
    }

    public String[] getArgv() {
        return argv;
    }
}
