package org.redrune.game

/**
 * The flags that can be altered for the game are stored here.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
object GameFlags {

    /**
     * The id of the world that will be ran
     */
    @JvmField
    var worldId: Byte = 0

    /**
     * If the game is running on developer mode (debug)
     */
    @JvmField
    var debugMode = false

    /**
     * If sql is enabled
     */
    @JvmField
    var webIntegrated = false
}