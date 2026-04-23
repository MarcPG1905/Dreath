package common.util

import com.marcpg.dreath.util.Seed
import java.util.*
import kotlin.math.floor
import kotlin.math.sqrt

class SimplexNoise2D(seed: Seed = Seed.ZERO) {
    private val grad3 = arrayOf(
        intArrayOf(1, 1), intArrayOf(-1, 1), intArrayOf(1, -1), intArrayOf(-1, -1),
        intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(0, -1)
    )

    private val p = IntArray(256)
    private val perm = IntArray(512)
    private val permMod8 = IntArray(512)

    init {
        val random = Random(seed.value)
        for (i in 0 until 256) p[i] = i
        for (i in 255 downTo 0) {
            val j = random.nextInt(i + 1)
            val tmp = p[i]
            p[i] = p[j]
            p[j] = tmp
        }
        for (i in 0 until 512) {
            perm[i] = p[i and 255]
            permMod8[i] = perm[i] % 8
        }
    }

    private fun dot(g: IntArray, x: Double, y: Double): Double = g[0] * x + g[1] * y

    @Suppress("LocalVariableName")
    fun noise(xin: Double, yin: Double): Double {
        val F2 = 0.5 * (sqrt(3.0) - 1.0)
        val G2 = (3.0 - sqrt(3.0)) / 6.0

        // Skew the input space to determine which simplex cell we're in
        val s = (xin + yin) * F2
        val i = floor(xin + s).toInt()
        val j = floor(yin + s).toInt()

        val t = (i + j) * G2
        val X0 = i - t
        val Y0 = j - t
        val x0 = xin - X0
        val y0 = yin - Y0

        // Determine which simplex we’re in
        val (i1, j1) = if (x0 > y0) 1 to 0 else 0 to 1

        // Offsets for the middle corner in (x,y) unskewed coords
        val x1 = x0 - i1 + G2
        val y1 = y0 - j1 + G2

        // Offsets for the last corner
        val x2 = x0 - 1.0 + 2.0 * G2
        val y2 = y0 - 1.0 + 2.0 * G2

        // Work out hashed gradient indices of the three simplex corners
        val ii = i and 255
        val jj = j and 255

        val gi0 = permMod8[ii + perm[jj]]
        val gi1 = permMod8[ii + i1 + perm[jj + j1]]
        val gi2 = permMod8[ii + 1 + perm[jj + 1]]

        // Calculate contributions from the three corners
        var n0 = 0.0
        var n1 = 0.0
        var n2 = 0.0

        var t0 = 0.5 - x0 * x0 - y0 * y0
        if (t0 >= 0) {
            t0 *= t0
            n0 = t0 * t0 * dot(grad3[gi0], x0, y0)
        }

        var t1 = 0.5 - x1 * x1 - y1 * y1
        if (t1 >= 0) {
            t1 *= t1
            n1 = t1 * t1 * dot(grad3[gi1], x1, y1)
        }

        var t2 = 0.5 - x2 * x2 - y2 * y2
        if (t2 >= 0) {
            t2 *= t2
            n2 = t2 * t2 * dot(grad3[gi2], x2, y2)
        }

        // Add contributions and scale to range [-1,1]
        return 70.0 * (n0 + n1 + n2)
    }
}
