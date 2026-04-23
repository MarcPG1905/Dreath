package com.marcpg.dreath.gameobj

/**
 * Represents anything that can exist in the game and has an owner, physical or not.
 */
interface GameObj {
    /**
     * This object's owner.
     *
     * Should never be `null`, except for very specific cases, which aren't of any mod author's concern.
     */
    val owner: GameObj?
}
