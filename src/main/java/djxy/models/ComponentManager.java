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

public interface ComponentManager {

    /**
     * Called when the player has been authenticated or when he reset his screen.
     *
     * @param playerUUID The player to init the his screen
     */
    void initPlayerGUI(String playerUUID);

    /**
     * Called when a player click on a button you are listening.
     *
     * @param playerUUID The player who send the form
     * @param form The form received
     */
    void receiveForm(String playerUUID, Form form);
    
}
