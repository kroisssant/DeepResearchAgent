package org.david.Search

interface Browser {
    fun search(query: String): List<SearchFound>
}
