package userservice.model;

public enum UserRoleEnum {
    ACTOR, TECHNICIAN, SOUND, ENGINEER, MAKE_UP, ARTIST;

    public static UserRoleEnum fromString(String role) {
        return switch (role.toUpperCase()) {
            case "ACTOR" -> ACTOR;
            case "TECHNICIAN" -> TECHNICIAN;
            case "SOUND" -> SOUND;
            case "ENGINEER" -> ENGINEER;
            case "MAKE_UP" -> MAKE_UP;
            case "ARTIST" -> ARTIST;
            default -> null;
        };
    }
}
