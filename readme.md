My tiny framework for doing [Advent of Code](https://adventofcode.com/) problems in Kotlin. This is
a fun little project for me to learn more about Kotlin and Gradle.

Its core feature is a CLI command that generates the boilerplate code for each problem.  After
solving each problem, it will also generate a test so you can easily validate experimental
implementations.

## Usage

I logged into Advent with GitHub.

`gradle installDist` to build and setup the `ka` executable, but don't test.

`gradle build` to build and test everything.

`gradle test --tests '*2015-09*'` to test a specific class.

`./ka --help` (Kotlin Advent) to run. More info in help text.

`./ka continue --help` for more detailed help on the `continue` command.

## Forks

If you've forked this and want to write your own implementations, delete all the `YYYY-DD*`
implementation files except one, and similarly all the test files except one. Rename the classes you
kept to the day you want to start with. Put your own AoC input in the implementation file and the
following in the test class body:
```
    // DO NOT DELETE. Part 1 placeholder. Run `./ka continue after verifying solution to populate.

    // DO NOT DELETE. Part 2 placeholder. Run `./ka continue after verifying solution to populate.
```

Then update `Problem.kt` to just have that one day's part 1 as the only instance.
