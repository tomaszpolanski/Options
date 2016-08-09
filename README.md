
# Options 
[![Build Status](https://travis-ci.org/tomaszpolanski/Options.svg?branch=master)](https://travis-ci.org/tomaszpolanski/Options)
[![Method count](https://img.shields.io/badge/Methods and size-core: 118 | deps: 20 | 16 KB-e91e63.svg)](http://www.methodscount.com/?lib=com.github.tomaszpolanski%3Aoptions%3A1.2.0)

[Functional Option](https://en.wikipedia.org/wiki/Option_type) that can be used with Java 1.7 and Android.

##Option

Similar to [Java 8 ``Optional``](http://www.oracle.com/technetwork/articles/java/java8-optional-2175753.html) but mainly targeting Android and Java 7 here. 

###Why to use them?

In 1965,  Sir Tony Hoare introduced ``null`` reference.  Since then he apologised for that and called it [The Billion Dollar Mistake](https://www.infoq.com/presentations/Null-References-The-Billion-Dollar-Mistake-Tony-Hoare). 
If the inventor of ``null`` pointers and references has condemned them, why the rest of us should be using them?


###Where to use them?

Currently there are several ways to say that an object could be null. Most popular way in Android nowadays is using ``Nullable`` or ``NonNull`` annotations. 
The problem with those annotations is that you cannot say what kind of objects are in an array, or if you use Rx, in an ``Observable``.
With ``Options`` you can use instead ```List<Option<String>>``` or in Rx ```Observable<Option<Integer>>```.

###How to use them?
If you have been using RxJava, this API will look really similar to RxJava.
Still ``Option`` is synchronous API that does not have too much to do with Reactive Programming.

**Basic usage**

Code with ``nulls``:

``` Java
String input = ...;
String result = null;

if (input != null && input.length() > 0) {
    result = "Length of the string is " + input.length();
} else {
    result = "The string is null or empty";
}
```

Code with ``Options``:

``` Java
String input = ...;

String result = Option.ofObj(input)
                      .filter(str -> str.length() > 0)
                      .match(str -> "Length of the string is " + str.length(),
                             ()  -> "The string is null or empty");
```

**Advance usage**

Code with ``nulls``:

```Java
String input = ...;
String result = null;

if (input != null) {
    try {
        int intValue = Integer.parseInt(input);
        result = "Input can be parsed to number: " + intValue;
    } catch (NumberFormatException e) {
        result = "Input is not a number";
    }
}
```

Code with ``Options``:

``` Java
String input = ...;

String result = Option.ofObj(input)
                      .flatMap(str -> Option.tryAsOption(() -> Integer.parseInt(str)))
                      .map(intValue -> "Input can be parsed to number: " + intValue)
                      .orDefault(() -> "Input is not a number");
```

###How to use them?
[![](https://jitpack.io/v/tomaszpolanski/options.svg)](https://jitpack.io/#tomaszpolanski/options)

Options are available on maven via [jitpack](https://jitpack.io/#tomaszpolanski/options/). Just add to your gradle files:

To your top level gradle.build file add repository:
``` Groovy
repositories { 
    jcenter()
    maven { url "https://jitpack.io" }
}
```

To your module level gradle.build add dependency: 
``` Groovy
dependencies {
    // other dependencies
    compile 'com.github.tomaszpolanski:options:1.2.0'
}
```

##References

This library was strongly influenced by [C# Functional Language Extensions](https://github.com/louthy/language-ext).
