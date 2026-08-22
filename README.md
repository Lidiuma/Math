[![Maven](https://maven-badges.sml.io/sonatype-central/org.lidiuma/math/badge.svg?style=flat&subject=Maven&color=blue)](https://maven-badges.sml.io/sonatype-central/org.lidiuma/math/)
# Math Library
Welcome!\
This is a math library meant for Modern Java; the library does not use the classic [OOP](https://en.wikipedia.org/wiki/Object-oriented_programming) paradigm, but instead uses mainly [FP](https://en.wikipedia.org/wiki/Functional_programming) paradigms; immutability for data, and type-classes/traits for behavior.

For example, to calculate a dot product of a `Vector3`, I can get an instance of `Vector3Ops` (ops stands for operations) and call `ops.dot(Vector3, Vector3)`, or I could use the alias class and call `Vectors.dot(Vector3, Vector3)`.\
Since using instances to call methods is annoying, there are static-aliases classes that are more than enough in most cases:
- `Vectors`
- `Points`
- `Rotations`
- `Matrices`
- `Shapes`
- `Colors`
- `Tuples`

# Features
- JPMS support. (it's a top priority)
- Zero `null` usage and safe nullability API thanks to [Jspecify](https://jspecify.dev/)
- `Point1`, `Point2`, `Point3`, `Point4` (versions: `float`, `double`, `int`, `long`)
- `Vector1`, `Vector2`, `Vector3`, `Vector4` (versions: `float`, `double`, `int`, `long`)
- `Quaternion`, `Angle`, `AxisAngle`, `SwingTwist` for rotation. (versions: `float`, `double`)
- `Rectangle`, `Segment`, `Radius`, and `Triangle`. (versions: `float`, `double`)
- `Affine2`, `Affine3`, `Matrix3`, and `Matrix4`. (versions: `float`, `double`)
- Implementation of [math-api and math-traits](https://github.com/Lidiuma/MathAPI/).

# How to Use
There are 3 different version of this library available on Maven Central, one for each java version.

### Java 28 Preview
This is the default version of the library, it provides all classes with the JEP 401 `value` modifier.\
To compile/run `--enable-preview` needs to be provided, since JEP 401 is in preview.

**Gradle**\
```implementation("org.lidiuma:lidiuma-math:0.3.0")```\
**Bld**\
```module("org.lidiuma", "math", "0.3.0")```

### Java 17
Modified library to be compiled with Java 17, naturally all Valhalla features are striped out.

**Gradle**\
```implementation("org.lidiuma:lidiuma-math:0.3.0-j17")```\
**Bld**\
```module("org.lidiuma", "math", "0.3.0-j17")```

### Java Early Access
Special library version to embrace the latest you can get Valhalla, pocking at JVM internal to have Null-Restricted types, Loosely-Consistent Values, and more when available.
Naturally this means general instability, JVM crashes, etc., use at your own risk.\
To compile/run you need to provide a few flags, since I'm using internal APIs.
- `--enable-preview`
- `--add-exports=java.base/jdk.internal.value=lidiuma.math`
- `--add-exports=java.base/jdk.internal.vm.annotation=lidiuma.math`

**Gradle**\
```implementation("org.lidiuma:lidiuma-math:0.3.0-jea")```\
**Bld**\
```module("org.lidiuma", "math", "0.3.0-jea")```

## Why?
I was not satisfied with LibGDX APIs, since quite messy and outdated; they started with Java 6,
it has almost been 2 decades since this version was released!\
Either way, I had to abuse primitives to not kill the GC, making both of these things a *bad* coding experience.

This is why I'm remaking some of the math classes by following modern standards, like immutability.
My objective is to have these math classes be used in hot-paths without any worry, hence why I'm using Valhalla.

## Backwards compatibility?
Since this is a `0.x` version, there might be some breaking changes. 
The project is built on assumptions of future Java versions, and these can change, once they become stable, a `1.0` can be considered.

Naturally I'll try my best to keep breaking changes at a minimum and document any,
after all I'll be the main person using this library and I get how annoying breaking changes are.

## Performance
This is the question everybody wants to know, especially considering this library uses generics and all the abstraction to use self-made type-classes.\
I unfortunately have done the minimum amount of benchmark, all I can say is:
- Primitive code performs only ~14% faster.
- Generics, this one worries me the most, it's not an issue if code is kept monomorphic, but it's a hard thing to do. The library API should be monomorphic, so in case of issues I should be able to do something about it.
- All the abstraction code gets inlined AGGRESSIVELY, I'm sure there are a few methods having issues to inline, but I can easily write hand-specialized code to fix those cases. (please let me know if you find such cases, so I can fix them) 

## Special Thanks
To [LibGDX](https://libgdx.com/), I used it as a reference, even though all the code has seen so much refactor that there's no longer trace of the original LibGDX code.
But I still like to thank it for allowing me to bootstrap and have a more or less clear direction to follow.

## Building the project
**I'm using [bld](https://rife2.com/bld), a lightweight and easy-to-read build tool that compiles Java using Java.**\
The build tool is quite easy to use, for IntelliJ IDEA everything should come already configured (via the `.idea` directory).

There are a few modules; `math`, `processor`, `benchmark`, that have different purposes, the main project is the `math` module.
Here are some useful commands to get started:
- `./bld math download`, `./bld benchmark download` Downloads all the dependencies of the module.
- `./bld math purge`, `./bld benchmark purge` Deletes all the cached dependencies.
- `./bld math clean compile`, `./bld benchmark clean compile` cleans the old build files and compiles new ones.
- `./bld benchmark run` runs the benchmark.
- `./bld math test` runs the tests.
- `./bld math jar` creates the jar dist.
