package dto.impl;

import java.util.Map;

public class SheetBasicData {
    private String sheetName;
    private String sheetOwner;
    private PermissionType sheetPermission;
    private String sheetSize;

    public SheetBasicData(String sheetName, String sheetOwner, Map<String, PermissionType> sheetPermission, String sheetSize, String userName) {
        this.sheetName = sheetName;
        this.sheetOwner = sheetOwner;
        this.sheetPermission = sheetPermission.get(userName);
        if(this.sheetPermission == null){
            this.sheetPermission = sheetOwner.equals(userName) ? PermissionType.OWNER : PermissionType.NONE;

        }
        this.sheetSize = sheetSize;
    }

    public String getSheetName() {
        return sheetName;
    }

    public String getSheetOwner() {
        return sheetOwner;
    }

    public PermissionType getSheetPermission() {
        return sheetPermission;
    }

    public String getSheetSize() {
        return sheetSize;
    }
}
