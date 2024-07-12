package org.example.model;

import java.util.Objects;

public class MyBaseBlock extends BaseBlock {
    public String id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyBaseBlock that = (MyBaseBlock) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
