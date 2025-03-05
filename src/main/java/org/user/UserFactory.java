package org.user;

public class UserFactory {
    public static User<?> createUser(AccountType accountType) {
        switch (accountType) {
            case Regular:
                return new Regular<>();
            case Contributor:
                return new Contributor<>();
            case Admin:
                return new Admin<>();
            default:
                throw new IllegalArgumentException("Invalid account type: " + accountType);
        }
    }
}
