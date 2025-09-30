package org.david.DeepResearch.Subgraphs

import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphDelegate
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.message.Message
import ai.koog.prompt.params.LLMParams
import org.david.DeepResearch.Nodes.nodeVerify
import org.david.writerPrompt

fun AIAgentSubgraphBuilderBase<*, *>.subgraphWrite(
    llmModel: LLModel? = null,
    llmParams: LLMParams? = null,
    userPrompt: Message? = null
): AIAgentSubgraphDelegate<List<String>, String> = subgraph(
    llmModel = llmModel,
    llmParams = llmParams,
) {
    val verificationResult by nodeVerify()
    val initialMessage: MutableList<String> = mutableListOf()
    var final = ""
    var trail = 0
    val writeReport by node<List<String>, Unit>("write-node"){
        trail += 1
        initialMessage.addAll(it)
        val res=  llm.writeSession() {

            rewritePrompt { prompt ->
                prompt.copy(messages = emptyList())

            }
            updatePrompt {
                system (writerPrompt)
                for(i in 0..<it.size) {
                    user(it[i])
                }
            }
            print(prompt)
            this.requestLLM()
        }
        final = res.content

    }

    edge(nodeStart forwardTo writeReport)
    edge(writeReport forwardTo verificationResult transformed { "Here is the generated report: \n\n" + it + "And here is the data used to generate the report: \n\n" + initialMessage.joinToString("\n\n") })
    edge(verificationResult forwardTo nodeFinish onCondition { it.approve or (trail >= 5)} transformed {final})
    edge(verificationResult forwardTo writeReport onCondition { !it.approve and (trail < 5) } transformed { initialMessage.add("The previous report was incorrect, please try again, here is the report again: " + final + "Here is a reason for why the previous report was incorrect" + it.suggestion) ; initialMessage })

}