# micro-unit but without reflection

## micro-unit? What is that?

It is a tiny introductory project for learning Reflection.

It is essentially a small set of playful little codes which mimics JUnit so that students can both:
- learn about Java,
- and see something in action!

There are some `micro-unit` implementations on GH:
- [impl a](https://github.com/jeszy75/micro-unit)
- [impl b](https://github.com/RolandMajor/microunit)
- [impl c](https://github.com/tothr94/microunit-10)

You can use annotations, and instantiate test classes by Reflection etc.

Pretty neat!

## Ok... But then what on Earth is this abomination?

I thought I'd make my own `micro-unit`, but with 3 ground rules:
1. No Reflection
2. No Annotations
3. No JUnit

Anyways... just run `Suite::main` and see text appear:

```
[PASS] Suite > Foo > a (1 ms)
[FAIL] Suite > Foo > b (0 ms)
  expected to throw
[ERR ] Suite > Foo > c (0 ms)
  Index 0 out of bounds for length 0
[FAIL] Suite > Foo > f (0 ms)
  This is another message
[FAIL] Suite > Bar > a (0 ms)
  expected true
[PASS] Suite > Bar > Nested bar > dig (0 ms)
[FAIL] Suite > Bar > Nested bar > deeper (0 ms)
  expected true
[FAIL] Suite > Bar > b (0 ms)
  expected true
[PASS] Suite > Bar > c (0 ms)
[FAIL] Suite > Bar > f (0 ms)
  This is a message
[PASS] Suite > I take some time (815 ms)
----------------
Sum :     11
Pass:      4
Fail:      6
Err :      1
```

_(you can turn on stack traces if you are a masochist)_

Here's how a test file looks like:

```java
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
                int[] a = new int[0];
                int x = a[0];
            }),
            test("f", () -> {
                fail("This is another message");
            })
    );
}
```

You can put together a bunch of these subtrees, filter what you want and run them.

Let the heap grow due to the explosion of intermediate lists and the overuse of lambdas!

Welcome to my `micro-unit` nightmare fuel!