package Components.MangerSheet.PermissionsTable;

import dto.impl.PermissionRequest;

public class PermissionTable {
    private String permissionStatus;
    private String permissionType;
    private String username;

    public PermissionTable(PermissionRequest permissionRequest) {
        this.permissionStatus = permissionRequest.getStatus();
        this.permissionType = permissionRequest.getPermission().toString();
        this.username = permissionRequest.getUsername();
    }

    public String getPermissionStatus() {
        return permissionStatus;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public String getUsername() {
        return username;
    }
}
