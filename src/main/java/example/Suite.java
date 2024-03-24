package example;

import java.io.PrintWriter;

import static microunit.Micro.*;

public class Suite {

    public static void main(String[] args) {
        group("Suite",
                FooTest.TESTS.get(),
                BarTest.TESTS.get(),
                SlowTest.A_SINGLE_TEST.get(),
                SkipTest.TESTS.get()
        ).filter(
                test -> !test.tags().contains(Tags.SKIP),
                group -> !group.tags().contains(Tags.SKIP)
        ).ifPresentOrElse(
                tree -> writeSummary(false, new PrintWriter(System.out), summarize(runTree(tree))),
                () -> System.out.println("Nothing to run...")
        );
    }
}
