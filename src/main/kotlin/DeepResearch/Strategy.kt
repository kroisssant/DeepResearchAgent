package org.example.DeepResearch

import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeLLMRequest
import ai.koog.agents.core.tools.ToolParameterType
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.ext.agent.subgraphWithTask
import ai.koog.prompt.dsl.Prompt
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.structure.StructuredOutputConfig
import ai.koog.prompt.structure.json.JsonStructuredData
import ai.koog.prompt.structure.json.generator.BasicJsonSchemaGenerator
import kotlinx.serialization.Serializable
import org.example.DeepResearch.Formats.PlannerFormat
import org.example.DeepResearch.Formats.WebpageResult
import org.example.DeepResearch.Subgraphs.subgraphExecuteDeepSearchSearchOnTopic
import org.example.DeepResearch.Subgraphs.subgraphPlanner
import org.example.DeepResearch.Subgraphs.subgraphWrite
import org.example.researchManagerPrompt
import java.time.InstantSource.system



val json = JsonStructuredData.createJsonStructure<PlannerFormat>(
    schemaGenerator = BasicJsonSchemaGenerator.Default,
)


val deepResearchStrategy =strategy<String, String>("deep-research-strategy") {
    var tasks: MutableList<String> = mutableListOf()
    var currentTaskIndex = -1
    val plannerSubgraph by subgraphPlanner()
    val querySubgraph by subgraphExecuteDeepSearchSearchOnTopic()
    val writerSubgraph by subgraphWrite()
    val findings: MutableList<String> = mutableListOf()

    val storeTasks by node<List<String>, Boolean> ("store-tasks") {
        tasks.addAll(it)
    }

    val getAndPrint by node<Boolean, Int>("get-and-print") {
        currentTaskIndex++
        if(currentTaskIndex<tasks.size) {
            print("Starting task ${currentTaskIndex+1} of ${tasks.size}: ${tasks[currentTaskIndex]}")
            currentTaskIndex
        } else {
            -1
        }
    }

    val saveToFindings by node<List<String>, Boolean>("save-to-findings") {
        print(it)
        findings.addAll(it)
        true
    }

    edge(nodeStart forwardTo plannerSubgraph)
    edge(plannerSubgraph forwardTo storeTasks)
    edge(storeTasks forwardTo getAndPrint)
    edge(querySubgraph forwardTo saveToFindings)
    edge(saveToFindings forwardTo getAndPrint)
    edge(getAndPrint forwardTo querySubgraph onCondition {it!=-1} transformed {tasks[currentTaskIndex]})
    edge(getAndPrint forwardTo writerSubgraph onCondition {it==-1} transformed {findings})
    edge(writerSubgraph forwardTo nodeFinish)

}