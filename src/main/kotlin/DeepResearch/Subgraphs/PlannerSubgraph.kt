package org.example.DeepResearch.Subgraphs

import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphDelegate
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.params.LLMParams
import org.apache.xpath.operations.Bool
import org.example.ApiClient.IApiClient
import org.example.ApiClient.OkHttpApiClient
import org.example.DeepResearch.Formats.PlannerFormat
import org.example.researchManagerPrompt


fun AIAgentSubgraphBuilderBase<*, *>.subgraphPlanner(
    llmModel: LLModel? = null,
    llmParams: LLMParams? = null
): AIAgentSubgraphDelegate<String, List<String>> = subgraph(
    llmModel = llmModel,
    llmParams = llmParams,
) {
    val tasks = mutableListOf<String>()
    var initialMessage: String = ""
    val planNode by node<String, Unit>("plan-node") {
        initialMessage = it
        val res=  llm.writeSession() {
            updatePrompt {
                system (researchManagerPrompt)
                user(it)
            }
            this.requestLLMStructured<PlannerFormat>()
        }

        tasks.addAll(res.getOrNull()?.structure?.subTasks?: throw IllegalStateException("No plan generated"))
    }

    val verifyNumberOfTasks by node<Unit, Boolean>("verify-number-of-tasks") {
        if(tasks.size < 10) {
            false
        }
        true
    }

    edge(nodeStart forwardTo planNode)
    edge(planNode forwardTo verifyNumberOfTasks)
    edge(verifyNumberOfTasks forwardTo planNode onCondition { !it } transformed { initialMessage + "Make sure only 10 tasks" })
    edge(verifyNumberOfTasks forwardTo nodeFinish onCondition { it } transformed { tasks })

}