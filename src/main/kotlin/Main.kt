package org.example

import ai.koog.agents.core.dsl.builder.AIAgentStrategyBuilder
import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.memory.feature.AgentMemory
import ai.koog.agents.snapshot.feature.Persistency
import ai.koog.agents.snapshot.providers.InMemoryPersistencyStorageProvider
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeLLMRequest
import ai.koog.agents.ext.agent.chatAgentStrategy
import ai.koog.agents.features.opentelemetry.feature.OpenTelemetry
import ai.koog.prompt.dsl.Prompt
import ai.koog.prompt.executor.model.PromptExecutor
import ai.koog.prompt.llm.OllamaModels
import ai.koog.prompt.message.Message
import ai.koog.prompt.params.LLMParams
import ai.koog.prompt.structure.StructuredResponse
import org.example.DeepResearch.deepResearchStrategy

suspend fun main() {
    val executor: PromptExecutor = simpleOllamaAIExecutor()

    val agent = AIAgent(
        executor = executor,
        llmModel = OllamaModels.Meta.LLAMA_3_2_3B,
        strategy = deepResearchStrategy,
        maxIterations = 10000,
        installFeatures = {
            install(OpenTelemetry) {
            }
        }
    )

   val input: String = readLine() ?: ""
    buildPrompts(input)
    val res = agent.run(
        input
    )

    println(res)
}

fun buildPrompts(initMessage: String) {
    searchPrompt = searchPrompt + initMessage
    summerizePrompt = summerizePrompt + initMessage
    writerPrompt = writerPrompt + initMessage
}
