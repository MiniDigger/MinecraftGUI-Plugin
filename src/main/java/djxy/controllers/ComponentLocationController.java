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

import djxy.models.component.Attributes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public final class ComponentLocationController {
    
    private static final String FILE = "/LocationsRelative.json";
    private final HashMap<String, HashMap<String, LocationRelative>> playersComponentLocationRelatives;

    public ComponentLocationController() {
        this.playersComponentLocationRelatives = new HashMap<>();
    }

    public void resetPlayerComponentLocationRelative(String playerUUID){
        playersComponentLocationRelatives.put(playerUUID, new HashMap<String, LocationRelative>());
    }

    public void setComponentLocationRelative(String playerUUID, String componentId, int xRelative, int yRelative){
        HashMap<String, LocationRelative> locationRelatives = playersComponentLocationRelatives.get(playerUUID);
        
        if(locationRelatives == null){
            playersComponentLocationRelatives.put(playerUUID, new HashMap<String, LocationRelative>());
            locationRelatives = playersComponentLocationRelatives.get(playerUUID);
        }
        
        locationRelatives.put(componentId.toLowerCase(), new LocationRelative(xRelative, yRelative));
    }
    
    public void setComponentLocation(String playerUUID, Attributes attributes){
        HashMap<String, LocationRelative> locationRelatives = playersComponentLocationRelatives.get(playerUUID);
        
        if(locationRelatives != null){
            LocationRelative locationRelative = locationRelatives.get(attributes.getId().toLowerCase());
            
            if(locationRelative != null){
                attributes.setXRelative(locationRelative.xRelative);
                attributes.setYRelative(locationRelative.yRelative);
            }
        }
    }
    
    public void load(){
        try {
            File file = new File(MainController.PATH+FILE);
            
            if(!file.exists()){
                file.createNewFile();
                return;
            }
            
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(new FileReader(file));
            
            for(Object obj : array){
                JSONObject object = (JSONObject) obj;
                setComponentLocationRelative((String) object.get("PlayerUUID"), (String) object.get("ComponentId"), (int) (long) object.get("XRelative"), (int) (long) object.get("YRelative"));
            }
        } catch (Exception ex) {}
    }
    
    public void save(){
        try {
            File file = new File(MainController.PATH+FILE);
            
            if(!file.exists())
                file.createNewFile();
            
            FileWriter fw = new FileWriter(file);
            
            JSONArray array = new JSONArray();
            
            for(Map.Entry pairs : playersComponentLocationRelatives.entrySet()){
                String playerUUID = (String) pairs.getKey();
                HashMap<String, LocationRelative> locationsRelative = (HashMap<String, LocationRelative>) pairs.getValue();
                
                for(Map.Entry pairs2 : locationsRelative.entrySet()){
                    JSONObject object = new JSONObject();
                    String componentId = (String) pairs2.getKey();
                    LocationRelative locationRelative = (LocationRelative) pairs2.getValue();
                    
                    object.put("PlayerUUID", playerUUID);
                    object.put("ComponentId", componentId);
                    object.put("XRelative", locationRelative.xRelative);
                    object.put("YRelative", locationRelative.yRelative);
                    array.add(object);
                }
            }
            
            fw.write(array.toJSONString());
            fw.close();
            fw.flush();
        }catch(Exception e){}
    }
    
    private class LocationRelative{
        
        private final int xRelative;
        private final int yRelative;

        public LocationRelative(int xRelative, int yRelative) {
            this.xRelative = xRelative;
            this.yRelative = yRelative;
        }
    }
    
}
