package org.david

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.features.opentelemetry.feature.OpenTelemetry
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.executor.model.PromptExecutor
import ai.koog.prompt.llm.OllamaModels
import org.david.DeepResearch.deepResearchStrategy

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

   val input: String = readlnOrNull() ?: ""
    buildPrompts(input)
    val res = agent.run(
        input
    )

    println(res)
}

fun buildPrompts(initMessage: String) {
    searchPrompt += initMessage
    summerizePrompt += initMessage
    writerPrompt += initMessage
}
