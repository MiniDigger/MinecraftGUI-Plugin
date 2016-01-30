/*
 *
 *  *     Minecraft GUI Server
 *  *     Copyright (C) 2015  Samuel Marchildon-Lavoie
 *  *
 *  *     This program is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
 *  *     (at your option) any later version.
 *  *
 *  *     This program is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU General Public License
 *  *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.github.minecraftgui.views;

import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.models.factories.GuiFactory;
import io.github.minecraftgui.models.listeners.OnGuiListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Samuel on 2016-01-17.
 */
public class AdminPlugin implements OnGuiListener {

    private final MinecraftGuiService service;
    private ArrayList<GuiFactory.GuiModel> guiModels;
    private final File xmlFolder;
    private boolean isDevMode = false;

    public AdminPlugin(MinecraftGuiService service, String path) {
        this.service = service;
        this.xmlFolder = initXmlFolder(path);
        this.guiModels = initGuiModels();
        service.addPlugin(this, "MinecraftGuiAdmin", initDependencies());
    }

    public void setDevMode(boolean isDevMode) {
        this.isDevMode = isDevMode;
    }

    @Override
    public void onGuiPreInit(UserGui userGui) {
        for(GuiFactory.GuiModel guiModel : guiModels)
            guiModel.preInitGui(service.getPluginInterface(), userGui);
    }

    @Override
    public void onGuiInit(UserGui userGui) {
        if(isDevMode)
            guiModels = initGuiModels();

        for(GuiFactory.GuiModel guiModel : guiModels)
            guiModel.initGui(service.getPluginInterface(), userGui, this);
    }

    private String[] initDependencies(){
        ArrayList<String> dependencies = new ArrayList<>();

        for(GuiFactory.GuiModel guiModel : guiModels)
            dependencies.addAll(guiModel.getDependencies());

        return dependencies.toArray(new String[dependencies.size()]);
    }

    private File initXmlFolder(String path){
        File file = new File(path+File.separator+"xml");
        file.mkdirs();

        return file;
    }

    private ArrayList<GuiFactory.GuiModel> initGuiModels(){
        ArrayList<GuiFactory.GuiModel> guiModels = new ArrayList<>();

        for(File file : xmlFolder.listFiles()){
            if(file.getName().endsWith(".xml")){
                try{
                    guiModels.add(service.createGuiModel(file));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return guiModels;
    }

}
