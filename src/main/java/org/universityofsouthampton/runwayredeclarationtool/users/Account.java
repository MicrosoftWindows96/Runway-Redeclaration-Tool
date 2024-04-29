package org.universityofsouthampton.runwayredeclarationtool.users;

public class Account {
    private String username;
    private String password;
    private Boolean isAdmin = false; // Boolean flag that this account is an ADMIN
    private Boolean isEditor = false; // Boolean flag that this account is an EDITOR
    private Boolean isViewer = false; // Boolean flag that this account is an VIEWER

    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
        setRole(role);
    }

    public void setRole(String role) {
        // Reset boolean flags
        isAdmin = false;
        isEditor = false;
        isViewer = false;
        switch (role) {
            case "admin" -> isAdmin = true;
            case "editor" -> isEditor = true;
            case "viewer" -> isViewer = true;
        }
    }

    public String getRole() {
        if (isAdmin) {
            return "admin";
        } else if (isEditor) {
            return "editor";
        } else {
            return "viewer";
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}