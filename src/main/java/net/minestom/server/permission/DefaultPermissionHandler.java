package net.minestom.server.permission;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class DefaultPermissionHandler implements PermissionHandler {

    private final Set<Permission> permissions = new CopyOnWriteArraySet<>();

    @Override
    public @NotNull Set<Permission> getAllPermissions() {
        return this.permissions;
    }

}
