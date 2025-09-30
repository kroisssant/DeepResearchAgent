# This is a deep research agent created for the AI Agent project at JetBrains.
This agent is designed to perform in-depth research on a given topic, gather relevant information from various sources, and compile the findings into a comprehensive report.

## Features
- **Topic Analysis**: Understands the given topic and identifies key areas for research.
- **Information Gathering**: Searches for relevant articles, papers, and other resources.
- **Data Extraction**: Extracts important information and insights from the gathered resources.
- **Report Generation**: Compiles the findings into a well-structured report.
- **Citation Management**: Keeps track of sources and provides proper citations in the report.
- **Verification of Summeries**: Ensures that all summaries and extracted information are accurate and reliable(this is done with a LLM model so it's not 100% accurate, in the future this can be changed with a tool similar to LOKI).

## Requirements

- Ollama (for local LLM model usage)
- Llama3.2:3b model downloaded and set up in Ollama
- Searxng local instance that supports JSON output, running on port 8888 (for web searching)

## Usage
1. Clone the repository:
   ```bash
   git clone https://github.com/kroisssant/DeepResearchAgent.git
   ```
2. Navigate to the project directory:
   ```bash
    cd deep-research-agent
    ```
3. Compile the project:
   ```bash
   ./gradlew build
   ```
4. Run the agent with a specified topic

### If you with to change the model please to that in the main.kt file using the koog library

## Future Improvements
- **Enhanced Source Verification**: Implement more robust methods for verifying the credibility of sources.
- **Interactive Reports**: Create interactive reports with visualizations and links to sources.
- **User Feedback Integration**: Allow users to provide feedback on the reports for continuous improvement.
- **Separate Agents for Reusability**: Break down the agent into smaller, specialized agents for tasks like searching, summarizing, and report generation.
- **Better Error Handling**: Implement more comprehensive error handling and recovery mechanisms.
- **Better Task Management**: Improve the way tasks are managed and prioritized within the agent.
- **Scraper Logic**: Implement more advanced scraping techniques to gather information from a wider range of sources.
- **Improve Usability**: Create a list of environment variables or a config file to make it easier to set up and use the agent.
- **External Citation Management**: Integrate with external citation management tools for better handling of references.