package org.example.DeepResearch.Subgraphs

import DeepResearch.Subgraphs.subgraphExtractInfoFromWebpage
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphDelegate
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.params.LLMParams

import it.skrape.core.htmlDocument
import it.skrape.selects.html5.body
import org.example.ApiClient.IApiClient
import org.example.ApiClient.OkHttpApiClient
import org.example.DeepResearch.Formats.PlannerFormat
import org.example.DeepResearch.Formats.QueryListFormat
import org.example.DeepResearch.Formats.WebpageResult
import org.example.DeepResearch.Nodes.nodeSummerize

import org.example.Search.Browser
import org.example.Search.Searcxng
import org.example.researchManagerPrompt
import org.example.searchPrompt
import org.example.summerizePrompt


fun AIAgentSubgraphBuilderBase<*, *>.subgraphExecuteDeepSearchSearchOnTopic(
    llmModel: LLModel? = null,
    llmParams: LLMParams? = null
): AIAgentSubgraphDelegate<String, List<String>> = subgraph(
    llmModel = llmModel,
    llmParams = llmParams,
) {
    val browser: Browser = Searcxng()

    val nodeSummery by nodeSummerize()
    val extractInfo by subgraphExtractInfoFromWebpage()
    val urlList: MutableList<String> = mutableListOf()
    val summeryOfResults: MutableList<String> = mutableListOf()
    val planQueryNode by node<String, QueryListFormat>("plan-node") {
        val res=  llm.writeSession() {
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
            val urls = browser.search(urlList[q])
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


