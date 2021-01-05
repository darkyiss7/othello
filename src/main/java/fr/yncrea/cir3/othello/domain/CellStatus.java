package fr.yncrea.cir3.othello.domain;

public enum CellStatus {
    EMPTY(""), X("⬤"), O("◯");

    public final String string;

    private CellStatus(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
