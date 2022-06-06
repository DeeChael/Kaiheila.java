package net.deechael.khl.entity;

public class PermissionOverwrite {

    private int targetRoleId;
    private String targetUserId;
    private int allow;
    private int deny;

    public int getTargetRoleId() {
        return targetRoleId;
    }

    public void setTargetRoleId(int targetRoleId) {
        this.targetRoleId = targetRoleId;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public int getAllow() {
        return allow;
    }

    public void setAllow(int allow) {
        this.allow = allow;
    }

    public int getDeny() {
        return deny;
    }

    public void setDeny(int deny) {
        this.deny = deny;
    }
}
