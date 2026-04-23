package common.command.console

import com.marcpg.dreath.util.io.ReplacedPrintStream
import org.jline.reader.LineReader

internal class LineReaderPrintStream(val lineReader: LineReader) : ReplacedPrintStream(System.out) { // <- System.out will never actually be used.
    override fun replacement(any: Any?) {
        lineReader.printAbove(any?.toString())
    }
}
