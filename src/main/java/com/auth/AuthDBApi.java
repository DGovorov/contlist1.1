package com.auth;


public interface AuthDBApi {

    /*
    * API for Oauth2 authorisation process
    */

    /*
    * Returns value of "code" in USER table or null if email not found*/
    String getUserCodeByEmail(String email);

    /*
    * Returns value if "id" in USER table or null if email not found*/
    Long getUserIdByEmail(String email);

    /*
    * Returns value if "state" in USER table or null if email not found*/
    Long getUserStateByEmail(String email);

    /*Returns String array of user data
    * {User ID, First name, Last name, State, Email, Code}
    * */
    String[] getUserByEmail(String email);

    /*Sets new value for "code" in USER table*/
    void updateUserOauthCode(Long id, String code);

    /*
    * Looks for user with specific  email in table "USER". Returns true if email exists, false if not*/
    Boolean checkExistingEmail(String email);

    /*Creates new user in "USER" table with given data*/
    void createOauthUser(String name, String lastname, String email, String code, String state);

}
