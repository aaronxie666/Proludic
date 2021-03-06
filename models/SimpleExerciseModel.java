package icn.proludic.models;

/* this class is only used in the Add Workout fragment, because other exercise models had unnecessary member variables */

public class SimpleExerciseModel {

    private String id, name;
    private boolean isSelected;

    public SimpleExerciseModel(String id, String name) {
        this.id = id;
        this.name = name;
        this.isSelected = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
