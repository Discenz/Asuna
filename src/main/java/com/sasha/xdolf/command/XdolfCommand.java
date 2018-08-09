package com.sasha.xdolf.command;

import com.sasha.xdolf.XdolfMod;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sasha on 08/08/2018 at 7:08 AM
 **/
public abstract class XdolfCommand {
//todo
    private String commandName;
    private String[] commandArgs;

    public final static String commandDelimetre = "-";

    public XdolfCommand(String commandName){
        this.commandName = commandName;
    }

    /**
     * Will treat text surrounded by quotes as one argument.
     * Keep package private so that devs can't accidentally do something dumb
     */
    /* package-private */ void setArguments(String theMessage){
        if (!theMessage.contains(" ")){
            this.commandArgs = null;
        }
        String updatedMessage = theMessage.replace(commandDelimetre + this.commandName + " ", ""); // i dont want the actual '-command' to be in the array
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(updatedMessage);
        while (m.find()) {
            list.add(m.group(1).replace("\"", ""));
        }
        this.commandArgs = list.toArray(this.commandArgs);
    }

    public String[] getArguments() {
        return commandArgs;
    }

    public String getCommandName(boolean withDelimetre) {
        return (withDelimetre ? commandDelimetre : "") + commandName;
    }

    public void onCommand() {
        XdolfMod.logMsg(true, "Player executing client command \"" + commandName + "\"");
    }
}
