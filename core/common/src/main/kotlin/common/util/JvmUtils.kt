package common.util

fun onShutdownProcess(block: () -> Unit) {
    Runtime.getRuntime().addShutdownHook(Thread(block))
}

fun keepAliveProcess() {
    Thread.startVirtualThread {
        try {
            Thread.sleep(Long.MAX_VALUE)
        } catch (_: InterruptedException) {}
    }
}
