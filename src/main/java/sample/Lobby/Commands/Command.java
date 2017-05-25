package sample.Lobby.Commands;

/**
 * Created by ksg on 24.04.17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Command {
    private String cmd;
    private String arguments;

    public String getCmd() {
        return cmd;
    }

    @SuppressWarnings("unused")
    public String getArguments() {
        return arguments;
    }

    @SuppressWarnings("unused")
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    @SuppressWarnings("unused")
    public void setArguments(String arguments) {
        this.arguments = arguments;
    }
}
