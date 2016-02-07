package polanski.jutils;

import org.junit.Test;

import polanski.jutils.option.Option;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static polanski.jutils.option.Option.NONE;
import static polanski.jutils.option.Option.ofObj;
import static polanski.jutils.option.Option.tryAsOption;
import static polanski.jutils.option.OptionUnsafe.getUnsafe;

public class TestOption {

    @Test
    public void testOfObj_whenSome() {

        final String str = "Something";
        Option<String> op = ofObj(str);

        assertTrue(op.isSome());
        assertEquals(str, getUnsafe(op));
    }

    @Test
    public void testOfObj_whenNone() {

        final String str = null;
        Option<String> op = ofObj(str);

        assertFalse(op.isSome());
    }

    @Test
    public void testMap_whenSome() {

        final String str = "Something";
        Option<String> op = ofObj("")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(final String __) {
                        return str;
                    }
                });

        assertTrue(op.isSome());
        assertEquals(str, getUnsafe(op));
    }

    @Test
    public void testMap_whenNone() {

        Option<String> op = ofObj((String) null)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(final String __) {
                        return "";
                    }
                });

        assertFalse(op.isSome());
    }

    @Test
    public void testFilter_whenSome() {

        final String str = "Something";
        Option<String> op = ofObj(str)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(final String val) {
                        return val.equals(str);
                    }
                });

        assertTrue(op.isSome());
        assertEquals(str, getUnsafe(op));
    }

    @Test
    public void testFilterSome_whenFailed() {

        final String str = "Something";
        Option<String> op = ofObj(str)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(final String val) {
                        return val.equals("");
                    }
                });

        assertFalse(op.isSome());
    }

    @Test
    public void testFilter_whenNone() {

        Option<String> op = ofObj((String) null)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(final String val) {
                        return val.equals("");
                    }
                });

        assertFalse(op.isSome());
    }

    @Test
    public void testFlatMap_whenSome() {

        final String str = "Something";
        Option<String> op = ofObj("")
                .flatMap(new Func1<String, Option<String>>() {
                    @Override
                    public Option<String> call(final String val) {
                        return ofObj(str);
                    }
                });

        assertTrue(op.isSome());
        assertEquals(str, getUnsafe(op));
    }

    @Test
    public void testFlatMapSome_whenFailed() {

        final String str = "Something";
        Option<String> op = ofObj(str)
                .flatMap(new Func1<String, Option<String>>() {
                    @Override
                    public Option<String> call(final String val) {
                        return ofObj((String) null);
                    }
                });

        assertFalse(op.isSome());
    }

    @Test
    public void testFlatMap_whenNone() {

        Option<String> op = ofObj((String) null)
                .flatMap(new Func1<String, Option<String>>() {
                    @Override
                    public Option<String> call(final String val) {
                        return ofObj("");
                    }
                });

        assertFalse(op.isSome());
    }

    @Test
    public void testOrOption_whenSome() {

        final String str = "Something";
        Option<String> op = ofObj(str)
                .orOption(new Func0<Option<String>>() {
                    @Override
                    public Option<String> call() {
                        return ofObj("");
                    }
                });

        assertTrue(op.isSome());
        assertEquals(str, getUnsafe(op));
    }

    @Test
    public void testOrOption_whenNone() {
        final String str = "Something";
        Option<String> op = ofObj((String) null)
                .orOption(new Func0<Option<String>>() {
                    @Override
                    public Option<String> call() {
                        return ofObj(str);
                    }
                });

        assertTrue(op.isSome());
        assertEquals(str, getUnsafe(op));
    }

    @Test
    public void testOrDefault_whenSome() {

        final String str = "Something";
        String s = ofObj(str)
                .orDefault(new Func0<String>() {
                    @Override
                    public String call() {
                        return "";
                    }
                });

        assertEquals(str, s);
    }

    @Test
    public void testOrDefault_whenNone() {
        final String str = "Something";
        String s = ofObj((String) null)
                .orDefault(new Func0<String>() {
                    @Override
                    public String call() {
                        return str;
                    }
                });

        assertEquals(str, s);
    }

    @Test
    public void testTryAsOption_whenSome() {

        final String str = "Something";
        Option<String> op = tryAsOption(
                new Func0<String>() {
                    @Override
                    public String call() {
                        return str;
                    }
                });

        assertTrue(op.isSome());
        assertEquals(str, getUnsafe(op));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testTryAsOption_whenNone() {
        final Integer str = null;

        //If converted to method reference then NPE is unavoidable
        @SuppressWarnings("Convert2MethodRef")
        Option<String> op = tryAsOption(
                new Func0<String>() {
                    @Override
                    public String call() {
                        return str.toString();
                    }
                });

        assertFalse(op.isSome());
    }

    @Test
    public void testMatchAction_whenSome() {
        final IFunction someFun = mock(IFunction.class);
        final IFunction noneFun = mock(IFunction.class);

        ofObj("").matchAction(new Action1<String>() {
                                  @Override
                                  public void call(final String it) {
                                      someFun.fun();
                                  }
                              },
                              new Action0() {
                                  @Override
                                  public void call() {
                                      noneFun.fun();
                                  }
                              });

        verify(someFun).fun();
        verify(noneFun, never()).fun();
    }

    @Test
    public void testMatchAction_whenNone() {
        final IFunction someFun = mock(IFunction.class);
        final IFunction noneFun = mock(IFunction.class);

        //noinspection unchecked
        NONE.matchAction(new Action1() {
                             @Override
                             public void call(final Object it) {
                                 someFun.fun();
                             }
                         },
                         new Action0() {
                             @Override
                             public void call() {
                                 noneFun.fun();
                             }
                         });

        verify(someFun, never()).fun();
        verify(noneFun).fun();
    }

    @Test
    public void testId() {
        final Option<String> op = ofObj("");

        assertEquals(op, op.id());
    }

    @Test
    public void testIfSome_whenSome() {
        final IFunction someFun = mock(IFunction.class);

        ofObj("").ifSome(new Action1<String>() {
            @Override
            public void call(final String it) {
                someFun.fun();
            }
        });

        verify(someFun, times(1)).fun();
    }

    @Test
    public void testIfSome_whenNone() {
        final IFunction noneFun = mock(IFunction.class);

        //noinspection unchecked
        NONE.ifSome(new Action1() {
            @Override
            public void call(final Object it) {
                noneFun.fun();
            }
        });

        verify(noneFun, never()).fun();
    }

    @Test
    public void testIfNone_whenSome() {
        final IFunction someFun = mock(IFunction.class);

        ofObj("").ifNone(new Action0() {
            @Override
            public void call() {
                someFun.fun();
            }
        });

        verify(someFun, never()).fun();
    }

    @Test
    public void testIfNone_whenNone() {
        final IFunction noneFun = mock(IFunction.class);

        //noinspection unchecked
        NONE.ifNone(new Action0() {
            @Override
            public void call() {
                noneFun.fun();
            }
        });

        verify(noneFun, times(1)).fun();
    }

    @Test
    public void testOfType_whenSomeAndProperType() {
        String value = "something";

        Option<String> op = ofObj((Object) value).ofType(String.class);

        assertTrue(op.isSome());
        assertEquals(value, getUnsafe(op));
    }

    @Test
    public void testOfType_whenSomeAndInvalidType() {
        String value = "something";

        Option<Integer> op = ofObj((Object) value).ofType(Integer.class);

        assertFalse(op.isSome());
    }

    @Test
    public void testOfType_whenNone() {
        //noinspection unchecked
        Option<String> op = NONE
                .ofType(String.class);

        assertFalse(op.isSome());
    }

    @Test
    public void testMatch_whenSome() {
        final String some = "some";
        final String none = "none";

        final String result = ofObj("").match(new Func1<String, String>() {
                                                  @Override
                                                  public String call(final String it) {
                                                      return some;
                                                  }
                                              },
                                              new Func0<String>() {
                                                  @Override
                                                  public String call() {
                                                      return none;
                                                  }
                                              });

        assertEquals(some, result);
    }

    @Test
    public void testMatch_whenNone() {
        final String some = "some";
        final String none = "none";

        //noinspection unchecked
        final String result = ofObj(null).match(new Func1<Object, String>() {
                                                    @Override
                                                    public String call(final Object it) {
                                                        return some;
                                                    }
                                                },
                                                new Func0<String>() {
                                                    @Override
                                                    public String call() {
                                                        return none;
                                                    }
                                                });

        assertEquals(none, result);
    }

    @Test
    public void testMatchUnsafe_whenSome() {
        final String some = "some";
        final String none = "none";

        final String result = ofObj("").matchUnsafe(new Func1<String, String>() {
                                                        @Override
                                                        public String call(final String it) {
                                                            return some;
                                                        }
                                                    },
                                                    new Func0<String>() {
                                                        @Override
                                                        public String call() {
                                                            return none;
                                                        }
                                                    });

        assertEquals(some, result);
    }

    @Test
    public void testMatchUnsafe_whenNone() {
        final String some = "some";
        final String none = "none";

        //noinspection unchecked
        final String result = ofObj(null).matchUnsafe(new Func1<Object, String>() {
                                                          @Override
                                                          public String call(final Object it) {
                                                              return some;
                                                          }
                                                      },
                                                      new Func0<String>() {
                                                          @Override
                                                          public String call() {
                                                              return none;
                                                          }
                                                      });

        assertEquals(none, result);
    }

    @Test
    public void testLift1_whenAllSome() {
        final Integer one = 1;
        final Integer two = 2;
        final Func2<Integer, Integer, Integer> fun = new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(final Integer f, final Integer s) {
                return f + s;
            }
        };

        final Option<Integer> op = ofObj(one).lift(ofObj(two), fun);

        assertTrue(op.isSome());
        assertEquals(fun.call(one, two), getUnsafe(op));
    }

    @Test
    public void testLift1_whenFirstIsNone() {
        final Integer one = 1;
        final Func2<Integer, Integer, Integer> fun = new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(final Integer f, final Integer s) {
                return f + s;
            }
        };

        //noinspection unchecked
        final Option<Integer> op = ofObj(one)
                .lift(NONE, fun);

        assertFalse(op.isSome());
    }

    @Test
    public void testLift1_whenSecondIsNone() {
        final Integer one = 1;
        final Func2<Integer, Integer, Integer> fun = new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(final Integer f, final Integer s) {
                return f + s;
            }
        };

        //noinspection unchecked
        final Option<Integer> op = NONE
                .lift(ofObj(one), fun);

        assertFalse(op.isSome());
    }

    @Test
    public void testLift2_whenAllSome() {
        final Integer one = 1;
        final Integer two = 2;
        final Integer three = 3;
        final Func3<Integer, Integer, Integer, Integer> fun =
                new Func3<Integer, Integer, Integer, Integer>() {
                    @Override
                    public Integer call(final Integer f, final Integer s, final Integer t) {
                        return f + s + t;
                    }
                };

        final Option<Integer> op = ofObj(one)
                .lift(ofObj(two), ofObj(three), fun);

        assertTrue(op.isSome());
        assertEquals(fun.call(one, two, three), getUnsafe(op));
    }

    @Test
    public void testLift2_whenFirstIsNone() {
        final Integer one = 1;
        final Integer two = 2;
        final Func3<Integer, Integer, Integer, Integer> fun =
                new Func3<Integer, Integer, Integer, Integer>() {
                    @Override
                    public Integer call(final Integer f, final Integer s, final Integer t) {
                        return f + s + t;
                    }
                };

        //noinspection unchecked
        final Option<Integer> op = NONE
                .lift(ofObj(one), ofObj(two), fun);

        assertFalse(op.isSome());
    }

    @Test
    public void testLift2_whenSecondIsNone() {
        final Integer one = 1;
        final Integer two = 2;
        final Func3<Integer, Integer, Integer, Integer> fun =
                new Func3<Integer, Integer, Integer, Integer>() {
                    @Override
                    public Integer call(final Integer f, final Integer s, final Integer t) {
                        return f + s + t;
                    }
                };

        //noinspection unchecked
        final Option<Integer> op = ofObj(one)
                .lift(NONE, ofObj(two), fun);

        assertFalse(op.isSome());
    }

    @Test
    public void testLift2_whenThirdIsNone() {
        final Integer one = 1;
        final Integer two = 2;
        final Func3<Integer, Integer, Integer, Integer> fun =
                new Func3<Integer, Integer, Integer, Integer>() {
                    @Override
                    public Integer call(final Integer f, final Integer s, final Integer t) {
                        return f + s + t;
                    }
                };

        //noinspection unchecked
        final Option<Integer> op = ofObj(one)
                .lift(ofObj(two), NONE, fun);

        assertFalse(op.isSome());
    }

    @Test
    public void testLift3_whenAllSome() {
        final Integer one = 1;
        final Integer two = 2;
        final Integer three = 3;
        final Integer four = 4;
        final Func4<Integer, Integer, Integer, Integer, Integer> fun =
                new Func4<Integer, Integer, Integer, Integer, Integer>() {
                    @Override
                    public Integer call(final Integer f, final Integer s, final Integer t,
                                        final Integer fo) {
                        return f + s + t + fo;
                    }
                };

        final Option<Integer> op = ofObj(one)
                .lift(ofObj(two), ofObj(three), ofObj(four), fun);

        assertTrue(op.isSome());
        assertEquals(fun.call(one, two, three, four),
                     getUnsafe(op));
    }

    @Test
    public void testLift3_whenAnyIsNone() {
        final Integer one = 1;
        final Integer two = 2;
        final Integer three = 3;
        final Func4<Integer, Integer, Integer, Integer, Integer> fun =
                new Func4<Integer, Integer, Integer, Integer, Integer>() {
                    @Override
                    public Integer call(final Integer f, final Integer s, final Integer t,
                                        final Integer fo) {
                        return f + s + t + fo;
                    }
                };

        //noinspection unchecked
        final Option<Integer> op = ofObj(one)
                .lift(ofObj(two), NONE, ofObj(three), fun);

        assertFalse(op.isSome());
    }

    @Test
    public void testLift3_whenFirstIsNone() {
        final Integer one = 1;
        final Integer two = 2;
        final Integer three = 3;
        final Func4<Integer, Integer, Integer, Integer, Integer> fun =
                new Func4<Integer, Integer, Integer, Integer, Integer>() {
                    @Override
                    public Integer call(final Integer f, final Integer s, final Integer t,
                                        final Integer fo) {
                        return f + s + t + fo;
                    }
                };

        //noinspection unchecked
        final Option<Integer> op =
                NONE.lift(ofObj(one), ofObj(two), ofObj(three), fun);
        assertFalse(op.isSome());
    }

    @Test
    public void testToString_whenSome() {
        final Integer value = 1;

        final String result = ofObj(value).toString();

        assertEquals(value.toString(), result);
    }

    @Test
    public void testToString_whenNone() {
        final String result = NONE.toString();

        assertEquals(NONE.getClass().getSimpleName(), result);
    }

    @Test
    public void testHashCode_whenSome() {
        final Integer value = 1;

        final int result = ofObj(value).hashCode();

        assertEquals(value.hashCode(), result);
    }

    @Test
    public void testHashCode_whenNone() {
        final int result = NONE.hashCode();

        assertEquals(0, result);
    }

    @Test
    public void testOrThrowUnsafe_whenSome() {
        final int value = 1;

        final int result = polanski.jutils.option.OptionUnsafe
                .orThrowUnsafe(ofObj(value), new RuntimeException());

        assertEquals(value, result);
    }

    @Test(expected = RuntimeException.class)
    public void testOrThrowUnsafe_whenNone() {
        polanski.jutils.option.OptionUnsafe
                .orThrowUnsafe(NONE, new RuntimeException());
    }

    @Test
    public void testLog_usesLoggingFunction() {
        final IFunction loggingFun = mock(IFunction.class);

        ofObj("").log(new Action1<String>() {
            @Override
            public void call(final String str) {
                loggingFun.fun(str);
            }
        });

        verify(loggingFun, times(1)).fun(anyString());
    }

    @Test
    public void testLog_containsValueOfSome() {
        final IFunction loggingFun = mock(IFunction.class);
        Option<String> option = ofObj("something");

        option.log(new Action1<String>() {
            @Override
            public void call(final String str) {
                loggingFun.fun(str);
            }
        });

        verify(loggingFun, times(1)).fun(option.toString());
    }

    @Test
    public void testLog_containsNone() {
        //noinspection unchecked
        Option<String> option = NONE;
        final IFunction loggingFun = mock(IFunction.class);

        option.log(new Action1<String>() {
            @Override
            public void call(final String str) {
                loggingFun.fun(str);
            }
        });

        verify(loggingFun, times(1)).fun(option.toString());
    }

    @Test
    public void testLog_usesLoggingFunctionWithTag() {
        final IFunction loggingFun = mock(IFunction.class);

        ofObj("").log("", new Action1<String>() {
            @Override
            public void call(final String str) {
                loggingFun.fun(str);
            }
        });

        verify(loggingFun, times(1)).fun(anyString());
    }

    @Test
    public void testLog_usesTag() {
        final String tag = "someTag";

        ofObj("").log(tag, new Action1<String>() {
            @Override
            public void call(final String str) {
                assertTrue(str.contains(tag));
            }
        });
    }

    @Test
    public void testLog_withTag_containsValueOfSome() {
        final Option<String> option = ofObj("something");

        option.log("any", new Action1<String>() {
            @Override
            public void call(final String str) {
                assertTrue(str.contains(option.toString()));
            }
        });
    }

    interface IFunction {

        void fun();

        void fun(String str);
    }

}
