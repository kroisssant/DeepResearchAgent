package org.david.DeepResearch.Nodes

import ai.koog.agents.core.dsl.builder.AIAgentNodeDelegate
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.prompt.message.Message
import org.david.DeepResearch.Formats.VerificationResult
import org.david.summerizePrompt
import org.david.verificationPrompt

fun AIAgentSubgraphBuilderBase<*, *>.nodeVerify(
    name: String? = null,
    allowToolCalls: Boolean = true,
    initialMessages: Message? = null,
): AIAgentNodeDelegate<String, VerificationResult> =
    node(name) {
        message ->
        val summery = llm.writeSession {
            rewritePrompt {
                it.copy(messages = emptyList())
            }
            updatePrompt {
                system(verificationPrompt + initialMessages)
                user(message)
            }

            this.requestLLMStructured<VerificationResult>()
        }
        if (summery != null) {
            summery.getOrNull()?.structure ?: VerificationResult(false, "Could not verify the content")
        } else {
            VerificationResult(false, "Could not verify the content")
        }
    }