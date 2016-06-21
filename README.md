
# Options [![Build Status](https://travis-ci.org/tomaszpolanski/Options.svg?branch=master)](https://travis-ci.org/tomaszpolanski/Options)


Functional Option that can be used with Java 1.7 and Android.

##Option

Similar to Java 8 Optionals but mainly targeting Android and Java 7 here. 

###Why to use them?

In 1965,  Sir Tony Hoare introduced null reference.  Since then he apologised for that and called it [The Billion Dollar Mistake](https://www.infoq.com/presentations/Null-References-The-Billion-Dollar-Mistake-Tony-Hoare). 
If the inventor of null pointers and references has condemned them,  why the rest of us should be using them?


###Where to use them?

Currently there are several ways to say that an object could be null.  Most popular way in Android nowadays is using Nullable or NonNull annotations. 
The problem with those annotations is that you cannot say what kind of objects are in an array,  or if you use Rx,  in an observable.
With Options you can instead ```List<Option<String>>``` of in Rx ```Observable<Option<Integer>>```.

###How to use them?
If you have been using RxJava, this API will look really similar to RxJava.
Still Option is synchronous API that does not have too much to do with Reactive Programming.

Basic pattern matching:
``` Java
String result = Option.ofObj("This string might have been null")
                      .match(str -> "Length of the string is " + str.length(),
                             () -> "The string did not exist");
```

Operating on Option:
```Java
String result = Option.ofObj("This string might have been null")
                      .filter(str -> str.contains("null"))
                      .map(str -> "This String contains word null: " + str)
                      .ofDefault(() -> "Did not find matching word in the String");
```

Using flatMap:

```Java
String result = Option.ofObj(firstString)
                      .flatMap(first -> Option.ofObj(secondString)
                                              .map(second -> String.format("Both Strings are valid: %s,  %s", first,  second))
                      .ofDefault(() -> "Some of the Strings were invalid");
```

###How to use them?

Options are available on maven. Just add to your gradle files:

To your top level gradle.build file add repository:
```
repositories { 
        jcenter()
        maven { url "https://jitpack.io" }
}
```

To your module level gradle.build add dependency: 
```
dependencies {
    // other dependencies
    compile 'com.github.tomaszpolanski:options:1.1'
}
```

Current method count (in version 1.1) is 132 altogether.

##References

This library was strongly influenced by [C# Functional Language Extensions](https://github.com/louthy/language-ext).
