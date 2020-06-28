package com.kasiopec.usermapper.presenter;
import com.kasiopec.usermapper.model.User;
import com.kasiopec.usermapper.model.UserModel;

import java.util.List;

public class Presenter implements Contract.Presenter {
    private Contract.View view;
    private UserModel userModel = new UserModel(this);

    public Presenter(Contract.View view){
        this.view = view;
    }

    @Override
    public void loadUsers() {
        userModel.startFetch();
    }

    @Override
    public void updateUserData(User updatedUser) {
        view.updateUsersMarkers(updatedUser);
    }

    @Override
    public void dataInitialized(List<User> userList) {
        view.displayMarkers(userList);
    }
}
