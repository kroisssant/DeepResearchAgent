package org.example.Search

import java.net.URL

interface Browser {
    fun search(query: String): List<SearchFound>
}
