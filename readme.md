My tiny framework for doing [Advent of Code](https://adventofcode.com/) problems in Kotlin. This is
a fun little project for me to learn more about Kotlin and Gradle.

Its core feature is a CLI command that generates the boilerplate code for each problem.  After
solving each problem, it will also generate a test so you can easily validate experimental
implementations.

## Usage

I logged into Advent with GitHub.

`gradle assemble` to build but not test (tests will add up and start taking a long time).

`gradle build` to build and test

`./ka --help` (Kotlin Advent) to run. More info in help text.

`./ka continue --help` for more detailed help on the `continue` command.

## Forks

If you've forked this and want to write your own implementations, delete all the `YYYY-DD*`
implementation files except one, and all the test files. Rename the one you kept to the day you want
to start with and put your own input in there. Then update `Problem.kt` to just have that one day as
the only instance.
