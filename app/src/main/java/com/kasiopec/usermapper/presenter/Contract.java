package com.kasiopec.usermapper.presenter;

import com.kasiopec.usermapper.model.User;

import java.util.List;

public interface Contract {
    interface View{
        void updateUsersMarkers(User updatedUser);
        void displayMarkers(List<User> userList);
    }
    interface Presenter{
        void loadUsers();
        void updateUserData(User updatedUser);
        void dataInitialized(List<User> userList);
    }
}
