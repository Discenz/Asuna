package com.sasha.xdolf.module;

import com.sasha.eventsys.SimpleEventHandler;
import com.sasha.eventsys.SimpleListener;
import com.sasha.xdolf.XdolfMod;
import com.sasha.xdolf.events.ModuleStatus;
import com.sasha.xdolf.events.XdolfModuleToggleEvent;

import static com.sasha.xdolf.module.ModuleUtils.moduleRegistry;

/**
 * Created by Sasha on 08/08/2018 at 11:52 AM
 **/
public class ModuleManager implements SimpleListener {

    @SimpleEventHandler
    public void onModToggle(XdolfModuleToggleEvent e){
        if (e.getToggleState() == ModuleStatus.ENABLE){
            e.getToggledModule().onEnable();
            if (!e.getToggledModule().isRenderable()) {
                XdolfModule.displayList.add(e.getToggledModule());
            }
            return;
        }
        e.getToggledModule().onDisable();
        if (!e.getToggledModule().isRenderable()){
            XdolfModule.displayList.remove(e.getToggledModule());
        }
    }

    public static XdolfModule getModuleByName(String key) {
        for (XdolfModule m : moduleRegistry) {
            if (m.getModuleName().equalsIgnoreCase(key)) return m;
        }

    }

    public static void tickModules(){
        moduleRegistry.forEach(XdolfModule::onTick);
    }

}
