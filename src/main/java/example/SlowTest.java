package example;

import microunit.Micro.TestThunk;

import static microunit.Assert.assertTrue;
import static microunit.Micro.test;

public class SlowTest {

    public static TestThunk A_SINGLE_TEST = () ->
            test("I take some time", () -> {
                int x = 0;
                for (int i = 0; i < 100000; i++)
                    for (int j = 1; j < 6969; j++)
                        x += j % 2 == 0 ? 1 : -1;
                assertTrue(x == 0);
            });
}
