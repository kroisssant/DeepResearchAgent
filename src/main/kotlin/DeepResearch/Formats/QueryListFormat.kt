package org.david.DeepResearch.Formats

import kotlinx.serialization.Serializable

@Serializable
data class QueryListFormat(val queries: List<String>) {}