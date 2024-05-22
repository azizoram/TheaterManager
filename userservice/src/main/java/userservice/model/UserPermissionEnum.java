package userservice.model;

public enum UserPermissionEnum {
    Employee,
    Admin;

    public static UserPermissionEnum fromString(String permission) {
        return switch (permission.toUpperCase()) {
            case "EMPLOYEE" -> Employee;
            case "ADMIN" -> Admin;
            default -> null;
        };
    }

}

