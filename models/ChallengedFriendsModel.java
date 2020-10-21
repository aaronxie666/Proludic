package icn.proludic.models;

import java.io.Serializable;


/**
 * Author:  Chang XIE
 * Date: 24/07/2018
 * Package: icn.proludic.models
 * Project Name: proludic
 */

public class ChallengedFriendsModel implements Serializable {

    private String name;
    private String objectId;
    private String username;
    private int hearts;
    private String description;
    private String homePark;
    private String location = "";
    private Object profilePicture;
    private String challengedStartDate;
    private String challengedEndDate;
    private Number FriendScore;
    private Number UserScore;
    private boolean isWeight;

    public ChallengedFriendsModel (String objectId, String name, String username, Object profilePicUrl, String description, int hearts, String homePark, String challengedStartDate, String challengedEndDate, boolean isWeight, Number FriendScore, Number UserScore) {
        this.objectId = objectId;
        this.name = name;
        this.username = username;
        this.profilePicture = profilePicUrl;
        this.description = description;
        this.hearts = hearts;
        this.homePark = homePark;

        this.challengedStartDate = challengedStartDate;
        this.challengedEndDate = challengedEndDate;
        this.FriendScore = FriendScore;
        this.UserScore = UserScore;
        this.isWeight = isWeight;


    }

    public String getName() {
        return name;
    }

    public Object getProfilePicture() {
        return profilePicture;
    }

    public String getObjectId() { return objectId; }

    public String getUsername() { return username; }

    public String getDescription() {
        return description;
    }

    public int getHearts() { return hearts; }

    public String getHomePark() { return homePark; }

    public String getChallengedStartDate() { return challengedStartDate; }

    public String getChallengedEndDate() { return challengedEndDate; }

    public Number getFriendScore() { return FriendScore; }

    public Number getUserScore() { return UserScore; }

    public boolean isWeight() { return isWeight; }
}
