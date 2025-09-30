package org.david.Tools

import it.skrape.core.htmlDocument
import it.skrape.selects.html5.body
import kotlin.collections.filter

fun htmlToText(html: String): String {
    return htmlDocument(html) {
        // Select all direct children of body that are not <script>
        body {
            findAll {
                filter { it.tagName != "script" }
                    .joinToString(" ") { it.text }
            }
        }
    }
}