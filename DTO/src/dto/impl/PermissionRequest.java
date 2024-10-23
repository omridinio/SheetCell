package dto.impl;

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

    public PermissionRequest(String owner, String sheetName) {
        this.username = owner;
        this.sheetName = sheetName;
        this.permission = PermissionType.OWNER;
        this.approved = true;
        this.owner = owner;
        this.status = "Approved";
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
    public String getStatus() {
        return status;
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

    public void delete() {
        counter--;
    }

    public void setStatus(String newStatus) {
        this.status = newStatus;
    }


}
