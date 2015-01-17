# AndroTools

## Synopsis
The goal of the AndroTools library is to provide recurring everyday functions that are useful in many, if not all apps.

Most of the classes in this library consist of purely static methods, in order to provide fast and easy access and minimize the memory footprint. Of course it also contains classes that either require instantiation or even derivation. Especially the latter classes aim to let you focus on the real task at hand, while providing the tedious "leg work" necessary for the actualy task in the background.

## License
As usual with my libraries, the AndroTools library is published under a simplified 2-clause BSD license.

## Availability
The AndroTools library is available through it's [GitHub.com repository](https://github.com/dimensionv/androtools), and through the central maven repository at [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Candrotools).

Simply add it as a dependency to your favorite build system / dependency management system, and you will be able to use it.

### Examples for Build-systems

#### Gradle

```
compile 'de.dimensionv:androtools:1.0.0'
```

#### Maven

```
<dependency>
    <groupId>de.dimensionv</groupId>
    <artifactId>androtools</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Documentation
The library is documented using JavaDoc. I took great care to explain everything properly, but feel free send patches with improvements where you think it's appropriate. In case I agree and find the time, I will update the library.
Besides being available through the same channels as the library itself, you can also read it directly online via my [javadoc-repository](http://javadoc.dimensionv.de/androtools/).

## Known Issues
Currently there are no issues.

However, if you find any bugs, feel free to

* report them
* fix them and send the patches