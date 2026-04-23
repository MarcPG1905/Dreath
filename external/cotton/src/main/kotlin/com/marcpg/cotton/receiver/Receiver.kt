package com.marcpg.cotton.receiver

import com.marcpg.cotton.boll.Boll
import com.marcpg.libpg.data.time.Time
import java.util.*

/**
 * Something that can receive various types of [Boll]s:
 * - Chat Messages
 * - Toasts
 * - Titles
 * - Text on Screen
 * @author MarcPG
 * @since 0.1.0
 */
interface Receiver {
    companion object {
        /** Default time that a toast will stay. */
        val DEFAULT_TOAST_STAY = Time(10)
        /** Default time that a title will stay. */
        val DEFAULT_TITLE_STAY = Time(7)
        /** Default time that a text will stay on the screen. */
        val DEFAULT_TEXT_STAY = Time(10)

        /** The default X-position of text in P%. */
        const val DEFAULT_TEXT_POS_X = 50.0
        /** The default Y-position of text in P%. */
        const val DEFAULT_TEXT_POS_Y = 50.0
    }

    /** This receiver's locale for translations. */
    val locale: Locale

    /** This receiver's name for reference. */
    val name: String

    /**
     * Sends a chat message to this receiver.
     * The message can contain any formatting supported by this receiver.
     * @param msg The message to send.
     */
    fun sendChat(msg: Boll)

    /**
     * Clears this receiver's chat.
     */
    fun clearChat()

    /**
     * Sends a toast to this receiver, which is a message displayed at the top left or top right corner of the screen.
     * The title and message can both contain any formatting supported by this receiver.
     * @param title The title of the toast.
     * @param msg The message of the toast.
     * @param stay How long the toast will stay. Optional.
     */
    fun sendToast(title: Boll, msg: Boll, stay: Time = DEFAULT_TOAST_STAY)

    /**
     * Clears this receiver's current toast, if one is shown.
     */
    fun clearToast()

    /**
     * Sends a title to this receiver, which is a message displayed in the middle of the screen.
     * The title and subtitle can both contain any formatting supported by this receiver.
     * @param title The big text of the title.
     * @param subtitle The small text of the title. Optional.
     * @param stay How long the title will stay. Optional.
     */
    fun sendTitle(title: Boll, subtitle: Boll? = null, stay: Time = DEFAULT_TITLE_STAY)

    /**
     * Clears this receiver's current title, if one is shown.
     */
    fun clearTitle()

    /**
     * Displays a message at a specific location on the receiver's screen.
     * The text can contain any formatting supported by this receiver.
     * @param msg The message to display.
     * @param stay How long the message will stay. Optional.
     * @param posX The X-position of the text in P%. Optional.
     * @param posY The Y-position of the text in P%. Optional.
     */
    fun displayMessage(msg: Boll, stay: Time = DEFAULT_TEXT_STAY, posX: Double = DEFAULT_TEXT_POS_X, posY: Double = DEFAULT_TEXT_POS_Y)

    /**
     * Clears the displayed message at a specific location on the receiver's screen, if one is shown there.
     */
    fun clearDisplayMessage(posX: Double, posY: Double)

    /**
     * Clears all displayed messages on the receiver's screen.
     */
    fun clearDisplayMessages()
}
