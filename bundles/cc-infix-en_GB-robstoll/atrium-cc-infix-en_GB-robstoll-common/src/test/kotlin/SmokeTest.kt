import ch.tutteli.atrium.api.cc.infix.en_GB.toBe
import ch.tutteli.atrium.assertions.Assertion
import ch.tutteli.atrium.creating.Assert
import ch.tutteli.atrium.creating.AssertionPlant
import ch.tutteli.atrium.domain.builders.AssertImpl
import ch.tutteli.atrium.reporting.RawString
import ch.tutteli.atrium.reporting.translating.StringBasedTranslatable
import ch.tutteli.atrium.translations.DescriptionBasic
import ch.tutteli.atrium.verbs.assert
import kotlin.test.Test

class SmokeTest {
    @Test
    fun toBe_canBeUsed(){
        assert(1) toBe 1
    }

    @Test
    fun assertionFunctionWithoutI18nCanBeUsed(){
        assert(2) tobe even
    }

    @Test
    fun assertionFunctionWithI18nCanBeUsed(){
        assert(4) isMultipleOf 2
    }
}

@Suppress("ClassName")
object even
infix fun Assert<Int>.tobe(@Suppress("UNUSED_PARAMETER") even: even)
    = createAndAddAssertion(DescriptionBasic.IS, RawString.create("an even number")) { subject % 2 == 0 }

infix fun Assert<Int>.isMultipleOf(base: Int)
    = addAssertion(_isMultipleOf(this, base))

fun _isMultipleOf(plant: AssertionPlant<Int>, base: Int): Assertion
    = AssertImpl.builder.createDescriptive(DescriptionIntAssertions.IS_MULTIPLE_OF, base) { plant.subject % base == 0 }

enum class DescriptionIntAssertions(override val value: String) : StringBasedTranslatable {
    IS_MULTIPLE_OF("is multiple of")
}
