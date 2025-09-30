package org.example.DeepResearch.Formats

import kotlinx.serialization.Serializable

@Serializable
data class PlannerFormat(val subTasks: List<String>) {}