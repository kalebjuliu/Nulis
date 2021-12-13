package ac.umn.id.nulis.HelperClass;

public class Character {
    String chId, title, description, role, goal, outcome, strength, weakness, skills, imgUrl;
    public Character(){}
    public Character(String title, String description, String role, String goal, String outcome, String strength,
                     String weakness, String skills, String imgUrl){
        this.title = title;
        this.description = description;
        this.role = role;
        this.goal = goal;
        this.outcome = outcome;
        this.strength = strength;
        this.weakness = weakness;
        this.skills = skills;
        this.imgUrl = imgUrl;
    }
    public Character(String title, String description, String role, String goal, String outcome, String strength,
                     String weakness, String skills, String imgUrl, String chId){
        this.title = title;
        this.description = description;
        this.role = role;
        this.goal = goal;
        this.outcome = outcome;
        this.strength = strength;
        this.weakness = weakness;
        this.skills = skills;
        this.imgUrl = imgUrl;
        this.chId = chId;
    }

    public String getChId() {
        return chId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRole() {
        return role;
    }

    public String getGoal() {
        return goal;
    }

    public String getOutcome() {
        return outcome;
    }

    public String getStrength() {
        return strength;
    }

    public String getWeakness() {
        return weakness;
    }

    public String getSkills() {
        return skills;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
