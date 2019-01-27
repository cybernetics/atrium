package ch.tutteli.atrium.domain.robstoll.lib.creating

import ch.tutteli.atrium.assertions.Assertion
import ch.tutteli.atrium.creating.AssertionPlant
import ch.tutteli.atrium.creating.CollectingAssertionPlant
import ch.tutteli.atrium.creating.CollectingAssertionPlantNullable
import ch.tutteli.atrium.domain.builders.AssertImpl
import ch.tutteli.atrium.domain.creating.feature.extract.FeatureExtractor
import ch.tutteli.atrium.translations.DescriptionListAssertion

fun <T : Any> _get(
    plant: AssertionPlant<out List<T>>,
    index: Int,
    assertionCreator: CollectingAssertionPlant<T>.() -> Unit
): Assertion = extractorForGetCall(index)
    .withParameterObject(createGetParameterObject(plant, index))
    .extractAndAssertIt(assertionCreator)

fun <T> _getNullable(
    plant: AssertionPlant<out List<T>>,
    index: Int,
    assertionCreator: CollectingAssertionPlantNullable<T>.() -> Unit
): Assertion = extractorForGetCall(index)
    .withParameterObjectNullable(createGetParameterObject(plant, index))
    .extractAndAssertIt(assertionCreator)

private fun extractorForGetCall(index: Int) = AssertImpl.feature.extractor.methodCall("get", index)

private fun <T> createGetParameterObject(
    plant: AssertionPlant<out List<T>>,
    index: Int
): FeatureExtractor.ParameterObject<T> = FeatureExtractor.ParameterObject(
    extractionNotSuccessful = DescriptionListAssertion.INDEX_OUT_OF_BOUNDS,
    warningCannotEvaluate = DescriptionListAssertion.CANNOT_EVALUATE_INDEX_OUT_OF_BOUNDS,
    canBeExtracted = { index < plant.subject.size },
    featureExtraction = { plant.subject[index] }
)
