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

package djxy.views;

import djxy.api.MinecraftGuiService;
import djxy.controllers.MainController;
import djxy.models.PluginInterface;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.event.entity.player.PlayerQuitEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ProviderExistsException;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.util.UUID;

@Plugin(id = "MinecraftGUIServer", name = "Minecraft GUI Server", version = "1.0")
public class Sponge implements PluginInterface {

    private Game game;
    private MainController mainController;

    public Sponge() {
        try {
            this.mainController = new MainController(this);
        } catch (Exception e) {}
    }

    @Subscribe
    protected void onPreInitializationEvent(PreInitializationEvent event) {
        game = event.getGame();
        new CommandGui(this, event.getGame());
        try {
            game.getServiceManager().setProvider(this, MinecraftGuiService.class, mainController.getMinecraftGuiService());
        } catch (ProviderExistsException e) {
            e.printStackTrace();
        }
        mainController.serverInit();
    }

    @Subscribe
    protected void onServerStartingEvent(ServerStartingEvent event) {
        mainController.serverIsStarting();
    }

    @Subscribe
    protected void onServerStoppingEvent(ServerStoppingEvent event) {
        mainController.serverIsStopping();
    }

    @Subscribe
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        mainController.playerJoin(event.getUser().getUniqueId().toString());
    }

    @Subscribe
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        mainController.playerQuit(event.getUser().getUniqueId().toString());
    }

    @Override
    public void sendAuthenticationCode(String playerUUID, String code) {
        try {
            Player player = game.getServer().getPlayer(UUID.fromString(playerUUID)).get();

            player.sendMessage(Texts.builder("Your authentication code: ").color(TextColors.GREEN).append(Texts.builder(code).color(TextColors.RED).append(Texts.builder(".").color(TextColors.GREEN).build()).build()).build());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void screenLoaded(String playerUUID) {
        try {
            Player player = game.getServer().getPlayer(UUID.fromString(playerUUID)).get();

            player.sendMessage(Texts.builder("Your screen is loaded.").color(TextColors.GREEN).build());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class CommandGui {

        private final CommandSpec command;

        public CommandGui(Object plugin, Game game) {
            command = CommandSpec.builder()
                    .description(Texts.of("MinecraftGUI command"))
                    .child(new CommandGuiConnectionState().command, "change", "c")
                    .child(new CommandGuiResetLocation().command, "reset", "r")
                    .child(new CommandGuiReload().command, "reload")
                    .build();

            game.getCommandDispatcher().register(plugin, command, "gui");
        }
    }

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
    }
}
