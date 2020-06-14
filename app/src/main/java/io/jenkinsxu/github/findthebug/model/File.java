package io.jenkinsxu.github.findthebug.model;

public class File {
    private Boolean containsBug = false;
    private Boolean isInvestigated = false;

    public File(Boolean containsBug) {
        this.containsBug = containsBug;
    }

    public Boolean isInvestigated() {
        return this.isInvestigated;
    }

    public void gotInvestigated() {
        this.isInvestigated = true;
    }

    public Boolean containsBug() {
        return this.containsBug;
    }

    public void debug() {
        this.containsBug = false;
    }
}
