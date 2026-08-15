[![Maven](https://maven-badges.sml.io/sonatype-central/org.lidiuma/math/badge.svg?style=flat&subject=Maven&color=blue)](https://maven-badges.sml.io/sonatype-central/org.lidiuma/math/)
# Math Library
Welcome!\
This is a math library made with one of the latest you can get Java, to be more specific, the [Valhalla Early Access 2](https://jdk.java.net/valhalla/) based on Java 26.\
If you are thinking "Valhalla?!" Yes, I understand, weirdly enough for many, Valhalla is coming soon!

The library does not use the classic [OOP](https://en.wikipedia.org/wiki/Object-oriented_programming) paradigm, but instead uses mainly [FP](https://en.wikipedia.org/wiki/Functional_programming) paradigms; immutability for data, and type-classes/traits for behavior.
For example for a `Vector3`, I can get an instance of `Vector3Ops` (ops stands for operations), of which I can use to call methods like `ops.dot(Vector3, Vector3)`,
but in mostly all cases, these convenience classes are more than enough:
- `Colors`
- `Matrices`
- `Points`
- `Rotations`
- `Tuples`
- `Vectors`

# Features
- JPMS support. (it's a top priority)
- Zero `null` usage and safe nullability API thanks to [Jspecify](https://jspecify.dev/)
- `Point1`, `Point2`, `Point3`, `Point4` (versions: `float`, `double`, `int`, `long`)
- `Vector1`, `Vector2`, `Vector3`, `Vector4` (versions: `float`, `double`, `int`, `long`)
- `Quaternion`, `Angle`, `AxisAngle`, `SwingTwist` for rotation. (versions: `float`, `double`)
- `Rectangle`, `Segment`, `Radius`, and `Triangle`. (versions: `float`, `double`)
- `Affine2`, `Affine3`, `Matrix3`, and `Matrix4`. (versions: `float`, `double`)

# How to Use
The library is on Maven Central and can be included with the following.

#### Gradle
```
implementation("org.lidiuma:math:0.2.0")
```
#### Bld
```
module("org.lidiuma", "math", "0.2.0")
```
To run the application, you'll need to provide a few runtime flags, since I'm using internal Valhalla APIs to provide better performance:
- `--enable-preview`
- `--add-exports=java.base/jdk.internal.value=lidiuma.math`
- `--add-exports=java.base/jdk.internal.vm.annotation=lidiuma.math`

## Why?
I was not satisfied with LibGDX APIs, since quite messy and outdated; they started with Java 6,
it has almost been 2 decades since this version was released!\
Either way, I had to abuse primitives to not kill the GC, making both of these things a *bad* coding experience.

This is why I'm remaking some of the math classes by following modern standards, like immutability.
My objective is to have these math classes be used in hot-paths without any worry, hence why I'm using Valhalla.

###### Outdated, now this is provided by the MathAPI
~~My dream/hope is that this becomes a standard for game related things to make library development easier between different frameworks,
I'll try my best to make this a reality, but it will likely just be one of those nice dreams... we'll see with time!~~

## Backwards compatibility?
No guarantees. I'm using an Early Access what do you expect?!\
I'll try to be careful, document any breakage, and version appropriately.
But there might be things out of my control, so this is something to keep in mind.\
To make you a bit relieved, I'll be the primary person using this library, and I'm quite lazy,
this means I won't have the motivation to change things (unless really needed), meaning breaking changes will probably be kept to a minimum.

## Special Thanks
To [LibGDX](https://libgdx.com/), I used it as a reference, even though all the code has seen so much refactor that there's no longer trace of the original LibGDX code.
But I still like to thank it for allowing me to bootstrap and have a more or less clear direction to follow.

## Building the project
**I'm using [bld](https://rife2.com/bld), a lightweight and easy-to-read build tool that compiles Java using Java.**
The build tool is quite easy to use, the only thing is you need to create the `java-version.txt` file and provide the Java directory.
Because of the lack of support, it's quite the pain to use with IntelliJ, hopefully with time this improves,
the good thing is I'm actively discussing with bld creators to improve on this. 

Said this, you can easily do (in the project root) `./bld download` to download the libraries and `./bld jar` to compile the jar.
