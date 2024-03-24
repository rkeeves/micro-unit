package microunit;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public sealed abstract class Rose<L, N> {
    public static <L, N> Rose<L, N> leaf(L l) {
        return new Leaf<>(l);
    }

    public static <L, N> Rose<L, N> node(N n, List<Rose<L, N>> children) {
        return new Node<>(n, children);
    }

    public <A> A cata(Function<L, A> f, BiFunction<N, List<A>, A> g) {
        if (this instanceof Leaf<L, N> l){
            return f.apply(l.label);
        } else if (this instanceof Node<L, N> n) {
            return g.apply(n.label, n.children.stream().map(c -> c.cata(f, g)).toList());
        }
        throw new Error("Non exhaustive");
    }

    public Optional<Rose<L, N>> filter(Predicate<L> f, Predicate<N> g) {
        return cata(
                l -> f.test(l) ? Optional.of(leaf(l)) : Optional.empty(),
                (n, xs) -> g.test(n) ? Optional.of(
                        node(n, xs.stream().flatMap(Optional::stream).toList()
                )) : Optional.empty()
        );
    }

    private static final class Leaf<L, N> extends Rose<L, N> {
        private final L label;
        Leaf(L label) {
            this.label = label;
        }
    }

    private static final class Node<L, N> extends Rose<L, N> {
        private final N label;
        private final List<Rose<L, N>> children;
        Node(N label, List<Rose<L, N>> children) {
            this.label = label;
            this.children = children;
        }
    }
}
