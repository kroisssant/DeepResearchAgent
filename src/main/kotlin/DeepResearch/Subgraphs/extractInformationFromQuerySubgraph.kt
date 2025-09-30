package DeepResearch.Subgraphs;

import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphDelegate
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.params.LLMParams
import org.example.ApiClient.IApiClient
import org.example.ApiClient.OkHttpApiClient
import org.example.DeepResearch.Formats.PlannerFormat
import org.example.DeepResearch.Formats.WebpageResult
import org.example.DeepResearch.Nodes.nodeSummerize
import org.example.DeepResearch.Nodes.nodeVerify
import org.example.Search.SearchResult
import org.example.Tools.htmlToText
import org.example.researchManagerPrompt

fun AIAgentSubgraphBuilderBase<*, *>.subgraphExtractInfoFromWebpage(
    llmModel: LLModel? = null,
    llmParams: LLMParams? = null
): AIAgentSubgraphDelegate<String, String> = subgraph(
    llmModel = llmModel,
    llmParams = llmParams,
) {
    val apiClient: IApiClient = OkHttpApiClient()
    var searchResult: String? = null
    val nodeVerification by nodeVerify()
    val nodeSummery by nodeSummerize()
    var summeryOfResults: String? = null
    var url = ""
    var trails: Int = 0

    val getTexts by node <String, Unit>("get-texts") {
            trails = 0
            var html: String
            url = it
            try {
                html = apiClient.getRequest(it)
                searchResult = htmlToText(html)

            } catch (e: Exception) {
                searchResult = null
            }

    }

    val storeSummery by node<String?, Unit>("store-summery") {
        trails += 1
        println("Trail: $trails")
        if (it != null) {
            summeryOfResults = it
        }
    }

    // In the future the retrial logic can be subtracted to a separate subgraph
    edge(nodeStart forwardTo getTexts)
    edge( getTexts forwardTo nodeSummery onCondition { searchResult != null} transformed { searchResult !!})
    edge(getTexts forwardTo nodeFinish onCondition { searchResult == null} transformed { "null"})
    edge(nodeSummery forwardTo storeSummery transformed { it })
    edge(nodeSummery forwardTo nodeFinish onCondition {it == null} transformed { "null" })
    edge(storeSummery forwardTo nodeVerification transformed { summeryOfResults + "Source Text" + searchResult })
    edge(nodeVerification forwardTo nodeFinish onCondition {it.approve or (trails >= 5) } transformed { WebpageResult(url, summeryOfResults!!).toString() })
    edge(nodeVerification forwardTo nodeSummery onCondition { !it.approve and (trails < 5) } transformed { "The previous summary was incorrect, please try again, here is the text again: " + searchResult!! + "Here is a reason for why the previous summary was incorrect" + it.suggestion })

}