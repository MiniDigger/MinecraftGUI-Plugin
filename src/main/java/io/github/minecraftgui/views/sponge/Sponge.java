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

package io.github.minecraftgui.views.sponge;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.network.ChannelRegistrationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "MinecraftGUIServer", name = "Minecraft GUI Server", version = "2.0")
public class Sponge  {

    @Inject private Logger logger;
    @Inject private Game game;
    /*private MainController mainController;

    public Sponge() {
        try {
            this.mainController = new MainController(this);
        } catch (Exception e) {}
    }*/

    @Listener
    public void onPreInitializationEvent(GamePreInitializationEvent event) {
        /*game = event.getGame();
        new CommandGui(this, event.getGame());
        try {
            game.getServiceManager().setProvider(this, MinecraftGuiService.class, mainController.getMinecraftGuiService());
        } catch (ProviderExistsException e) {
            e.printStackTrace();
        }*/
        //mainController.serverInit();
        new SpongeNetwork(this, game);
    }

    @Listener
    public void onChannel(ChannelRegistrationEvent event){
        System.out.println(event.getChannel());
    }

    @Listener
    public void onInitializationEvent(GameInitializationEvent event) {
        /*game = event.getGame();
        new CommandGui(this, event.getGame());
        try {
            game.getServiceManager().setProvider(this, MinecraftGuiService.class, mainController.getMinecraftGuiService());
        } catch (ProviderExistsException e) {
            e.printStackTrace();
        }*/
        //mainController.serverInit();
    }

    @Listener
    protected void onServerStartingEvent(GameStartingServerEvent event) {
        //mainController.serverIsStarting();
    }

    @Listener
    protected void onServerStoppingEvent(GameStoppingServerEvent event) {
        //mainController.serverIsStopping();
    }

    @Listener
    public void onPlayerJoinEvent(ClientConnectionEvent.Join event){
        //mainController.playerJoin(event.getProfile().getUniqueId().toString());
    }

    @Listener
    public void onPlayerQuitEvent(ClientConnectionEvent.Disconnect event){
        //mainController.playerQuit(event.getTargetEntity().getUniqueId().toString());
    }

    private class CommandGui {

        //private CommandSpec command;

        public CommandGui(Object plugin, Game game) {
            /*command = CommandSpec.builder()
                    .description(Texts.of("MinecraftGUI command"))
                    .child(new CommandGuiConnectionState().command, "change", "c")
                    .child(new CommandGuiResetLocation().command, "reset", "r")
                    .child(new CommandGuiReload().command, "reload")
                    .build();

            game.getCommandDispatcher().register(plugin, command, "gui");*/
        }
    }
/*
    private class CommandGuiReload implements CommandExecutor{

        private final CommandSpec command;

        public CommandGuiReload() {
            command = CommandSpec.builder()
                    .description(Texts.of("Reload the screen."))
                    .executor(this)
                    .build();
        }

        @Override
        public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
            if(commandSource instanceof Player) {
                Player player = (Player) commandSource;
                String playerUUID = player.getUniqueId().toString();

                if(mainController.isPlayerAuthenticated(playerUUID))
                    mainController.reloadPlayerScreen(playerUUID);
            }
            return CommandResult.success();
        }
    }

    private class CommandGuiResetLocation implements CommandExecutor{

        private final CommandSpec command;

        public CommandGuiResetLocation() {
            command = CommandSpec.builder()
                    .description(Texts.of("Reset to the default value all the location of your components."))
                    .executor(this)
                    .build();
        }

        @Override
        public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
            if(commandSource instanceof Player) {
                Player player = (Player) commandSource;
                String playerUUID = player.getUniqueId().toString();

                if(mainController.isPlayerAuthenticated(playerUUID)) {
                    mainController.resetPlayerComponentLocation(playerUUID);
                    commandSource.sendMessage(Texts.builder("Your screen has been reset.").color(TextColors.GREEN).build());
                }
            }
            return CommandResult.success();
        }
    }

    private class CommandGuiConnectionState implements CommandExecutor{

        private final CommandSpec command;

        public CommandGuiConnectionState() {
            command = CommandSpec.builder()
                    .description(Texts.of("Turn on/off the state of your connection."))
                    .executor(this)
                    .build();
        }

        @Override
        public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
            if(commandSource instanceof Player) {
                Player player = (Player) commandSource;
                String playerUUID = player.getUniqueId().toString();

                if(mainController.isPlayerAuthenticated(playerUUID)) {
                    String connectionState = mainController.changePlayerConnectionState(playerUUID) == true ? "on" : "off";

                    commandSource.sendMessage(Texts.builder("Connection state changed on ").color(TextColors.GREEN).append(Texts.builder(connectionState).color(TextColors.RED).append(Texts.builder(".").color(TextColors.GREEN).build()).build()).build());
                }
            }
            return CommandResult.success();
        }
    }*/
}
