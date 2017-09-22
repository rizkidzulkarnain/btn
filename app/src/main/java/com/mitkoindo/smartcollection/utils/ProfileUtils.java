package com.mitkoindo.smartcollection.utils;

import android.content.Context;

import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.helper.ResourceLoader;

/**
 * Created by ericwijaya on 9/15/17.
 */

public class ProfileUtils {

    public static int BRANCH_COORDINATOR = 1;
    public static int SUPERVISOR = 2;
    public static int STAFF = 3;

    public static String getAccessToken(Context context) {
        return ResourceLoader.LoadAuthToken(context);
    }

    public static String getUserId(Context context) {
        return ResourceLoader.LoadUserID(context);
    }

    public static String getUserName(Context context) {
        return ResourceLoader.LoadUserName(context);
    }

    public static String getGroupId(Context context) {
        return ResourceLoader.LoadGroupID(context);
    }

    public static String getGroupName(Context context) {
        return ResourceLoader.LoadGroupName(context);
    }

    public static int getUserType(Context context) {
        String userGroupID = getGroupId(context);

        final String userGroup_Staff1 = context.getString(R.string.UserGroup_Staff1);
        final String userGroup_Staff2 = context.getString(R.string.UserGroup_Staff2);
        final String userGroup_Staff3 = context.getString(R.string.UserGroup_Staff3);
        final String userGroup_Supervisor1 = context.getString(R.string.UserGroup_Supervisor1);
        final String userGroup_Supervisor2 = context.getString(R.string.UserGroup_Supervisor2);
        final String userGroup_BranchCoordinator = context.getString(R.string.UserGroup_BranchCoordinator);
        final String userGroup_BranchManager = context.getString(R.string.UserGroup_BranchManager);

        if (userGroupID.equals(userGroup_Staff1) || userGroupID.equals(userGroup_Staff2) || userGroupID.equals(userGroup_Staff3)) {
            return STAFF;
        } else if (userGroupID.equals(userGroup_Supervisor1) || userGroupID.equals(userGroup_Supervisor2)) {
            return SUPERVISOR;
        } else {
            return BRANCH_COORDINATOR;
        }
    }
}
