package example;

import microunit.Micro.TestThunk;

import static microunit.Assert.assertThrows;
import static microunit.Assert.fail;
import static microunit.Micro.group;
import static microunit.Micro.test;

public class FooTest {

    static final TestThunk TESTS = () -> group("Foo",
            test("a", () -> {
                assertThrows(NullPointerException.class, () -> {
                    Object g = null;
                    g.equals(null);
                });
            }),
            test("b", () -> {
                assertThrows(NullPointerException.class, () -> {

                });
            }),
            test("c", () -> {
                (new int[0])[0]++;
            }),
            test("f", () -> {
                fail("This is another message");
            })
    );
}
