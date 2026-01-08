package com.example.comp2000restuarantapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CourseworkApi {

    private static final String BASE_URL = "http://10.240.72.69/comp2000/coursework/";
    private static final String STUDENT_ID = "student_10929632";

    private final VolleySingleton volley;

    public CourseworkApi(Context context) {
        this.volley = VolleySingleton.getInstance(context);
    }

    // Custom Error class to hold more details
    public static class ApiError {
        private final String message;
        private final int statusCode;

        public ApiError(String message, int statusCode) {
            this.message = message;
            this.statusCode = statusCode;
        }

        public String getMessage() {
            return message;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }


    public interface ApiCallback<T> {
        void onSuccess(T data);
        void onError(ApiError error);
    }

    // Helper to parse VolleyError into our custom ApiError
    private ApiError parseVolleyError(VolleyError error) {
        if (error.networkResponse != null) {
            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
            return new ApiError("API Error: " + responseBody, error.networkResponse.statusCode);
        } else {
            // No network response, must be a client-side error (e.g., timeout, no connection)
            return new ApiError("Network error: " + error.getMessage(), 0);
        }
    }

    // POST create_student/{student_id}
    public void createStudentDatabase(final ApiCallback<String> cb) {
        String url = BASE_URL + "create_student/" + STUDENT_ID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    try {
                        String message = response.has("message") ? response.getString("message") : "Success";
                        cb.onSuccess(message);
                    } catch (JSONException e) {
                        cb.onSuccess("Student database action completed.");
                    }
                },
                error -> cb.onError(parseVolleyError(error)));
        volley.addToRequestQueue(request);
    }

    // POST create_user/{student_id}
    public void createUser(User user, final ApiCallback<String> cb) {
        String url = BASE_URL + "create_user/" + STUDENT_ID;
        JSONObject jsonBody = userToJson(user);
        if (jsonBody == null) {
            cb.onError(new ApiError("Failed to create JSON body", -1));
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> cb.onSuccess("User created successfully."),
                error -> cb.onError(parseVolleyError(error)));
        volley.addToRequestQueue(request);
    }

    // GET read_all_users/{student_id}
    public void readAllUsers(final ApiCallback<List<User>> cb) {
        String url = BASE_URL + "read_all_users/" + STUDENT_ID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<User> userList = new ArrayList<>();
                        if (response.has("users")) {
                            JSONArray array = response.getJSONArray("users");
                            for (int i = 0; i < array.length(); i++) {
                                userList.add(jsonToUser(array.getJSONObject(i)));
                            }
                        }
                        cb.onSuccess(userList);
                    } catch (JSONException e) {
                        cb.onError(new ApiError("JSON Parsing error: " + e.getMessage(), -1));
                    }
                },
                error -> cb.onError(parseVolleyError(error)));
        volley.addToRequestQueue(request);
    }

    // GET read_user/{student_id}/{username}
    public void readUser(String username, final ApiCallback<User> cb) {
        String encodedUsername = encode(username);
        String url = BASE_URL + "read_user/" + STUDENT_ID + "/" + encodedUsername;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        cb.onSuccess(jsonToUser(response));
                    } catch (JSONException e) {
                        cb.onError(new ApiError("JSON Parsing error: " + e.getMessage(), -1));
                    }
                },
                error -> cb.onError(parseVolleyError(error)));
        volley.addToRequestQueue(request);
    }

    // PUT update_user/{student_id}/{username}
    public void updateUser(String username, User user, final ApiCallback<User> cb) {
        String encodedUsername = encode(username);
        String url = BASE_URL + "update_user/" + STUDENT_ID + "/" + encodedUsername;
        JSONObject jsonBody = userToJson(user);
        if (jsonBody == null) {
            cb.onError(new ApiError("Failed to create JSON body for update", -1));
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                response -> {
                    try {
                        cb.onSuccess(jsonToUser(response));
                    } catch (JSONException e) {
                        // Fallback to returning the original user on JSON parse error
                        cb.onSuccess(user);
                    }
                },
                error -> cb.onError(parseVolleyError(error)));
        volley.addToRequestQueue(request);
    }

    // DELETE delete_user/{student_id}/{username}
    public void deleteUser(String username, final ApiCallback<String> cb) {
        String encodedUsername = encode(username);
        String url = BASE_URL + "delete_user/" + STUDENT_ID + "/" + encodedUsername;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> cb.onSuccess("User deleted successfully."),
                error -> cb.onError(parseVolleyError(error)));
        volley.addToRequestQueue(request);
    }

    // --- Helper Methods ---
    private String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    private JSONObject userToJson(User user) {
        try {
            JSONObject json = new JSONObject();
            json.put("username", user.getUsername());
            json.put("password", user.getPassword());
            json.put("firstname", user.getFirstname());
            json.put("lastname", user.getLastname());
            json.put("email", user.getEmail());
            json.put("contact", user.getContact());
            json.put("usertype", user.getUserType());
            return json;
        } catch (JSONException e) {
            Log.e("CourseworkApi", "Error creating JSON from User", e);
            return null;
        }
    }

    private User jsonToUser(JSONObject json) throws JSONException {
        JSONObject userJson = json.has("user") ? json.getJSONObject("user") : json;
        User user = new User();
        user.setUsername(userJson.optString("username", ""));
        user.setPassword(userJson.optString("password", ""));
        user.setFirstname(userJson.optString("firstname", ""));
        user.setLastname(userJson.optString("lastname", ""));
        user.setEmail(userJson.optString("email", ""));
        user.setContact(userJson.optString("contact", ""));
        user.setUserType(userJson.optString("usertype", ""));
        return user;
    }
}