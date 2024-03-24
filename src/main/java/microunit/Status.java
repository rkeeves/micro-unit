package microunit;

import java.util.function.Function;

public abstract sealed class Status {

    public static Status pass() {
        return new Pass();
    }

    public static Status fail(Throwable t) {
        return new Fail(t);
    }

    public static Status err(Throwable t) {
        return new Err(t);
    }

    public <A> A match(Function<Unit, A> pass, Function<Throwable, A> fail, Function<Throwable, A> err_) {
        if (this instanceof Pass __) {
            return pass.apply(Unit.UNIT);
        } else if (this instanceof Fail f) {
            return fail.apply(f.t);
        } else if (this instanceof Err e) {
            return err_.apply(e.t);
        }
        throw new Error("Not exhaustive");
    }

    private static final class Pass extends Status {
        Pass() { }
    }

    private static final class Fail extends Status {
        private final Throwable t;
        Fail(Throwable t) {
            this.t = t;
        }
    }

    private static final class Err extends Status {
        private final Throwable t;
        Err(Throwable t) {
            this.t = t;
        }
    }
}
