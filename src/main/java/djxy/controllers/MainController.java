/*
 *     Minecraft GUI Server
 *     Copyright (C) 2015  Samuel Marchildon-Lavoie
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package djxy.controllers;

import djxy.api.MinecraftGuiService;
import djxy.controllers.NetworkController.PlayerConnection;
import djxy.models.ComponentManager;
import djxy.models.Form;
import djxy.models.PluginInterface;
import djxy.models.component.Attributes;
import djxy.models.component.Component;
import djxy.models.resource.FontResource;
import djxy.models.resource.ImageResource;
import djxy.models.resource.Resource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class MainController {

    public static final String PATH = "mods/MinecraftGUI";
    private static MainController instance = null;

    //**************************************************************************
    //**************************************************************************

    private final CopyOnWriteArrayList<ComponentManager> componentManagers;
    private final ArrayList<Resource> resourcesToDownload;
    private final HashMap<String, Object> componentsCreated;
    private final HashMap<String, ArrayList<ComponentManager>> componentManagersListeningButton;
    private final NetworkController networkController;
    private final ComponentLocationController componentLocationController;
    private final AuthenticationManager authenticationManager;
    private final PluginInterface pluginInterface;
    private final MinecraftGuiService minecraftGuiService;
    private boolean playerNeedAuthentication = false;

    public MainController(PluginInterface pluginInterface) throws Exception {
        if(instance != null)
            throw new Exception();

        instance = this;
        minecraftGuiService = new Service();
        resourcesToDownload = new ArrayList<>();
        this.pluginInterface = pluginInterface;
        componentsCreated = new HashMap<>();
        componentManagers = new CopyOnWriteArrayList<>();
        componentManagersListeningButton = new HashMap<>();
        this.authenticationManager = new AuthenticationManager(this);
        networkController = new NetworkController(this, 20000);
        componentLocationController = new ComponentLocationController();
    }

    public MinecraftGuiService getMinecraftGuiService() {
        return minecraftGuiService;
    }

    public void reloadPlayerScreen(String playerUUID){
        callInitPlayerGUIEvent(networkController.getPlayerConnection(playerUUID));
    }

    public void sendResource(String playerUUID, Resource resource){
        if(resource instanceof ImageResource)
            networkController.sendCommandTo(playerUUID, createCommandDownloadImage(resource.getUrl(), ((ImageResource) resource).getName()));
        else if(resource instanceof FontResource)
            networkController.sendCommandTo(playerUUID, createCommandDownloadFont(resource.getUrl()));
    }

    public void sendTimerRemover(String playerUUID, String componentIdToRemove, int second){
        networkController.sendCommandTo(playerUUID, createCommandTimerRemover(componentIdToRemove, second));
    }

    public void sendCallButtonEvent(String playerUUID, String buttonId){
        networkController.sendCommandTo(playerUUID, createCommandCallButtonEvent(buttonId));
    }

    public void sendComponentRemove(String playerUUID, String componentId){
        networkController.sendCommandTo(playerUUID, createCommandRemoveComponent(componentId));
    }

    public void sendComponentUpdate(String playerUUID, Attributes attributes){
        componentLocationController.setComponentLocation(playerUUID, attributes);
        networkController.sendCommandTo(playerUUID, attributes.getCommands());
    }

    public void sendComponentCreate(String playerUUID, Component component){
        componentsCreated.put(component.getId().toLowerCase(), Boolean.TRUE);
        componentLocationController.setComponentLocation(playerUUID, component.getAttributes());
        networkController.sendCommandTo(playerUUID, component.getCommands());

        for(Component child : component.getChildren())
            sendComponentCreate(playerUUID, child);
    }

    public void addComponentManager(ComponentManager manager, boolean playerNeedAuthentication){
        this.playerNeedAuthentication = playerNeedAuthentication == false?playerNeedAuthentication:true;

        componentManagers.add(manager);
    }

    public void addComponentManagerListenButton(ComponentManager manager, String buttonId){
        if(componentsCreated.get(buttonId.toLowerCase()) == null) {
            ArrayList<ComponentManager> componentManagers = componentManagersListeningButton.get(buttonId);

            if (componentManagers == null) {
                componentManagersListeningButton.put(buttonId, new ArrayList<ComponentManager>());
                componentManagers = componentManagersListeningButton.get(buttonId);
            }

            componentManagers.add(manager);
        }
    }

    public PluginInterface getPluginInterface() {
        return pluginInterface;
    }

    public boolean isPlayerAuthenticated(String playerUUID){
        if(!playerNeedAuthentication)
            return true;
        else
            return authenticationManager.isPlayerAuthenticated(playerUUID);
    }

    public boolean changePlayerConnectionState(String playerUUID){
        return networkController.changePlayerConnectionState(playerUUID);
    }

    public void resetPlayerComponentLocation(String playerUUID){
        componentLocationController.resetPlayerComponentLocationRelative(playerUUID);
        callInitPlayerGUIEvent(networkController.getPlayerConnection(playerUUID));
    }

    public void closePlayerConnection(String playerUUID) {
        networkController.closePlayer(playerUUID);
    }

    protected void newPlayerConnection(PlayerConnection playerConnection){
        authenticationManager.addPlayerToAuthenticate(playerConnection.getPlayerUUID());

        if(playerNeedAuthentication)
            authenticationManager.initPlayerGUI(playerConnection.getPlayerUUID());
        else{
            for(Resource resource : resourcesToDownload)
                sendResource(playerConnection.getPlayerUUID(), resource);
            callInitPlayerGUIEvent(playerConnection);
        }
    }

    protected void receiveCommand(PlayerConnection playerConnection, JSONObject object){
        String command[] = ((String) object.get("Command")).split(" ");
        String playerUUID = playerConnection.getPlayerUUID();
        
        if(command.length == 2){
            if(command[0].equals("FORM")){
                if(command[1].equals("INPUT")){
                    if(isPlayerAuthenticated(playerUUID))
                        callReceiveInputFormEvent(playerConnection, object);
                    else {
                        authenticationManager.receiveForm(playerUUID, new Form(object));

                        if(isPlayerAuthenticated(playerUUID))
                            callInitPlayerGUIEvent(playerConnection);
                    }
                }
            }
            else if(command[0].equals("SET")){
                if(command[1].equals("LOCATION_RELATIVE")){
                    setComponentLocationRelative(playerConnection.getPlayerUUID(), object);
                }
            }
        }
    }
    
    private void setComponentLocationRelative(String playerUUID, JSONObject object){
        String componentId = (String) object.get("ComponentId");
        int x = (int) (long) object.get("XRelative");
        int y = (int) (long) object.get("YRelative");
        
        componentLocationController.setComponentLocationRelative(playerUUID, componentId, x, y);
    }

    protected void callInitPlayerGUIEvent(PlayerConnection playerConnection){
        networkController.sendCommandTo(playerConnection.getPlayerUUID(), createCommandClearScreen());

        for(ComponentManager manager : componentManagers)
            manager.initPlayerGUI(playerConnection.getPlayerUUID());

        try {
            sendResource(playerConnection.getPlayerUUID(), ResourceFactory.load(new File("mods/testRes.xml")).get(0));
            sendComponentCreate(playerConnection.getPlayerUUID(), ComponentFactory.load(new File("mods/test.xml"), CSSFactory.load(new File("mods/test.css"))));
        }catch (Exception e){
            e.printStackTrace();
        }

        //pluginInterface.screenLoaded(playerConnection.getPlayerUUID());
    }
    
    private void callReceiveInputFormEvent(PlayerConnection playerConnection, JSONObject object){
        String buttonId = (String) object.get("ButtonId");
        ArrayList<ComponentManager> managers = componentManagersListeningButton.get(buttonId);

        if(managers != null){
            Form form = new Form(object);
            
            for(ComponentManager manager : managers)
                manager.receiveForm(playerConnection.getPlayerUUID(), form);
        }
    }

    protected void clearPlayerScreen(String playerUUID){
        networkController.sendCommandTo(playerUUID, createCommandClearScreen());
    }

    public void serverInit(){
        new File(PATH).mkdirs();
        componentLocationController.load();
    }

    public void serverIsStarting(){
        networkController.start();
    }

    public void serverIsStopping(){
        networkController.closeServer();
        componentLocationController.save();
    }

    public void playerJoin(String playerUUID){
        networkController.addPlayerConnected(playerUUID);
    }

    public void playerQuit(String playerUUID) {
        networkController.closePlayer(playerUUID);
    }

    private JSONArray createCommandCallButtonEvent(String buttonId){
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("Command", "CALL BUTTON_EVENT");
        object.put("ButtonId", buttonId);

        array.add(object);

        return array;
    }

    private JSONArray createCommandClearScreen(){
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("Command", "CLEAR SCREEN");
        
        array.add(object);
        
        return array;
    }

    private JSONArray createCommandDownloadImage(String url, String name){
        JSONArray array = new JSONArray();

        JSONObject object = new JSONObject();

        object.put("Command", "DOWNLOAD IMAGE");
        object.put("Url", url);
        object.put("File", name);

        array.add(object);

        return array;
    }

    private JSONArray createCommandDownloadFont(String url){
        JSONArray array = new JSONArray();

        JSONObject object = new JSONObject();

        object.put("Command", "DOWNLOAD FONT");
        object.put("Url", url);

        array.add(object);

        return array;
    }
    
    private JSONArray createCommandRemoveComponent(String componentId){
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("Command", "REMOVE COMPONENT");
        object.put("ComponentId", componentId);
        
        array.add(object);
        
        return array;
    }

    private JSONArray createCommandTimerRemover(String componentIdToRemove, int second){
        JSONArray array = new JSONArray();

        JSONObject object = new JSONObject();

        object.put("Command", "CREATE TIMER_REMOVER");
        object.put("ComponentId", componentIdToRemove);
        object.put("Second", second);

        array.add(object);

        return array;
    }

    private class Service implements MinecraftGuiService {

        @Override
        public void registerComponentManager(ComponentManager manager, boolean playerNeedAuthentication, ArrayList<Resource> resourceToDownload) {
            addComponentManager(manager, playerNeedAuthentication);
            resourcesToDownload.addAll(resourceToDownload);
        }

        @Override
        public void registerComponentManager(ComponentManager manager, boolean playerNeedAuthentication) {
            addComponentManager(manager, playerNeedAuthentication);
        }

        @Override
        public void listenButton(ComponentManager manager, String buttonId) {
            addComponentManagerListenButton(manager, buttonId);
        }

        @Override
        public void createTimerRemover(String playerUUID, String componentIdToRemove, int second) {
            Boolean auth = isPlayerAuthenticated(playerUUID);

            if(auth != null && auth == true)
                sendTimerRemover(playerUUID, componentIdToRemove, second);
        }

        @Override
        public void callButtonEvent(String playerUUID, String buttonId) {
            Boolean auth = isPlayerAuthenticated(playerUUID);

            if(auth != null && auth == true)
                sendCallButtonEvent(playerUUID, buttonId);
        }

        @Override
        public void createComponent(String playerUUID, Component component) {
            Boolean auth = isPlayerAuthenticated(playerUUID);

            if(auth != null && auth == true)
                sendComponentCreate(playerUUID, component);
        }

        @Override
        public void updateComponent(String playerUUID, Attributes attributes) {
            Boolean auth = isPlayerAuthenticated(playerUUID);

            if(auth != null && auth == true)
                sendComponentUpdate(playerUUID, attributes);
        }

        @Override
        public void removeComponent(String playerUUID, String componentId) {
            Boolean auth = instance.isPlayerAuthenticated(playerUUID);

            if(auth != null && auth == true)
                instance.sendComponentRemove(playerUUID, componentId);
        }

        @Override
        public void downloadResource(String playerUUID, Resource resource) {
            Boolean auth = instance.isPlayerAuthenticated(playerUUID);

            if(auth != null && auth == true)
                instance.sendResource(playerUUID, resource);
        }

    }

}
