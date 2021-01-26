package sample;

import java.io.Serializable;

public class ControllerHelp implements Serializable {
    private final String link_long;
    private final String ling_short;
    private final String help;
    private final String helpStyle;

    public ControllerHelp(String link_long, String ling_short, String help, String helpStyle) {
        this.link_long = link_long;
        this.ling_short = ling_short;
        this.help = help;
        this.helpStyle = helpStyle;
    }

    public String getLink_long() {
        return link_long;
    }

    public String getLing_short() {
        return ling_short;
    }

    public String getHelp() {
        return help;
    }

    public String getHelpStyle() {
        return helpStyle;
    }
}
