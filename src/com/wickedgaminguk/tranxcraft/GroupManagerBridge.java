package com.wickedgaminguk.tranxcraft;

import java.util.Arrays;
import java.util.List;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;

public class GroupManagerBridge {

    private final GroupManager groupManager;

    public GroupManagerBridge(GroupManager gmPlugin) {
        this.groupManager = gmPlugin;
    }

    public String getGroup(final Player base) {
        final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(base);
        if (handler == null) {
            return null;
        }
        return handler.getGroup(base.getName());
    }

    public boolean setGroup(final Player base, final String group) {
        final OverloadedWorldHolder handler = groupManager.getWorldsHolder().getWorldData(base);
        if (handler == null) {
            return false;
        }
        handler.getUser(base.getName()).setGroup(handler.getGroup(group));
        return true;
    }

    public List<String> getGroups(final Player base) {
        final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(base);
        if (handler == null) {
            return null;
        }
        return Arrays.asList(handler.getGroups(base.getName()));
    }

    public String getPrefix(final Player base) {
        final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(base);
        if (handler == null) {
            return null;
        }
        return handler.getUserPrefix(base.getName());
    }

    public String getSuffix(final Player base) {
        final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(base);
        if (handler == null) {
            return null;
        }
        return handler.getUserSuffix(base.getName());
    }

    public boolean hasPermission(final Player base, final String node) {
        final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(base);
        if (handler == null) {
            return false;
        }
        return handler.has(base, node);
    }
}
