package br.com.stone.posandroid.hal.demo.rule

/**
 * Annotation used to indicate the precondition that must be met before proceeding with test execution.
 *
 * This annotation is used in conjuction with [ConditionTestRule].
 */
annotation class Precondition(val text: String)
