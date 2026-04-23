package common.command.console

import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine

class ConsoleCompleter : Completer, ConsolePart {
    override fun complete(reader: LineReader, line: ParsedLine, candidates: MutableList<Candidate?>) {
        val result = dissect(reader, line) ?: error("Could not dissect input")

        possibleChildren(result.command ?: RootCommand, result.options, result.flags).forEach { (suggestion, description) ->
            candidates.add(Candidate(suggestion, suggestion, null, description, null, null, true))
        }
    }
}
