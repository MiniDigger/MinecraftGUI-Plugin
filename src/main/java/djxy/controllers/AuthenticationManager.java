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

import djxy.models.ComponentManager;
import djxy.models.Form;
import djxy.models.component.Component;
import djxy.models.resource.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AuthenticationManager implements ComponentManager {

    private final static int maxTry = 3;
    private final static String inputId = "authenticationInput";
    private final static String buttonSendCodeInChatId = "authenticationSendCode";
    private final static String buttonAuthenticateId = "authenticationAuthenticate";

    private final HashMap<String, Boolean> playersAuthenticated;
    private final HashMap<String, Integer> playersTrying;
    private final HashMap<String, String> playersCode;
    private final MainController mainController;
    private final Component root;
    private final ArrayList<Resource> resources;

    public AuthenticationManager(MainController mainController) {
        this.mainController = mainController;
        playersAuthenticated = new HashMap<>();
        playersTrying = new HashMap<>();
        playersCode = new HashMap<>();
        root = ComponentFactory.load(new File(MainController.PATH+"/Authentication/authComponents.xml"), new File(MainController.PATH+"/Authentication/authAttributes.css"));
        resources = ResourceFactory.load(new File(MainController.PATH+"/Authentication/authResources.xml"));
    }

    public void addPlayerToAuthenticate(String playerUUID){
        playersAuthenticated.put(playerUUID, false);
    }

    public boolean isPlayerAuthenticated(String playerUUID){
        return playersAuthenticated.get(playerUUID);
    }

    @Override
    public void initPlayerGUI(String playerUUID) {
        for(Resource resource : resources)
            mainController.sendResource(playerUUID, resource);

        mainController.sendComponentCreate(playerUUID, root);

        initPlayerAuth(playerUUID);
    }

    @Override
    public void receiveForm(String playerUUID, Form form) {
        if(form.getButtonId().equals(buttonAuthenticateId)) {
            String codeReceived = form.getInput(inputId);
            String playerCode = playersCode.get(playerUUID);

            if (codeReceived.equals(playerCode)) {
                mainController.sendComponentRemove(playerUUID, "authenticationPanel");
                playersAuthenticated.put(playerUUID, true);
            } else {
                playersTrying.put(playerUUID, playersTrying.get(playerUUID) + 1);

                if (playersTrying.get(playerUUID) == maxTry)
                    mainController.closePlayerConnection(playerUUID);
            }
        }
        else if(form.getButtonId().equals(buttonSendCodeInChatId))
            sendPlayerCode(playerUUID);
    }

    private void sendPlayerCode(String playerUUID){
        mainController.getPluginInterface().sendAuthenticationCode(playerUUID, playersCode.get(playerUUID));
    }

    private void initPlayerAuth(String playerUUID) {
        String code = (new Random().nextInt(900000)+100000)+"";
        playersTrying.put(playerUUID, 0);
        playersCode.put(playerUUID, code);
        sendPlayerCode(playerUUID);
    }
}
