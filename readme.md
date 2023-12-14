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
kept to the day you want to start with. Put your own AoC input in the implementation file and put
the following somewhere in the test class body:
```kotlin

    // DO NOT DELETE. Part 1 placeholder. To populate, verify solution on AoC then `./ka continue`

    // DO NOT DELETE. Part 2 placeholder. To populate, verify solution on AoC then `./ka continue`

```

Then update `Problem.kt` to just have that one day's part 1 as the only instance.

## Tactics index

### Binary search
* 2023-06

### Least common multiple
* 2023-08

### Parallel processing
* 2023-12

### Transpose matrix
* 2023-13

### Parsing
#### Recursive combinator
Note that betterparse seems to sometimes require that the recursive parser be on the right side of an `and`.
* 2023-06
 
#### Parsing something exactly n times
* 2023-07