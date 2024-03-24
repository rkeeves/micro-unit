package microunit;

public class Assert {

    public static void fail(String msg) {
        throw new AssertionError(msg);
    }

    public static void assertTrue(boolean x) {
        if (!x)
            throw new AssertionError("expected true");
    }

    public static void assertThrows(Class<?> cls, Runnable f) {
        try {
            f.run();
        } catch (Throwable t) {
            if (!t.getClass().equals(cls))
                throw new AssertionError("expected to throw " + cls.getName() + ", but was " + t.getClass().getName());
            return;
        }
        throw new AssertionError("expected to throw");
    }
}
