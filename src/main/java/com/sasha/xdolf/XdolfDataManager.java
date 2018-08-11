package com.sasha.xdolf;

import com.sasha.xdolf.friend.Friend;
import com.sasha.xdolf.gui.clickgui.elements.XdolfWindow;
import com.sasha.xdolf.gui.hud.RenderableObject;
import com.sasha.xdolf.gui.hud.ScreenCornerPos;
import com.sasha.xdolf.misc.YMLParser;
import com.sasha.xdolf.module.XdolfModule;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.sasha.xdolf.module.ModuleManager.moduleRegistry;

/**
 * Created by Sasha on 08/08/2018 at 12:47 PM
 **/
// i love making simple tasks overly complex :3:3:3:3
// maybe im just a complex kind of girl
public class XdolfDataManager {
    private final Lock threadLock = new ReentrantLock();
    private final String dataFileName = "XdolfData.yml";

    public synchronized int[] getSavedGuiPos(XdolfWindow window){
        XdolfMod.logMsg(true, "Loading \"" + window.getTitle()+"\"'s saved GUI position...");
        threadLock.lock();
        XdolfMod.logWarn(true, "Thread locking engaged!");
        try {
            File f = new File(dataFileName);
            if (!f.exists()){
                XdolfMod.logErr(true, "Data file doesn't exist (maybe this is the client's first run?)");
                return new int[]{0,0};
            }
            YMLParser parser = new YMLParser(f);
            if (!parser.exists("xdolf.gui.clickgui."+window.getTitle()+".x") && (!parser.exists("xdolf.gui.clickgui."+window.getTitle()+".y"))){
                parser.set("xdolf.gui.clickgui."+window.getTitle()+".x", window.dragX);
                parser.set("xdolf.gui.clickgui."+window.getTitle()+".y", window.dragY);
                parser.save();
                return new int[]{0,0};
            }
            int x = parser.getInt("xdolf.gui.clickgui."+window.getTitle()+".x",0);
            int y = parser.getInt("xdolf.gui.clickgui."+window.getTitle()+".y",0);
            return new int[]{x,y};
        } finally {
            threadLock.unlock();
            XdolfMod.logWarn(true, "Thread locking disengaged!");
        }
    }
    public synchronized void saveGuiPos(XdolfWindow window) throws IOException{
        XdolfMod.logMsg(true, "Loading \"" + window.getTitle()+"\"'s saved GUI position...");
        threadLock.lock();
        XdolfMod.logWarn(true, "Thread locking engaged!");
        try {
            File f = new File(dataFileName);
            if (!f.exists()){
                XdolfMod.logErr(true, "Data file doesn't exist (maybe this is the client's first run?)");
                f.createNewFile();
            }
            YMLParser parser = new YMLParser(f);
            parser.set("xdolf.gui.clickgui."+window.getTitle()+".x",window.dragX);
            parser.set("xdolf.gui.clickgui."+window.getTitle()+".y",window.dragY);
        } finally {
            threadLock.unlock();
            XdolfMod.logWarn(true, "Thread locking disengaged!");
        }
    }

    public synchronized ScreenCornerPos getHudPositionState(RenderableObject robj) throws IOException {
        XdolfMod.logMsg(true, "Loading \"" + robj.getName() +"\"'s saved HUD position...");
        threadLock.lock();
        XdolfMod.logWarn(true, "Thread locking engaged!");
        try {
            File f = new File(dataFileName);
            if (!f.exists()){
                XdolfMod.logErr(true, "Data file doesn't exist (maybe this is the client's first run?)");
                f.createNewFile();
                XdolfMod.logMsg(true, "Creating new data file with default HUD poaitions.");
                YMLParser parser = new YMLParser(f);
                parser.set("xdolf.gui.hud." + robj.getName().toLowerCase(), RenderableObject.getPosStr(robj.getDefaultPos()));
                parser.save();
                return robj.getDefaultPos();
            }
            YMLParser parser = new YMLParser(f);
            if (!parser.exists("xdolf.gui.hud." + robj.getName())){
                parser.set("xdolf.gui.hud." + robj.getName().toLowerCase(), RenderableObject.getPosStr(robj.getDefaultPos()));
                parser.save();
                return robj.getDefaultPos();
            }
            return RenderableObject.getPosEnum(parser.getString("xdolf.gui.hud." + robj.getName().toLowerCase()));
        } finally {
            threadLock.unlock();
            XdolfMod.logWarn(true, "Thread locking disengaged!");
        }
    }

