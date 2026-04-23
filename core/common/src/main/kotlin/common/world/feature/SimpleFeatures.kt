package common.world.feature

import com.marcpg.dreath.scsdfwt.SDF
import com.marcpg.dreath.util.Seed
import com.marcpg.dreath.world.feature.Feature
import common.util.SimplexNoise2D
import kotlin.math.max

class HeightmapFeature(override val seed: Seed, val intensity: Double) : Feature {
    override val name: String = "heightmap"

    private val noise = SimplexNoise2D(seed)

    override fun getSDF(): SDF = SDF { x, y, z ->
        y - noise.noise(x * 0.02, z * 0.01) * intensity
    }

    override fun getShaderSDF(): String = """
        float $glslFuncName(vec3 pos) {
            return pos.y - simplexNoise2D(pos.x * 0.01, pos.z * 0.01) * 20.0;
        }
    """.trimIndent()
}

class SeaLevelFeature(private val height: Double = 0.0): Feature {
    override val name: String = "sea-level"

    override fun getSDF(): SDF = SDF { x, y, z ->
        max(0.0, y - height)
    }

    override fun getShaderSDF(): String = """
        float $glslFuncName(vec3 pos) {
            return max(0.0, pos.y - $height);
        }
    """.trimIndent()
}
