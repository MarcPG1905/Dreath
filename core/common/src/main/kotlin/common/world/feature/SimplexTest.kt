package common.world.feature

import com.marcpg.dreath.util.Seed
import com.marcpg.dreath.world.World
import common.util.SimplexNoise2D

fun main() {
    SimplexTest.run(Seed(124L))
}

object SimplexTest {
    fun run(seed: Seed) {
        val noise = SimplexNoise2D(seed)
        println("Noise at (1,1): ${noise.noise(1.0, 1.0)}")

        val generator = World(seed, "test-world")

        generator.addFeature(HeightmapFeature(seed, 25.0))

        val sdf = generator.getSDF()
        println("Distance at (5,10,0): ${sdf(5.0, 10.0, 0.0)}")
    }
}
