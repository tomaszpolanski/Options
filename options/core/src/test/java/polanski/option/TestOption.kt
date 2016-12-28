package polanski.option

import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Matchers.anyString
import org.mockito.Mockito.*
import polanski.option.Option.*
import polanski.option.OptionUnsafe.getUnsafe
import polanski.option.function.Func2
import polanski.option.function.Func3
import polanski.option.function.Func4

class TestOption {

    val fun2 = Func2<Int, Int, Int> { f, s -> f + s }
    val fun3 = Func3<Int, Int, Int, Int> { f, s, t -> f + s + t }
    val fun4 = Func4<Int, Int, Int, Int, Int> { f, s, t, fo -> f + s + t + fo }

    @Test
    fun testOfObj_whenSome() {
        val str = "Something"
        val op = ofObj(str)

        assertThat(op.isSome).isTrue()
        assertEquals(str, getUnsafe(op))
    }

    @Test
    fun testOfObj_whenNone() {
        val str: String? = null
        val op = ofObj(str)

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testIsSome_whenSome() {
        val op = ofObj("Something")

        assertThat(op.isSome).isTrue()
    }

    @Test
    fun testIsSome_whenNone() {
        val op = none<String>()

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testIsNone_whenSome() {
        val op = ofObj("Something")

        assertThat(op.isNone).isFalse()
    }

    @Test
    fun testIsNone_whenNone() {
        val op = none<String>()

        assertThat(op.isNone).isTrue()
    }

    @Test
    fun testMap_whenSome() {
        val str = "Something"
        val op = ofObj("").map { str }

        assertThat(op.isSome).isTrue()
        assertEquals(str, getUnsafe(op))
    }

    @Test
    fun testMap_whenNone() {
        val op = none<String>().map { "" }

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testFilter_whenSome() {
        val str = "Something"
        val op = ofObj(str).filter { it == str }

        assertThat(op.isSome).isTrue()
        assertEquals(str, getUnsafe(op))
    }

    @Test
    fun testFilterSome_whenFailed() {
        val str = "Something"
        val op = ofObj(str).filter { it == "" }

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testFilter_whenNone() {
        val op = none<String>().filter { it == "" }

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testFlatMap_whenSome() {
        val str = "Something"
        val op = ofObj("").flatMap { ofObj(str) }

        assertThat(op.isSome).isTrue()
        assertEquals(str, getUnsafe(op))
    }

    @Test
    fun testFlatMapSome_whenFailed() {
        val op = ofObj("Something").flatMap { none<String>() }

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testFlatMap_whenNone() {
        val op = none<String>().flatMap { ofObj("") }

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testOrOption_whenSome() {
        val str = "Something"
        val op = ofObj(str).orOption { ofObj("") }

        assertThat(op.isSome).isTrue()
        assertEquals(str, getUnsafe(op))
    }

    @Test
    fun testOrOption_whenNone() {
        val str = "Something"
        val op = none<String>().orOption { ofObj(str) }

        assertThat(op.isSome).isTrue()
        assertEquals(str, getUnsafe(op))
    }

    @Test
    fun testOrDefault_whenSome() {
        val str = "Something"
        val s = ofObj(str).orDefault { "" }

        assertEquals(str, s)
    }

    @Test
    fun testOrDefault_whenNone() {
        val str = "Something"
        val s = none<String>().orDefault { str }

        assertEquals(str, s)
    }

    @Test
    fun testTryAsOption_whenSome() {
        val str = "Something"
        val op = tryAsOption { str }

        assertThat(op.isSome).isTrue()
        assertEquals(str, getUnsafe(op))
    }

    @Test
    fun testTryAsOption_whenNone() {
        val str: Int? = null

        val op = tryAsOption { str!!.toString() }

        assertFalse(op.isSome)
    }

    @Test
    fun testMatchAction_whenSome() {
        val someFun = mock(IFunction::class.java)
        val noneFun = mock(IFunction::class.java)

        ofObj("").matchAction({ someFun.`fun`() })
        { noneFun.`fun`() }

        verify(someFun).`fun`()
        verify(noneFun, never()).`fun`()
    }

    @Test
    fun testMatchAction_whenNone() {
        val someFun = mock(IFunction::class.java)
        val noneFun = mock(IFunction::class.java)

        NONE.matchAction(
                { someFun.`fun`() }
                , { noneFun.`fun`() })

        verify(someFun, never()).`fun`()
        verify(noneFun).`fun`()
    }

    @Test
    fun testId() {
        val op = ofObj("")

        assertEquals(op, op.id())
    }

    @Test
    fun testIfSome_whenSome() {
        val someFun = mock(IFunction::class.java)

        ofObj("").ifSome { someFun.`fun`() }

        verify(someFun, times(1)).`fun`()
    }

    @Test
    fun testIfSome_whenNone() {
        val noneFun = mock(IFunction::class.java)

        NONE.ifSome { noneFun.`fun`() }


        verify(noneFun, never()).`fun`()
    }

    @Test
    fun testIfNone_whenSome() {
        val someFun = mock(IFunction::class.java)

        ofObj("").ifNone { someFun.`fun`() }

        verify(someFun, never()).`fun`()
    }

    @Test
    fun testIfNone_whenNone() {
        val noneFun = mock(IFunction::class.java)

        NONE.ifNone { noneFun.`fun`() }

        verify(noneFun, times(1)).`fun`()
    }

    @Test
    fun testOfType_whenSomeAndProperType() {
        val value = "something"

        val op = ofObj(value as Any).ofType(String::class.java)

        assertThat(op.isSome).isTrue()
        assertEquals(value, getUnsafe(op))
    }

    @Test
    fun testOfType_whenSomeAndInvalidType() {
        val value = "something"

        val op = ofObj(value as Any).ofType(Int::class.java)

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testOfType_whenNone() {
        val op = none<Int>().ofType(String::class.java)

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testMatch_whenSome() {
        val some = "some"
        val none = "none"

        val result = ofObj("").match({ some })
        { none }

        assertEquals(some, result)
    }

    @Test
    fun testMatch_whenNone() {
        val none = "none"

        val result = ofObj<Any>(null).match({ "some" })
        { none }

        assertEquals(none, result)
    }

    @Test
    fun testMatchUnsafe_whenSome() {
        val some = "some"

        val result = ofObj("").matchUnsafe({ some })
        { "none" }

        assertEquals(some, result)
    }

    @Test
    fun testMatchUnsafe_whenNone() {
        val none = "none"

        val result = ofObj<Any>(null).matchUnsafe({ "some" })
        { none }

        assertEquals(none, result)
    }

    @Test
    fun testLift1_whenAllSome() {
        val one = 1
        val two = 2

        val op = ofObj(one).lift(ofObj(two), fun2)

        assertThat(op.isSome).isTrue()
        assertEquals(fun2.call(one, two), getUnsafe(op))
    }

    @Test
    fun testLift1_whenFirstIsNone() {
        val op = ofObj(1).lift<Int, Int>(none(), fun2)

        assertFalse(op.isSome)
    }

    @Test
    fun testLift1_whenSecondIsNone() {
        val op = none<Int>().lift(ofObj(1), fun2)

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testLift2_whenAllSome() {
        val one = 1
        val two = 2
        val three = 3

        val op = ofObj(one).lift(ofObj(two), ofObj(three), fun3)

        assertThat(op.isSome).isTrue()
        assertEquals(fun3.call(one, two, three), getUnsafe(op))
    }

    @Test
    fun testLift2_whenFirstIsNone() {
        val op = none<Int>().lift(ofObj(1), ofObj(2), fun3)

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testLift2_whenSecondIsNone() {
        val op = ofObj(1).lift<Int, Int, Int>(none(), ofObj(2), fun3)

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testLift2_whenThirdIsNone() {
        val op = ofObj(1).lift<Int, Int, Int>(ofObj(2), none(), fun3)

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testLift3_whenAllSome() {
        val one = 1
        val two = 2
        val three = 3
        val four = 4

        val op = ofObj(one).lift(ofObj(two), ofObj(three), ofObj(four), fun4)

        assertThat(op.isSome).isTrue()
        assertEquals(fun4.call(one, two, three, four),
                getUnsafe(op))
    }

    @Test
    fun testLift3_whenAnyIsNone() {
        val op = ofObj(1).lift<Int, Int, Int, Int>(ofObj(2), none(), ofObj(3), fun4)

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testLift3_whenFirstIsNone() {
        val op = none<Int>().lift(ofObj(1), ofObj(2), ofObj(3), fun4)

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testLiftMany_whenFirstIsNone_returnNone() {
        val op = none<Int>().lift(1.rangeTo(4).map { ofObj(it) }, { it })

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testLiftMany_whenAnyIsNone_returnNone() {
        val op = ofObj(1).lift(2.rangeTo(4).map { ofObj(it) }.plus(none()), { it })

        assertThat(op.isSome).isFalse()
    }

    @Test
    fun testLiftMany_whenAllAreSome_returnSome() {
        val funN = polanski.option.function.FuncN<Int> {
            it.filterIsInstance<Int>()
                    .sum()
        }
        val first = 1
        val rest = 1..4
        val op = ofObj(first).lift(rest.map { ofObj(it) }, funN)

        assertThat(op.isSome).isTrue()
        assertEquals(rest.sum() + first, getUnsafe(op))
    }

    @Test
    fun testToString_whenSome() {
        val value = 1

        val result = ofObj(value).toString()

        assertEquals(value.toString(), result)
    }

    @Test
    fun testToString_whenNone() {
        val result = NONE.toString()

        assertEquals(NONE.javaClass.simpleName, result)
    }

    @Test
    fun testHashCode_whenSome() {
        val value = 1

        val result = ofObj(value).hashCode()

        assertEquals(value.hashCode().toLong(), result.toLong())
    }

    @Test
    fun testHashCode_whenNone() {
        val result = NONE.hashCode()

        assertEquals(0, result.toLong())
    }

    @Test
    fun testOrThrowUnsafe_whenSome() {
        val value = 1

        val result = OptionUnsafe.orThrowUnsafe(ofObj(value), RuntimeException())

        assertEquals(value.toLong(), result.toLong())
    }

    @Test(expected = RuntimeException::class)
    fun testOrThrowUnsafe_whenNone() {
        OptionUnsafe.orThrowUnsafe(NONE, RuntimeException())
    }

    @Test
    fun testLog_usesLoggingFunction() {
        val loggingFun = mock(IFunction::class.java)

        ofObj("").log { loggingFun.`fun`(it) }

        verify(loggingFun, times(1)).`fun`(anyString())
    }

    @Test
    fun testLog_containsValueOfSome() {
        val loggingFun = mock(IFunction::class.java)
        val option = ofObj("something")

        option.log { loggingFun.`fun`(it) }

        verify(loggingFun, times(1)).`fun`(option.toString())
    }

    @Test
    fun testLog_containsNone() {
        val option = NONE
        val loggingFun = mock(IFunction::class.java)

        option.log { loggingFun.`fun`(it) }

        verify(loggingFun, times(1)).`fun`(option.toString())
    }

    @Test
    fun testLog_usesLoggingFunctionWithTag() {
        val loggingFun = mock(IFunction::class.java)

        ofObj("").log("") { loggingFun.`fun`(it) }

        verify(loggingFun, times(1)).`fun`(anyString())
    }

    @Test
    fun testLog_usesTag() {
        val tag = "someTag"

        ofObj("").log(tag) { assertTrue(it.contains(tag)) }
    }

    @Test
    fun testLog_withTag_containsValueOfSome() {
        val option = ofObj("something")

        option.log("any") { assertTrue(it.contains(option.toString())) }
    }

    @Test
    fun testNone_returnsNONE() {
        assertEquals(Option.NONE, Option.none<Any>())
    }

    @Test(expected = IllegalStateException::class)
    fun testGetUnsafe_none() {
        Option.NONE.unsafe
    }

    @Test
    fun testGetUnsafe_some() {
        val value = 1

        assertEquals(value, ofObj(value).unsafe)
    }

    internal interface IFunction {

        fun `fun`()

        fun `fun`(str: String)
    }
}


