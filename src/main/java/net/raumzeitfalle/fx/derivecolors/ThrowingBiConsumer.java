package net.raumzeitfalle.fx.derivecolors;

@FunctionalInterface
public interface ThrowingBiConsumer<T,U,E extends Throwable> {
    void accept(T t, U u);
}
