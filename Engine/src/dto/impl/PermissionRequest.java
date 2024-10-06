package dto.impl;

import Mangger.PermissionType;

public class PermissionRequest {
    private static int counter = 0;
    private String username;
    private String sheetName;
    private PermissionType permission;
    private int index;
    private boolean approved;
    private String owner;
    private String status;

    public PermissionRequest(String username, String sheetName, PermissionType permission, String owner) {
        this.username = username;
        this.sheetName = sheetName;
        this.permission = permission;
        this.approved = false;
        this.index = counter++;
        this.owner = owner;
        this.status = "Pending";
    }

    public String getUsername() {
        return username;
    }

    public String getSheetName() {
        return sheetName;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public int getIndex() {
        return index;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PermissionRequest) {
            PermissionRequest other = (PermissionRequest) obj;
            return this.username.equals(other.username) && this.sheetName.equals(other.sheetName) && this.permission.equals(other.permission) && this.status.equals(other.status);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return username.hashCode() + sheetName.hashCode() + permission.hashCode();
    }
}
