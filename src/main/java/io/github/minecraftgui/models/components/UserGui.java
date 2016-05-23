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

package io.github.minecraftgui.models.components;

import io.github.minecraftgui.models.forms.Dropdown;
import io.github.minecraftgui.models.forms.Form;
import io.github.minecraftgui.models.forms.RadioButtonGroup;
import io.github.minecraftgui.models.listeners.OnGuiListener;
import io.github.minecraftgui.models.network.UserConnection;

import java.awt.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2015-12-29.
 */
public final class UserGui {

    private final UserConnection userConnection;
    private final ConcurrentHashMap<String, Form> forms;
    private final ConcurrentHashMap<String, Dropdown> dropdowns;
    private final ConcurrentHashMap<String, RadioButtonGroup> radioButtonGroups;
    private final ConcurrentHashMap<UUID, Component> components;
    private final ConcurrentHashMap<String, Component> componentsWithId;
    private final Root root;

    public UserGui( UserConnection userConnection ) {
        this.userConnection = userConnection;
        this.components = new ConcurrentHashMap<>();
        this.dropdowns = new ConcurrentHashMap<>();
        this.forms = new ConcurrentHashMap<>();
        this.radioButtonGroups = new ConcurrentHashMap<>();
        this.componentsWithId = new ConcurrentHashMap<>();
        this.root = new Root( this, userConnection );
    }

    public void addOnGuiOpenListener( OnGuiListener plugin, OnGuiListener.OnGuiOpen onGuiOpen ) {
        userConnection.addOnGuiOpenListener( plugin, onGuiOpen );
    }

    public void addOnGuiCloseListener( OnGuiListener plugin, OnGuiListener.OnGuiClose onGuiClose ) {
        userConnection.addOnGuiCloseListener( plugin, onGuiClose );
    }

    public Form getForm( String name ) {
        Form form = forms.get( name.toLowerCase() );

        if ( form != null ) {
            return forms.get( name.toLowerCase() );
        } else {
            form = new Form();

            forms.put( name.toLowerCase(), form );

            return form;
        }
    }

    public Dropdown getDropdown( String name ) {
        Dropdown dropdown = dropdowns.get( name.toLowerCase() );

        if ( dropdown != null ) {
            return dropdown;
        } else {
            dropdown = new Dropdown();

            dropdowns.put( name.toLowerCase(), dropdown );

            return dropdown;
        }
    }

    public RadioButtonGroup getRadioButtonGroup( String name ) {
        RadioButtonGroup radioButtonGroup = radioButtonGroups.get( name.toLowerCase() );

        if ( radioButtonGroup != null ) {
            return radioButtonGroup;
        } else {
            radioButtonGroup = new RadioButtonGroup();

            radioButtonGroups.put( name.toLowerCase(), radioButtonGroup );

            return radioButtonGroup;
        }
    }

    public UUID getPlayerUUID() {
        return userConnection.getUserUuid();
    }

    public Root getRoot() {
        return root;
    }

    public Component getComponent( String id ) {
        return componentsWithId.get( id );
    }

    public void addImage( String url, String name ) {
        userConnection.addImage( url, name );
    }

    public void addFont( String url ) {
        userConnection.addFont( url );
    }

    public void addFontToGenerate( String name, int size, Color color ) {
        userConnection.addFontToGenerate( name, size, color );
    }

    public void clear() {
        forms.clear();
        dropdowns.clear();
        radioButtonGroups.clear();
        components.clear();
        componentsWithId.clear();
    }

    protected final void addComponent( Component component ) {
        components.put( component.getUniqueId(), component );

        if ( component.getId() != null ) {
            componentsWithId.put( component.getId(), component );
        }
    }

    protected final void removeComponent( Component component ) {
        components.remove( component.getUniqueId() );

        if ( component.getId() != null ) {
            componentsWithId.remove( component.getId() );
        }
    }

}
