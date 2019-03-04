package com.urbanairship.tags;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class Users {
    private static Users sUsers;

    private static HashMap<String, Set<String>> users; // username, tags

    private Users() {
    }

    public static Users getInstance() {
        if(sUsers == null) {
            sUsers = new Users();
            users = new HashMap<>();
        }
        return sUsers;
    }

    public void update(String userName, Set<String> add, Set<String> remove) {

        if (users.containsKey(userName)) {
            // update user's tags
            users.get(userName).addAll(add);
            users.get(userName).removeAll(remove);

        } else {
            // add new user
            Set<String> tagSet = add.isEmpty() ? add : new HashSet<>();
            users.put(userName, tagSet);
        }
    }

    public String getResponse(String userName) {

        // get tags
        JSONArray tags = new JSONArray();
        for (String tag : users.get(userName)) {
            tags.put(tag);
        }

        String jsonString = new JSONObject()
                .put("user", userName)
                .put("tags", tags)
                .toString()
                + "\n";

        return jsonString;
    }
}
