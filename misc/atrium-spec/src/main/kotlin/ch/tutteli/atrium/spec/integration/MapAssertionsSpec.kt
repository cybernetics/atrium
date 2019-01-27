package ch.tutteli.atrium.spec.integration

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.creating.Assert
import ch.tutteli.atrium.spec.AssertionVerbFactory
import ch.tutteli.atrium.spec.describeFun
import ch.tutteli.atrium.translations.DescriptionAnyAssertion
import ch.tutteli.atrium.translations.DescriptionBasic
import ch.tutteli.atrium.translations.DescriptionCollectionAssertion
import ch.tutteli.atrium.translations.DescriptionMapAssertion
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.include

abstract class MapAssertionsSpec(
    verbs: AssertionVerbFactory,
    containsPair: Pair<String, Assert<Map<String, Int>>.(Pair<String, Int>, Array<out Pair<String, Int>>) -> Assert<Map<String, Int>>>,
    containsNullablePair: Pair<String, Assert<Map<String?, Int?>>.(Pair<String?, Int?>, Array<out Pair<String?, Int?>>) -> Assert<Map<String?, Int?>>>,
    containsKeyPair: Pair<String, Assert<out Map<String, *>>.(String) -> Assert<out Map<String, *>>>,
    containsNullableKeyPair: Pair<String, Assert<out Map<String?, *>>.(String?) -> Assert<out Map<String?, *>>>,
    hasSizePair: Pair<String, Assert<Map<String, Int>>.(Int) -> Assert<Map<String, Int>>>,
    isEmptyPair: Pair<String, Assert<Map<String, Int>>.() -> Assert<Map<String, Int>>>,
    isNotEmptyPair: Pair<String, Assert<Map<String, Int>>.() -> Assert<Map<String, Int>>>,
    describePrefix: String = "[Atrium] "
) : Spek({

    include(object : SubjectLessAssertionSpec<Map<String, Int>>(describePrefix,
        containsPair.first to mapToCreateAssertion { containsPair.second(this, "key" to 1, arrayOf()) },
        containsKeyPair.first to mapToCreateAssertion{ containsKeyPair.second(this, "a") },
        hasSizePair.first to mapToCreateAssertion { hasSizePair.second(this, 2) },
        isEmptyPair.first to mapToCreateAssertion { isEmptyPair.second(this) },
        isNotEmptyPair.first to mapToCreateAssertion { isNotEmptyPair.second(this) }
    ) {})
    include(object : SubjectLessAssertionSpec<Map<String?, Int?>>("$describePrefix[nullable Key] ",
        containsNullablePair.first to mapToCreateAssertion{ containsNullablePair.second(this, null to 1, arrayOf("a" to null)) },
        containsNullableKeyPair.first to mapToCreateAssertion{ containsNullableKeyPair.second(this, null) }
    ) {})

    include(object : CheckingAssertionSpec<Map<String, Int>>(verbs, describePrefix,
        checkingTriple(containsPair.first, { containsPair.second(this, "a" to 1, arrayOf("b" to 2)) }, mapOf("a" to 1, "b" to 2), mapOf("a" to 1, "b" to 3)),
        checkingTriple(containsKeyPair.first, {containsKeyPair.second(this, "a")}, mapOf("a" to 1), mapOf("b" to 1)),
        checkingTriple(hasSizePair.first, { hasSizePair.second(this, 1) }, mapOf("a" to 1), mapOf("a" to 1, "b" to 2)),
        checkingTriple(isEmptyPair.first, { isEmptyPair.second(this) }, mapOf(), mapOf("a" to 1, "b" to 2)),
        checkingTriple(isNotEmptyPair.first, { isNotEmptyPair.second(this) }, mapOf("b" to 2), mapOf())
    ) {})
    include(object : CheckingAssertionSpec<Map<String?, Int?>>(verbs, "$describePrefix[nullable Key] ",
        checkingTriple(containsNullablePair.first, {containsNullablePair.second(this, null to 1, arrayOf("a" to null))}, mapOf("a" to null, null to 1), mapOf<String?, Int?>("b" to 1, null to 1)),
        checkingTriple(containsNullableKeyPair.first, {containsNullableKeyPair.second(this, null)}, mapOf("a" to 1, null to 1), mapOf<String?, Int?>("b" to 1))
    ) {})

    fun describeFun(vararg funName: String, body: SpecBody.() -> Unit)
        = describeFun(describePrefix, funName, body = body)

    val assert: (Map<String, Int>) -> Assert<Map<String, Int>> = verbs::checkImmediately
    val expect = verbs::checkException
    val map = mapOf("a" to 1, "b" to 2)
    val fluent = assert(map)

    val (contains, containsFun) = containsPair
    val (containsNullable, containsNullableFun) = containsNullablePair
    val (containsKey, containsKeyFun) = containsKeyPair
    val (containsNullableKey, containsNullableKeyFun) = containsNullableKeyPair
    val (hasSize, hasSizeFun) = hasSizePair
    val (isEmpty, isEmptyFun) = isEmptyPair
    val (isNotEmpty, isNotEmptyFun) = isNotEmptyPair

    val isDescr = DescriptionBasic.IS.getDefault()
    val isNotDescr = DescriptionBasic.IS_NOT.getDefault()
    val empty = DescriptionCollectionAssertion.EMPTY.getDefault()
    val containsKeyDescr = DescriptionMapAssertion.CONTAINS_KEY.getDefault()
    val toBeDescr = DescriptionAnyAssertion.TO_BE.getDefault()
    val keyDoesNotExist = DescriptionMapAssertion.KEY_DOES_NOT_EXIST.getDefault()

    fun entry(key: String): String
        = String.format(DescriptionMapAssertion.ENTRY_WITH_KEY.getDefault(), "\"$key\"")

    fun entry(key: String, value: Any): String
        = entry(key) + ": " + value

    describeFun(contains) {
        context("map $map") {
            listOf(
                mapOf("a" to 1),
                mapOf("b" to 2),
                mapOf("a" to 1, "b" to 2),
                mapOf("b" to 2, "a" to 1)
            ).forEach {
                test("$it does not throw") {
                    val pairs = it.toList()
                    fluent.containsFun(pairs.first(), pairs.drop(1).toTypedArray())
                }
            }

            test("a to 1 and a to 1 does not throw (ignores duplicates)") {
                fluent.containsFun("a" to 1, arrayOf("a" to 1))
            }

            test("{a to 1, b to 3, c to 4} throws AssertionError, reports b and c") {
                expect {
                    fluent.containsFun("a" to 1, arrayOf("b" to 3, "c" to 4))
                }.toThrow<AssertionError>{
                    message {
                        contains(
                            entry("b", 2),
                            "$toBeDescr: 3",
                            entry("c", keyDoesNotExist),
                            "$toBeDescr: 4"
                        )
                        containsNot(entry("a"))
                    }
                }
            }
        }
    }

    describeFun(containsNullable) {
        val nullableMap = mapOf("a" to null, null to 1, "b" to 2)
        val nullableFluent = verbs.checkImmediately(nullableMap)
        context("map $map") {
            listOf(
                mapOf("a" to null),
                mapOf(null to 1),
                mapOf("b" to 2),
                mapOf("a" to null, "b" to 2),
                mapOf(null to 1, "b" to 2),
                mapOf(null to 1, "a" to null),
                mapOf(null to 1, "a" to null, "b" to 2),
                mapOf("b" to 2, null to 1, "a" to null)
            ).forEach {
                test("$it does not throw") {
                    val pairs = it.toList()
                    nullableFluent.containsNullableFun(pairs.first(), pairs.drop(1).toTypedArray())
                }
            }

            test("a to null and a to null does not throw (ignores duplicates)") {
                nullableFluent.containsNullableFun("a" to null, arrayOf("a" to null))
            }

            test("{a to null, null to 2, b to 3, c to 4} throws AssertionError, reports a, null, b and c") {
                expect {
                    nullableFluent.containsNullableFun("a" to null, arrayOf(null to 2, "b" to 3, "c" to 4))
                }.toThrow<AssertionError>{
                    message {
                        contains(
                            entry("b", 2),
                            "$toBeDescr: 3",
                            entry("c", keyDoesNotExist)
                            //TODO seems like notToBeNull is not subjectLess
                            //"$toBeDescr: 4"
                        )
                        containsNot(entry("a"))
                    }
                }
            }
        }
    }

    describeFun(containsKey) {
        it("does not throw if the map contains the key") {
            fluent.containsKeyFun("a")
        }

        it("throws an AssertionError if the map does not contain the key") {
            expect {
                fluent.containsKeyFun("c")
            }.toThrow<AssertionError> { messageContains("$containsKeyDescr: \"c\"")}
        }
    }

    describeFun(containsNullableKey) {
        it("does not throw if the map contains the key") {
            verbs.checkImmediately(mapOf("a" to 1, null to null)).containsNullableKeyFun(null)
        }

        it("throws an AssertionError if the map does not contain the key") {
            expect {
                verbs.checkImmediately(mapOf<String?, Int?>("a" to 1, "b" to 2)).containsNullableKeyFun(null)
            }.toThrow<AssertionError> { messageContains("$containsKeyDescr: null")}
        }
    }

    it("does not throw if null is passed and the map contains null as key") {
        fluent.containsKeyFun("a")
    }

    describeFun(hasSize) {
        context("map with two entries") {
            test("expect 2 does not throw") {
                fluent.hasSizeFun(2)
            }
            test("expect 1 throws an AssertionError") {
                expect {
                    fluent.hasSizeFun(1)
                }.toThrow<AssertionError> {
                    messageContains("size: 2", DescriptionAnyAssertion.TO_BE.getDefault() + ": 1")
                }
            }
            test("expect 3 throws an AssertionError") {
                expect {
                    fluent.hasSizeFun(3)
                }.toThrow<AssertionError> {
                    messageContains("size: 2", DescriptionAnyAssertion.TO_BE.getDefault() + ": 3")
                }
            }
        }
    }

    describeFun(isEmpty) {
        it("does not throw if a map is empty") {
            assert(mapOf()).isEmptyFun()
        }

        it("throws an AssertionError if a map is not empty") {
            expect {
                fluent.isEmptyFun()
            }.toThrow<AssertionError> { messageContains("$isDescr: $empty") }
        }
    }

    describeFun(isNotEmpty) {
        it("does not throw if a map is not empty") {
            fluent.isNotEmptyFun()
        }

        it("throws an AssertionError if a map is empty") {
            expect {
                assert(mapOf()).isNotEmptyFun()
            }.toThrow<AssertionError> { messageContains("$isNotDescr: $empty") }
        }
    }
})
