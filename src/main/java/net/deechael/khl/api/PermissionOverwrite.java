package net.deechael.khl.api;

import net.deechael.khl.type.Permissions;

public interface PermissionOverwrite {

    int getAllow();

    void setAllow(int allow);

    int getDeny();

    void setDeny(int deny);

    void addAllow(Permissions permission);

    void addDeny(Permissions permission);

    void update();

}
