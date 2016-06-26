package polanski.option

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import polanski.option.Option.NONE
import polanski.option.Option.ofObj

class AtomicOptionTest {

    @Test
    fun isNone_byDefault() {
        assertThat(AtomicOption<Unit>().get()).isEqualTo(NONE)
    }

    @Test
    fun constructor_setsTheValue() {
        val str = "Something"
        assertThat(AtomicOption(str).get()).isEqualTo(ofObj(str))
    }

    @Test
    fun getAndClear_returnsOldValue() {
        val str = "Something"
        val atomic = AtomicOption(str)

        assertThat(atomic.getAndClear()).isEqualTo(ofObj(str))
    }

    @Test
    fun getAndClear_clearsPreviousValue() {
        val atomic = AtomicOption("Something")

        atomic.getAndClear()

        assertThat(atomic.get()).isEqualTo(NONE)
    }

    @Test
    fun setIfNone_whenValueIsNotNone_returnFalse() {
        val atomic = AtomicOption("")

        assertThat(atomic.setIfNone("String")).isFalse()
    }

    @Test
    fun setIfNone_whenValueIsNone_returnTrue() {
        val atomic = AtomicOption<String>()

        assertThat(atomic.setIfNone("String")).isTrue()
    }

    @Test
    fun setIfNone_setsNewValue() {
        val newString = "Str"
        val atomic = AtomicOption<String>()
        atomic.setIfNone(newString)

        assertThat(atomic.get()).isEqualTo(ofObj(newString))
    }
}
