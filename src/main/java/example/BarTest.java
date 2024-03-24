package example;

import microunit.Micro.TestThunk;

import java.util.Set;

import static microunit.Assert.assertTrue;
import static microunit.Assert.fail;
import static microunit.Micro.group;
import static microunit.Micro.test;

public class BarTest {

    public static final TestThunk TESTS = () -> group("Bar",
            test("a", () -> {
                assertTrue(false);
            }),
            group("Nested bar",
                    test("dig", () -> {
                        assertTrue(true);
                    }),
                    test("deeper", () -> {
                        assertTrue(false);
                    })
            ),
            test(Set.of(Tags.SKIP),
                    "this test will be skipped", () -> {
                assertTrue(false);
            }),
            group(Set.of(Tags.SKIP),
                    "I'm a nested group, who must be skipped",
                    test("skip me because my parent group is skipped", () -> {
                        assertTrue(false);
                    }),
                    test("skip me too because my parent group is skipped", () -> {
                        assertTrue(true);
                    })
            ),
            test("b", () -> {
                assertTrue(false);
            }),
            test("c", () -> {
                assertTrue(true);
            }),
            test("f", () -> {
                fail("This is a message");
            })
    );
}
