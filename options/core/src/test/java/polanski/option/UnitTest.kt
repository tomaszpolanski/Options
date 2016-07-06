package polanski.option

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import polanski.option.Unit.*
import rx.functions.Action0

class UnitTest {
    @Test
    fun testFrom_returnsUnit() {
        assertThat(from(mock(Action0::class.java))).isEqualTo(DEFAULT)
    }

    @Test
    fun testFrom_callsAction() {
        val action = mock(Action0::class.java)

        from(action)

        Mockito.verify(action).call()
    }

    @Test
    fun testAsUnit_returnsUnit() {
        assertThat(asUnit(mock(Any::class.java))).isEqualTo(DEFAULT)
    }
}
