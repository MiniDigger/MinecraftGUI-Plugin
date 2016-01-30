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
import io.github.minecraftgui.models.components.Paragraph;
import io.github.minecraftgui.models.components.UserGui;
import io.github.minecraftgui.views.AdminPlugin;
import io.github.minecraftgui.views.MinecraftGuiService;
import io.github.minecraftgui.views.PluginInterface;
import io.github.minecraftgui.views.sponge.commands.ReloadCommand;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

@Plugin(id = "MinecraftGui", name = "Minecraft Gui Server", version = "2.0")
public class Sponge implements PluginInterface {

    @Inject
    private Game game;

    @Inject @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    private SpongeNetwork spongeNetwork;
    private MinecraftGuiService service;
    private AdminPlugin adminPlugin;

    @Listener
    public void onInitializationEvent(GameInitializationEvent event) {
        this.spongeNetwork = new SpongeNetwork(this, game);
        this.service = new MinecraftGuiService(spongeNetwork, this);

        adminPlugin = new AdminPlugin(service, defaultConfig.toString().substring(0, defaultConfig.toString().lastIndexOf(File.separator)));

        game.getServiceManager().setProvider(this, MinecraftGuiService.class, this.service);

        CommandSpec commandReload = CommandSpec.builder()
                .description(Text.of("MinecraftGui commands"))
                .executor(new ReloadCommand(spongeNetwork))
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("action"))))
                .build();

        CommandSpec commandDev = CommandSpec.builder()
                .description(Text.of("MinecraftGui command to start the dev mode."))
                .executor(new CommandExecutor() {
                    @Override
                    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
                        adminPlugin.setDevMode((Boolean) commandContext.getOne("state").get());
                        return CommandResult.success();
                    }
                })
                .arguments(GenericArguments.onlyOne(GenericArguments.bool(Text.of("state"))))
                .build();

        game.getCommandManager().register(this, commandReload, "gui");
        game.getCommandManager().register(this, commandDev, "guidev");
    }

    @Listener
    public void onStartingEvent(GameStartingServerEvent event) {
        this.spongeNetwork.sortPlugins();
    }

    @Override
    public UserGui getUserGui(String plugin, UUID player) {
        return this.spongeNetwork.getUserConnection(player).getUserGui(plugin);
    }

    @Override
    public String getPlayerName(UUID player) {
        return org.spongepowered.api.Sponge.getGame().getServer().getPlayer(player).get().getName();
    }

    @Override
    public void sendCommand(UUID sender, String command) {
        Task.Builder taskBuilder = game.getScheduler().createTaskBuilder();

        taskBuilder
                .execute(() -> {
                    org.spongepowered.api.Sponge.getGame().getCommandManager().process(org.spongepowered.api.Sponge.getGame().getServer().getPlayer(sender).get(), command);
                }).submit(this);
    }

    @Override
    public void setParagraphToWorldChangeEvent(UUID player, Paragraph paragraph) {
        game.getEventManager().registerListeners(this, new WorldChangeEvent(paragraph, player));

        paragraph.setText(org.spongepowered.api.Sponge.getGame().getServer().getPlayer(player).get().getWorld().getName());
    }

    @Override
    public void setParagraphToEconomyTransactionEvent(UUID player, Paragraph paragraph, String currency) {
        game.getEventManager().registerListeners(this, new EconomyTransactionEvent(paragraph, player, currency));

        EconomyService economyService = game.getServiceManager().provide(EconomyService.class).get();
        UniqueAccount account = economyService.getAccount(player).get();
        Currency curr = null;

        for(Currency c : economyService.getCurrencies())
            if(c.getDisplayName().toPlain().equalsIgnoreCase(currency))
                curr = c;

        if(curr != null)
            paragraph.setText(account.getBalance(curr).toString());
    }

}
