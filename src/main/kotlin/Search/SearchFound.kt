package org.example.Search

import kotlinx.serialization.Serializable

@Serializable
data class SearchFound(val url: String,
                       val title: String,
                       val content: String
)

@Serializable
data class SearchResult(val results: List<SearchFound>)
// {"url": "https://www.spectroscopyonline.com/view/why-sky-blue", "title": "Why Is the Sky Blue?", "content": "1 apr 2013 \u2014 A detailed look at what makes the sky appear blue in color, using Rayleigh scattering, Maxwell's equations, and the Mie theory.", "thumbnail": null, "engine": "google", "template": "default.html", "parsed_url": ["https", "www.spectroscopyonline.com", "/view/why-sky-blue", "", "", ""], "img_src": "", "priority": "", "engines": ["google"], "positions": [7], "score": 0.14285714285714285, "category": "general", "publishedDate": null}