package org.example.DeepResearch.Nodes

import ai.koog.agents.core.dsl.builder.AIAgentNodeDelegate
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.prompt.message.Message
import org.example.summerizePrompt

fun AIAgentSubgraphBuilderBase<*, *>.nodeSummerize(
    name: String? = null,
    allowToolCalls: Boolean = true,
    initialMessages: Message? = null,
): AIAgentNodeDelegate<String, String?> =
    node(name) { message ->
        try {
            val summery = llm.writeSession {
                rewritePrompt {
                    it.copy(messages = emptyList())
                }
                updatePrompt {
                    system(summerizePrompt + initialMessages)
                    user(message)
                }

                this.requestLLM()
            }
            summery.content
        } catch (_: Exception) {
            null
        }

    }