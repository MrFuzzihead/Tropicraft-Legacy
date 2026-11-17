package net.tropicraft.config;

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Covers the coconut bomb whitelist parsing.
 * <p>
 * Two bugs lived here: an empty string split into {@code [""]} (a list with one nameless entry, so "is this player
 * whitelisted" was answering a question nobody asked), and the comma branch stripped <em>all</em> spaces, so
 * {@code "alice, bob"} only worked by accident. The whitelist is also now behind an on/off switch, because for
 * everybody who is not running a server the old behaviour - a bomb that refuses to work until you name yourself in a
 * config file - was the bug.
 */
public class ConfigMiscTest {

    private String originalWhitelist;

    @Before
    public void rememberConfig() {
        this.originalWhitelist = ConfigMisc.coconutBombWhitelist;
    }

    @After
    public void restoreConfig() {
        ConfigMisc.coconutBombWhitelist = this.originalWhitelist;
        ConfigMisc.coconutBombWhitelistedUsers = Arrays.asList(new String[0]);
    }

    private static void parse(final String value) {
        ConfigMisc.coconutBombWhitelist = value;
        new ConfigMisc().hookUpdatedValues();
    }

    @Test
    public void anEmptyWhitelistHasNoEntries() {
        parse("");
        Assert.assertTrue(ConfigMisc.coconutBombWhitelistedUsers.isEmpty());
    }

    @Test
    public void aNullWhitelistHasNoEntries() {
        parse(null);
        Assert.assertTrue(ConfigMisc.coconutBombWhitelistedUsers.isEmpty());
    }

    @Test
    public void commaSeparatedNamesAreAccepted() {
        parse("alice, bob");
        Assert.assertEquals(Arrays.asList("alice", "bob"), ConfigMisc.coconutBombWhitelistedUsers);
    }

    @Test
    public void spaceSeparatedNamesAreAccepted() {
        parse("alice bob");
        Assert.assertEquals(Arrays.asList("alice", "bob"), ConfigMisc.coconutBombWhitelistedUsers);
    }

    @Test
    public void extraWhitespaceIsIgnored() {
        parse("  alice ,, bob ,carol  ");
        Assert.assertEquals(Arrays.asList("alice", "bob", "carol"), ConfigMisc.coconutBombWhitelistedUsers);
    }

    @Test
    public void aSingleNameIsAccepted() {
        parse("alice");
        Assert.assertEquals(Arrays.asList("alice"), ConfigMisc.coconutBombWhitelistedUsers);
    }
}
