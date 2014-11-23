/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.example.app.robolectric.spec;

import android.app.Fragment;
import android.content.Intent;

import com.example.app.robolectric.support.EspressoSpec;
import com.example.presentation.R;
import com.example.presentation.view.activity.UserListActivity;
import com.google.android.apps.common.testing.ui.espresso.Espresso;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserListActivityTest extends EspressoSpec {


    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity().startActivity(createTargetIntent());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().startActivity(createTargetIntent());
            }
        });
        Espresso.closeSoftKeyboard();
    }

    public void testContainsAUserListFragment() {
        Fragment userListFragment =
                getCurrentActivity().getFragmentManager().findFragmentById(R.id.fragmentUserList);
        assertThat(userListFragment, is(notNullValue()));
    }

    public void testContainsProperTitle() {
        String actualTitle = getCurrentActivity().getTitle().toString().trim();

        assertThat(actualTitle, is("Users List"));
    }

    private Intent createTargetIntent() {
        Intent intentLaunchActivity =
                UserListActivity.getCallingIntent(getInstrumentation().getTargetContext());

        return intentLaunchActivity;
    }
}