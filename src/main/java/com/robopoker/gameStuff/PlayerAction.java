package com.robopoker.gameStuff;

/**
 * User: Demishev
 * Date: 15.04.2014
 * Time: 13:46
 */
public class PlayerAction {
    public enum Type {
        READY
    }

    private final Type type;
    private final int value;

    public PlayerAction(Type type, int value) {
        this.value = value;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerAction)) return false;

        PlayerAction that = (PlayerAction) o;

        if (value != that.value) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + value;
        return result;
    }
}
