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

package io.github.minecraftgui.models.network.packets;

import io.github.minecraftgui.controllers.NetworkController;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samuel on 2015-12-12.
 */
public class PacketInitClient extends PacketOut {

    private final ArrayList<String> fonts;
    private final HashMap<String, String> images;
    private final HashMap<String, HashMap<Color, ArrayList<Integer>>> fontsGenerate;

    public PacketInitClient( ArrayList<String> fonts, HashMap<String, String> images, HashMap<String, HashMap<Color, ArrayList<Integer>>> fontsGenerate ) {
        this.fonts = fonts;
        this.images = images;
        this.fontsGenerate = fontsGenerate;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        JSONArray fonts = new JSONArray();
        JSONArray images = new JSONArray();
        JSONArray fontsToGenerate = new JSONArray();

        for ( String font : this.fonts ) {
            fonts.put( font );
        }

        for ( Map.Entry pairs : this.images.entrySet() ) {
            images.put( new JSONObject().put( NetworkController.URL, pairs.getKey() ).put( NetworkController.NAME, pairs.getValue() ) );
        }

        for ( Map.Entry pairs : this.fontsGenerate.entrySet() ) {
            JSONObject font = new JSONObject();
            JSONArray colorList = new JSONArray();

            for ( Map.Entry pairs1 : ( (HashMap<Color, Integer>) pairs.getValue() ).entrySet() ) {
                JSONObject colorObj = new JSONObject();
                JSONArray sizeList = new JSONArray();
                Color color = (Color) pairs1.getKey();
                ArrayList<Integer> sizes = (ArrayList) pairs1.getValue();

                for ( Integer size : sizes ) {
                    sizeList.put( size );
                }

                colorObj.put( NetworkController.R, color.getRed() );
                colorObj.put( NetworkController.G, color.getGreen() );
                colorObj.put( NetworkController.B, color.getBlue() );
                colorObj.put( NetworkController.A, color.getAlpha() );
                colorObj.put( NetworkController.LIST, sizeList );
                colorList.put( colorObj );
            }

            font.put( NetworkController.NAME, pairs.getKey() );
            font.put( NetworkController.LIST, colorList );
            fontsToGenerate.put( font );
        }

        jsonObject.put( NetworkController.FONTS, fonts );
        jsonObject.put( NetworkController.IMAGES, images );
        jsonObject.put( NetworkController.FONTS_TO_GENERATE, fontsToGenerate );

        return jsonObject;
    }

}
