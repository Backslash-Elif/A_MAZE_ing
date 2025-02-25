package ch.bbcag.simulation;

public enum AlgorithmType {
    NEAREST("Nearest"),
    FARTHEST("Farthest"),
    LEFT("Turn left"),
    RIGHT("Turn right"),
    RANDOM("Random"),
    NESW("Predetermined: N E S W");

    private final String friendlyName;

    AlgorithmType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
