package ch.fhnw.ima.bimgur.activiti.model.util;

/**
 * Types-safe wrapper around a raw id value.
 */
public class Id {

    protected static final String NONE = "none";

    private final String raw;

    public Id(String raw) {
        this.raw = (raw == null) ? NONE : raw;
    }

    public final String getRaw() {
        return raw;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Id id = (Id) o;

        return raw.equals(id.raw);
    }

    @Override
    public final int hashCode() {
        return raw.hashCode();
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName() + ": " + raw;
    }

}