package net.tropicraft.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Covers the {@code /tc} sub-commands added for §7 of the review: {@code version} (so a bug report can name a build),
 * {@code time sync}, and the {@code village} tools from issue #49.
 * <p>
 * The senders are mocks, which is exactly how {@code CommandBase} talks to a sender - {@code addChatMessage} is the
 * only thing these commands do with one.
 * </p>
 */
public class CommandTest {

    private static ICommandSender senderWith(final int permissionLevel) {
        final ICommandSender sender = Mockito.mock(ICommandSender.class);
        Mockito.when(sender.canCommandSenderUseCommand(4, "tc"))
            .thenReturn(permissionLevel >= 4);
        Mockito.when(sender.getPlayerCoordinates())
            .thenReturn(new ChunkCoordinates(0, 64, 0));
        return sender;
    }

    private static String lastMessage(final ICommandSender sender) {
        final ArgumentCaptor<IChatComponent> captor = ArgumentCaptor.forClass(IChatComponent.class);
        Mockito.verify(sender, Mockito.atLeastOnce())
            .addChatMessage(captor.capture());
        final List<IChatComponent> messages = captor.getAllValues();
        final IChatComponent last = messages.get(messages.size() - 1);
        return last.getUnformattedTextForChat();
    }

    @Test
    public void versionReportsTheModVersion() {
        final ICommandSender sender = senderWith(0);
        new CommandTCVersion().processCommand(sender, new String[0]);
        final String message = lastMessage(sender);
        Assert.assertTrue(message, message.startsWith("Tropicraft "));
        Assert.assertTrue("the message should name the build: " + message, message.contains("6."));
    }

    @Test
    public void timeTabCompletionOffersGetSetAndSync() {
        final ICommandSender sender = senderWith(0);
        final List<String> options = new CommandTCTime().addTabCompletionOptions(sender, new String[] { "" });
        Assert.assertNotNull(options);
        Assert.assertTrue(options.toString(), options.contains("get"));
        Assert.assertTrue(options.toString(), options.contains("set"));
        Assert.assertTrue(options.toString(), options.contains("sync"));
    }

    @Test
    public void timeSyncWithoutAServerSaysSoInsteadOfCrashing() {
        final ICommandSender sender = senderWith(0);
        new CommandTCTime().processCommand(sender, new String[] { "sync" });
        final String message = lastMessage(sender);
        Assert.assertTrue(message, message.contains("loaded"));
    }

    @Test(expected = WrongUsageException.class)
    public void timeSetWithoutATimeIsAWrongUsage() {
        new CommandTCTime().processCommand(senderWith(0), new String[] { "set" });
    }

    @Test(expected = CommandException.class)
    public void villageNeedsOperatorPermission() {
        new CommandTCVillage().processCommand(senderWith(2), new String[] { "list" });
    }

    @Test(expected = WrongUsageException.class)
    public void villageWithoutASubcommandIsAWrongUsage() {
        new CommandTCVillage().processCommand(senderWith(4), new String[0]);
    }

    @Test(expected = WrongUsageException.class)
    public void villageWithAnUnknownSubcommandIsAWrongUsage() {
        new CommandTCVillage().processCommand(senderWith(4), new String[] { "explode" });
    }

    @Test
    public void theRootListsEverySubcommand() {
        final String usage = new CommandTropicraft().getCommandUsage(senderWith(0));
        for (final String name : new String[] { "time", "weather", "tropics", "version", "village" }) {
            Assert.assertTrue(usage, usage.contains(name));
        }
    }

    @Test
    public void everySubcommandHasAUsageString() {
        final ICommandSender sender = senderWith(0);
        for (final CommandBase command : new CommandBase[] { new CommandTCTime(), new CommandTCWeather(),
            new CommandTropicsTeleport(), new CommandTCVersion(), new CommandTCVillage() }) {
            final String usage = command.getCommandUsage(sender);
            Assert.assertNotNull(command.getCommandName(), usage);
            Assert.assertTrue(command.getCommandName() + ": " + usage, usage.startsWith("/tc "));
        }
    }

    @Test
    public void versionIsReadableWithoutOperatorPermission() {
        Assert.assertEquals(0, new CommandTCVersion().getRequiredPermissionLevel());
        Assert.assertEquals(4, new CommandTCVillage().getRequiredPermissionLevel());
    }

    @Test
    public void chatMessagesAreChatComponents() {
        // Guards against the message-building being swapped for a plain String somewhere later.
        final ICommandSender sender = senderWith(0);
        Mockito.when(sender.getEntityWorld())
            .thenReturn(null);
        new CommandTCVersion().processCommand(sender, new String[0]);
        Mockito.verify(sender)
            .addChatMessage(Mockito.any(ChatComponentText.class));
    }
}