    public synchronized int getSavedKeybind(XdolfModule module){
        XdolfMod.logMsg(true, "Getting module keybinde...");
        threadLock.lock(); // Don't allow other threads to modify this file until this operation is done.
        XdolfMod.logWarn(true, "Thread locking engaged!");
        try {
            File file = new File(dataFileName);
            if (!file.exists()) {
                XdolfMod.logErr(true, "Data file doesn't exist (maybe this is the client's first run?)");
                return 0;
            }
            YMLParser parser = new YMLParser(file);
            if (!parser.exists("xdolf.modules."+module.getModuleName()+".bind")){
                parser.set("xdolf.modules."+module.getModuleName()+".bind", 0);
                parser.save();
                return 0;
            }
            return parser.getInt("xdolf.modules."+module.getModuleName()+".bind", 0);
        } finally {
            threadLock.unlock();
            XdolfMod.logWarn(true, "Thread locking disengaged!");
        }
    }
    public synchronized void saveModuleBind(XdolfModule module) throws IOException {
        XdolfMod.logMsg(true, "Saving module keybinde...");
        threadLock.lock(); // Don't allow other threads to modify this file until this operation is done.
        XdolfMod.logWarn(true, "Thread locking engaged!");
        try {
            File file = new File(dataFileName);
            if (!file.exists()) {
                XdolfMod.logErr(true, "Data file doesn't exist (maybe this is the client's first run?)");
                file.createNewFile();
            }
            YMLParser parser = new YMLParser(file);
            parser.set("xdolf.modules."+module.getModuleName()+".bind", module.getKeyBind());
            parser.save();
        } finally {
            threadLock.unlock();
            XdolfMod.logWarn(true, "Thread locking disengaged!");
        }
    }

    public synchronized void saveModuleStates(boolean fileExists) throws IOException {
        XdolfMod.logMsg(true, "Updating module savestates...");
        threadLock.lock(); // Don't allow other threads to modify this file until this operation is done.
        XdolfMod.logWarn(true, "Thread locking engaged!");
        try {
            File file = new File(dataFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            YMLParser parser = new YMLParser(file);
            moduleRegistry.forEach(mod -> parser.set("xdolf.modules." + mod.getModuleName()+".state", fileExists && mod.isEnabled()));
            parser.save();
        } finally {
            threadLock.unlock();
            XdolfMod.logWarn(true, "Thread locking disengaged!");
        }
    }
    public synchronized boolean getSavedModuleState(String modName) throws IOException {
        XdolfMod.logMsg(true, "Getting module \"" + modName + "\"'s previously saved state...");
        threadLock.lock();
        XdolfMod.logWarn(true, "Thread locking engaged!");
        try {
            File file = new File(dataFileName);
            if (!file.exists()) {
                XdolfMod.logErr(true, "Module states file doesn't exist (maybe this is the client's first run?)");
                saveModuleStates(false);
                return false;
            }
            YMLParser parser = new YMLParser(file);
            return parser.getBoolean("xdolf.modules." + modName + ".state", false);
        } finally {
            threadLock.unlock();
            XdolfMod.logWarn(true, "Thread locking disengaged!");
        }
    }
    public synchronized void saveFriends(ArrayList<Friend> updatedFriends) throws IOException {
        try {
            XdolfMod.logMsg(true, "Updating saved friends...");
            threadLock.lock(); // Don't allow other threads to modify this file until this operation is done.
            XdolfMod.logWarn(true, "Thread locking engaged!");
            File file = new File(dataFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            YMLParser parser = new YMLParser(file);
            ArrayList<String> strFriends = new ArrayList<>();
            updatedFriends.forEach(f -> strFriends.add(f.getFriendName()));
            parser.set("xdolf.friends.friendlist", strFriends);//todo test
            parser.save();
        } finally {
            threadLock.unlock();
        }
    }
    public synchronized ArrayList<Friend> loadFriends() throws IOException {
        XdolfMod.logMsg(true, "Loading saved friends...");
        threadLock.lock();
        XdolfMod.logWarn(true, "Thread locking engaged!");
        try {
            File file = new File(dataFileName);
            if (!file.exists()) {
                XdolfMod.logErr(true, "Data file doesn't exist (maybe this is the client's first run?)");
                return new ArrayList<>();
            }
            YMLParser parser = new YMLParser(file);
            List<String> list = parser.getStringList("xdolf.friends.friendlist");
            ArrayList<Friend> fliet = new ArrayList<>();
            list.forEach(fs -> fliet.add(new Friend(fs)));
            return fliet; // it was supposed to say flist but i made a typo but idc, now its a filet :yum:
        } finally {
            threadLock.unlock();
            XdolfMod.logWarn(true, "Thread locking disengaged!");
        }
    }

}
