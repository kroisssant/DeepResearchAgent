package org.david.DeepResearch.Subgraphs

import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphDelegate
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.params.LLMParams
import org.david.DeepResearch.Formats.QueryListFormat
import org.david.Search.Browser
import org.david.Search.Searcxng
import org.david.searchPrompt


fun AIAgentSubgraphBuilderBase<*, *>.subgraphExecuteDeepSearchSearchOnTopic(
    llmModel: LLModel? = null,
    llmParams: LLMParams? = null
): AIAgentSubgraphDelegate<String, List<String>> = subgraph(
    llmModel = llmModel,
    llmParams = llmParams,
) {
    val browser: Browser = Searcxng()

    val extractInfo by subgraphExtractInfoFromWebpage()
    val urlList: MutableList<String> = mutableListOf()
    val summeryOfResults: MutableList<String> = mutableListOf()
    val planQueryNode by node<String, QueryListFormat>("plan-query-node") {
        urlList.clear()
        val res=  llm.writeSession{
            rewritePrompt {
                it.copy(messages = emptyList())
            }
            updatePrompt {
                system (searchPrompt)
                user(it)
            }
            this.requestLLMStructured<QueryListFormat>()
        }
        res.getOrNull()?.structure?: throw IllegalStateException("No plan generated")
    }

    val getUrls by node <List<String>, Unit>("get-urls") {
        for( q in 0..<it.size.coerceAtMost(5)) {
            val urls = browser.search(it[q])
            for(u in 0..<urls.size.coerceAtMost(5)) {
                urlList.add(urls[u].url)
            }
        }

    }

    val getUrlAndGetOutput by node<Unit, String>("getUrlAndSummeryOfRes") {
        print(urlList.size)
        urlList.removeFirst()
    }

    val appendToResults by node<String, Unit> {
        if (it != "null") {
            summeryOfResults.add(it)
        }

    }




    edge(nodeStart forwardTo planQueryNode)
    edge(planQueryNode forwardTo getUrls transformed {it.queries})
    edge(getUrls forwardTo getUrlAndGetOutput)
    edge(getUrlAndGetOutput forwardTo extractInfo)
    edge(extractInfo forwardTo appendToResults)
    edge(appendToResults forwardTo getUrlAndGetOutput onCondition {urlList.isNotEmpty()})
    edge(appendToResults forwardTo nodeFinish onCondition {urlList.isEmpty()} transformed {summeryOfResults})

}


