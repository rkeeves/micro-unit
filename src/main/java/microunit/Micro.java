package microunit;

import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Micro {

    public record CaseDef(String name, Set<String> tags, Runnable r) { }

    public record CaseReport(LinkedList<GroupDef> parents, CaseResult caseResult) { }

    public record CaseResult(String name, Set<String> tags, Instant start, Instant end, Status status) { }

    public static class Scores { int pass, fail, err_; }

    @FunctionalInterface
    public interface TestThunk extends Supplier<Rose<CaseDef, GroupDef>> { }

    public record Summary(Scores scores, List<CaseReport> caseReports) { }

    public record GroupDef(String name, Set<String> tags) { }

    public static Rose<CaseDef, GroupDef> test(String name, Runnable r) {
        return Rose.leaf(new CaseDef(name, Collections.emptySet(), r));
    }

    public static Rose<CaseDef, GroupDef> test(Set<String> tags, String name, Runnable r) {
        return Rose.leaf(new CaseDef(name, tags, r));
    }

    @SafeVarargs
    public static Rose<CaseDef, GroupDef> group(String name, Rose<CaseDef, GroupDef>... subtrees) {
        return Rose.node(new GroupDef(name, Collections.emptySet()), Arrays.asList(subtrees));
    }

    @SafeVarargs
    public static Rose<CaseDef, GroupDef> group(Set<String> tags, String name, Rose<CaseDef, GroupDef>... subtrees) {
        return Rose.node(new GroupDef(name, tags), Arrays.asList(subtrees));
    }

    public static Rose<CaseResult, GroupDef> runTree(Rose<CaseDef, GroupDef> tree) {
        return tree.cata(
                leaf -> {
                    Instant start = Instant.now();
                    try {
                        leaf.r().run();
                        return Rose.leaf(
                                new CaseResult(leaf.name(), leaf.tags(), start, Instant.now(), Status.pass())
                        );
                    } catch (AssertionError fail) {
                        return Rose.leaf(
                                new CaseResult(leaf.name(), leaf.tags(), start, Instant.now(), Status.fail(fail))
                        );
                    } catch (Throwable err) {
                        return Rose.leaf(
                                new CaseResult(leaf.name(), leaf.tags(), start, Instant.now(), Status.err(err))
                        );
                    }
                },
                Rose::node
        );
    }

    public static Summary summarize(Rose<CaseResult, GroupDef> r) {
        var caseReports = r.cata(
                leaf -> List.of(new CaseReport(new LinkedList<>(), leaf)),
                (group, reports) -> reports.stream()
                        .flatMap(leafs -> leafs.stream().peek(leaf -> leaf.parents().addFirst(group)))
                        .toList()
        );
        var scores = caseReports.stream().map(x -> x.caseResult().status())
                .collect(
                        Scores::new,
                        (s, x) -> x.match(
                                pass -> s.pass++,
                                fail -> s.fail++,
                                err_ -> s.err_++
                        ),
                        (sa, sb) -> {
                            sa.pass += sb.pass;
                            sa.fail += sb.fail;
                            sa.err_ += sb.err_;
                        });
        return new Summary(scores, caseReports);
    }

    public static void writeSummary(boolean writeTrace, PrintWriter w, Summary fr) {
        fr.caseReports().forEach(r -> {
            var title = Stream.concat(
                            r.parents().stream().map(GroupDef::name),
                            Stream.of(r.caseResult().name()))
                    .collect(Collectors.joining(" > "));
            var time = Duration.between(r.caseResult().start(), r.caseResult().end()).toMillis();
            r.caseResult().status().match(
                    pass -> {
                        w.printf("[PASS] %s (%d ms)%n", title, time);
                        return Unit.UNIT;
                    },
                    fail -> {
                        w.printf("[FAIL] %s (%d ms)%n  %s%n", title, time, fail.getMessage());
                        if (writeTrace) fail.printStackTrace(w);
                        return Unit.UNIT;
                    },
                    err_ -> {
                        w.printf("[ERR ] %s (%d ms)%n  %s%n", title, time, err_.getMessage());
                        if (writeTrace) err_.printStackTrace(w);
                        return Unit.UNIT;
                    });
        });
        var s = fr.scores();
        w
                .printf("----------------%n")
                .printf("Sum : %6d%n", s.pass + s.fail + s.err_)
                .printf("Pass: %6d%n", s.pass)
                .printf("Fail: %6d%n", s.fail)
                .printf("Err : %6d%n", s.err_)
                .flush();
    }
}
