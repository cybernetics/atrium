package ch.tutteli.atrium.api.cc.en_UK

import ch.tutteli.atrium.checking.AssertionChecker
import ch.tutteli.atrium.creating.Assert
import ch.tutteli.atrium.creating.AssertionPlant
import ch.tutteli.atrium.creating.AssertionPlantNullable
import ch.tutteli.atrium.domain.builders.AssertImpl
import ch.tutteli.atrium.reporting.Reporter

/**
 * Makes the assertion that the [Assert.subject][AssertionPlant.subject] is (equal to) [expected].
 *
 * This method might enforce in the future, that [expected] has to be the same type as [Assert.subject][AssertionPlant.subject].
 * Currently the following is possible: `assert(1).toBe(1.0)`
 *
 * @return This plant to support a fluent API.
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
@Deprecated("Use pendant from package en_GB; will be removed with 1.0.0", ReplaceWith("ch.tutteli.atrium.api.cc.en_GB.toBe(expected)"))
fun <T : Any> Assert<T>.toBe(expected: T)
    = addAssertion(AssertImpl.any.toBe(this, expected))

/**
 * Makes the assertion that the [Assert.subject][AssertionPlant.subject] is not (equal to) [expected].
 *
 * This method might enforce in the future, that [expected] has to be the same type as [Assert.subject][AssertionPlant.subject].
 * Currently the following is possible: `assert(1).notToBe(1.0)`
 *
 * @return This plant to support a fluent API.
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
@Deprecated("Use pendant from package en_GB; will be removed with 1.0.0", ReplaceWith("ch.tutteli.atrium.api.cc.en_GB.notToBe(expected)"))
fun <T : Any> Assert<T>.notToBe(expected: T)
    = addAssertion(AssertImpl.any.notToBe(this, expected))

/**
 * Makes the assertion that the [Assert.subject][AssertionPlant.subject] is the same instance as [expected].
 *
 * This method might enforce in the future, that [expected] has to be the same type as [Assert.subject][AssertionPlant.subject].
 * Currently the following is possible: `assert(1).isSame(1.0)`
 *
 * @return This plant to support a fluent API.
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
@Deprecated("Use `isSameAs` from package en_GB; will be removed with 1.0.0", ReplaceWith("isSameAs(expected)", "ch.tutteli.atrium.api.cc.en_GB.isSameAs"))
fun <T : Any> Assert<T>.isSame(expected: T)
    = addAssertion(AssertImpl.any.isSame(this, expected))

/**
 * Makes the assertion that the [Assert.subject][AssertionPlant.subject] is not the same instance as [expected].
 *
 * This method might enforce in the future, that [expected] has to be the same type as [Assert.subject][AssertionPlant.subject].
 * Currently the following is possible: `assert(1).isNotSame(1.0)`
 *
 * @return This plant to support a fluent API.
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
@Deprecated("Use `isNotSameAs` from package en_GB; will be removed with 1.0.0", ReplaceWith("isNotSameAs(expected)", "ch.tutteli.atrium.api.cc.en_GB.isNotSameAs"))
fun <T : Any> Assert<T>.isNotSame(expected: T)
    = addAssertion(AssertImpl.any.isNotSame(this, expected))

/**
 * Makes the assertion that the [Assert.subject][AssertionPlant.subject] is `null`.
 *
 * @return Does not support a fluent API because: what else would you want to assert about `null` anyway?
 *
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
@Deprecated("Will be removed with 1.0.0 because it is redundant in terms of `toBe(null)` without adding enough to be a legit alternative.", ReplaceWith("ch.tutteli.atrium.api.cc.en_GB.toBe(null)"))
fun <T : Any?> AssertionPlantNullable<T>.isNull() {
    addAssertion(AssertImpl.any.isNull(this))
}

/**
 * Can be used to separate assertions when using the fluent API.
 *
 * For instance `assert(1).isLessThan(2).and.isGreaterThan(0)` creates
 * two assertions (not one assertion with two sub-assertions) - the first asserts that 1 is less than 2 and a second
 * asserts that 1 is greater than 0. If the first assertion fails, then usually (depending on the configured
 * [AssertionChecker]) the second assertion is not evaluated.
 *
 * @return This plant to support a fluent API.
 */
@Deprecated("Use pendant from package en_GB; will be removed with 1.0.0", ReplaceWith("ch.tutteli.atrium.api.cc.en_GB.and"))
val <T : Any> AssertionPlant<T>.and: AssertionPlant<T> get() = this

/**
 * Can be used to create a group of sub assertions when using the fluent API.
 *
 * For instance `assert(1).isLessThan(3).and { isEven(); isGreaterThan(1) }` creates
 * two assertions where the second one consists of two sub-assertions. In case the first assertion holds, then the
 * second one is evaluated as a whole. Meaning, even though 1 is not even, it still evaluates that 1 is greater than 1.
 * Hence the reporting might (depending on the configured [Reporter]) contain both failing sub-assertions.
 *
 * @return This plant to support a fluent API.
 */
@Deprecated("Use pendant from package en_GB; will be removed with 1.0.0", ReplaceWith("ch.tutteli.atrium.api.cc.en_GB.and(assertionCreator)"))
infix fun <T : Any> AssertionPlant<T>.and(assertionCreator: Assert<T>.() -> Unit)
    = addAssertionsCreatedBy(assertionCreator)
