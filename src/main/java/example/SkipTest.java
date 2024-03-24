package example;

import microunit.Micro.TestThunk;

import java.util.Set;

import static microunit.Assert.assertThrows;
import static microunit.Micro.group;
import static microunit.Micro.test;

public class SkipTest {

    static final TestThunk TESTS = () -> group(
            Set.of(Tags.SKIP),
            "Skip my tests and me",
            test("skip me due to group", () -> {
                assertThrows(NullPointerException.class, () -> {
                    Object g = null;
                    g.equals(null);
                });
            })
    );
}
