package org.david.DeepResearch.Formats

import kotlinx.serialization.Serializable

@Serializable
data class PlannerFormat(val subTasks: List<String>) {}