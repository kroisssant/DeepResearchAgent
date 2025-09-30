package org.example.DeepResearch.Formats

import kotlinx.serialization.Serializable

@Serializable
data class VerificationResult (val approve: Boolean, val suggestion: String)