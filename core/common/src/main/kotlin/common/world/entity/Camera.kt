package common.world.entity

import com.marcpg.dreath.gameobj.GameObj
import com.marcpg.dreath.gameobj.PhysicalObj
import com.marcpg.dreath.util.vector.MutableLocation
import org.joml.Matrix3f
import org.joml.Vector3f
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Camera(
    override val location: MutableLocation,
    override val velocity: Vector3f,
    var yawRange: Float = 360f,
    var pitchRange: Float = 360f,
    var rollRange: Float = 360f,
    yaw: Float = 0f,
    pitch: Float = 0f,
    roll: Float = 0f,
) : PhysicalObj {
    companion object {
        const val PI_F = PI.toFloat()

        const val DEG_TO_RAD = (PI / 180.0).toFloat()
        const val RAD_TO_DEG = (180.0 / PI).toFloat()
    }

    override val owner: GameObj
        get() = location.world!!

    var yaw: Float = yaw
        set(value) {
            field = normalizeDegrees(value, yawRange)
            recalculateValues()
        }
    var radYaw: Float = 0f; private set
    var cosYaw: Float = 0f; private set
    var sinYaw: Float = 0f; private set

    var pitch: Float = pitch
        set(value) {
            field = normalizeDegrees(value, pitchRange)
            recalculateValues()
        }
    var radPitch: Float = 0f; private set
    var cosPitch: Float = 0f; private set
    var sinPitch: Float = 0f; private set

    var roll: Float = roll
        set(value) {
            field = normalizeDegrees(value, rollRange)
            recalculateValues()
        }
    var radRoll: Float = 0f; private set
    var cosRoll: Float = 0f; private set
    var sinRoll: Float = 0f; private set

    var rotationMatrix: Matrix3f = Matrix3f(); private set
    val forwardVec: Vector3f = Vector3f()
    val rightVec: Vector3f = Vector3f()
    val upVec: Vector3f = Vector3f()

    init {
        recalculateValues()
    }

    private fun recalculateValues() {
        radYaw = yaw * DEG_TO_RAD
        cosYaw = cos(radYaw)
        sinYaw = sin(radYaw)

        radPitch = pitch * DEG_TO_RAD
        cosPitch = cos(radPitch)
        sinPitch = sin(radPitch)

        radRoll = roll * DEG_TO_RAD
        cosRoll = cos(radRoll)
        sinRoll = sin(radRoll)

        rotationMatrix = Matrix3f(
            cosYaw * cosRoll + sinYaw * sinPitch * sinRoll,     sinRoll * cosPitch, -sinYaw * cosRoll + cosYaw * sinPitch * sinRoll,
            -cosYaw * sinRoll + sinYaw * sinPitch * cosRoll,    cosRoll * cosPitch, sinYaw * sinRoll + cosYaw * sinPitch * cosRoll,
            sinYaw * cosPitch,                                  -sinPitch,          cosYaw * cosPitch
        )

        forwardVec.set(0f, 0f, -1f).mul(rotationMatrix)
        rightVec.set(1f, 0f, 0f).mul(rotationMatrix)
        upVec.set(0f, 1f, 0f).mul(rotationMatrix)
    }

    private fun normalizeDegrees(angle: Float, range: Float): Float {
        var result = angle % range

        if (result <= -(range / 2))
            result += range
        else if (result > range / 2)
            result -= range

        return result
    }
}
