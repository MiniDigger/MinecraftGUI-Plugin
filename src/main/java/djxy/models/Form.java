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

package djxy.models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class Form {
    
    private final String buttonId;
    private final HashMap<String, String> inputs;

    public Form(JSONObject object) {
        this.buttonId = (String) object.get("ButtonId");
        this.inputs = new HashMap<>();
        initForm(object);
    }

    public String getButtonId() {
        return buttonId;
    }

    public String getInput(String inputId){
        return inputs.get(inputId);
    }
    
    private void initForm(JSONObject obj){
        JSONArray array = (JSONArray) obj.get("Inputs");

        for (Object object : array) {
            JSONObject jsonObject = (JSONObject) object;

            inputs.put((String) jsonObject.get("Id"), (String) jsonObject.get("Value"));
        }
    }

    @Override
    public String toString(){
        return inputs.toString();
    }

}
