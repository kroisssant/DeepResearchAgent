package org.david.DeepResearch.Formats

import kotlinx.serialization.Serializable


// In the future this should be inherited from a Results class so that we could implement multiple types of results for multiple data sources types
@Serializable
data class WebpageResult (val url: String, val summery: String)