package polanski.option

import org.junit.Test

class OptionUnsafeTest {

    @Test(expected = AssertionError::class)
    fun constructor_throwsException() {
        OptionUnsafe()
    }
}
