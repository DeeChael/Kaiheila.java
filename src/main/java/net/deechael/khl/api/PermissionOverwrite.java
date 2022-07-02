package net.deechael.khl.api;

import net.deechael.khl.type.Permissions;

public interface PermissionOverwrite {

    int getAllow();

    int getDeny();

    void setAllow(int allow);

    void addAllow(Permissions permission);

    void addDeny(Permissions permission);

    void setDeny(int deny);

    void update();

}
